package szysz3.planty.data.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.IOException
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.repository.PlantIdRepository
import javax.inject.Inject

class PlantIdRepositoryImpl @Inject constructor(
    private val client: OkHttpClient
) : PlantIdRepository {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val responseAdapter = moshi.adapter(PlantIdResponse::class.java)

    override suspend fun identifyPlant(
        image: ByteArray, // Image data as ByteArray
        apiKey: String // Mandatory API key
    ): PlantIdResponse {
        return withContext(Dispatchers.IO) {
            // Prepare multipart form data
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "images",
                    "image.jpg",
                    RequestBody.create("image/jpeg".toMediaTypeOrNull(), image)
                )
                .build()

            // Build request
            val url = "https://my-api.plantnet.org/v2/identify/all" // Using 'all' for project
            val request = Request.Builder()
                .url(url)
                .post(multipartBody)
                .addHeader("Authorization", "Bearer $apiKey")
                .build()

            // Execute request and handle response
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Failed to identify plant: ${response.message}")
                }

                response.body?.string()?.let { responseBody ->
                    responseAdapter.fromJson(responseBody)
                        ?: throw IOException("Invalid response format")
                } ?: throw IOException("Empty response body")
            }
        }
    }
}
