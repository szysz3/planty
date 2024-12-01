package szysz3.planty.data.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.repository.PlantIdRepository
import javax.inject.Inject

class PlantIdRepositoryImpl @Inject constructor(
    private val client: OkHttpClient
) : PlantIdRepository {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val responseAdapter = moshi.adapter(PlantIdResponse::class.java)

    override suspend fun identifyPlant(
        imageUrls: List<String>,
        apiKey: String
    ): PlantIdResponse {
        return withContext(Dispatchers.IO) {
            val urlBuilder = StringBuilder("https://my-api.plantnet.org/v2/identify/all?")
            urlBuilder.append("api-key=$apiKey")
            imageUrls.forEach { url ->
                urlBuilder.append("&images=$url")
            }

            val request = Request.Builder()
                .url(urlBuilder.toString())
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IllegalStateException("Failed to identify plant: ${response.message}")
                }
                responseAdapter.fromJson(
                    response.body?.string() ?: throw IllegalStateException("Empty response body")
                )
                    ?: throw IllegalStateException("Invalid response format")
            }
        }
    }
}
