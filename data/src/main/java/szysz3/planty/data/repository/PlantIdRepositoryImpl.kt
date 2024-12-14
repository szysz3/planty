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
import szysz3.planty.data.network.HttpResponseHandler
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.repository.PlantIdRepository
import timber.log.Timber
import javax.inject.Inject

class PlantIdRepositoryImpl @Inject constructor(
    private val client: OkHttpClient
) : PlantIdRepository {
    
    private val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
    private val responseAdapter by lazy { moshi.adapter(PlantIdResponse::class.java) }

    override suspend fun identifyPlant(
        image: ByteArray,
        apiKey: String
    ): PlantIdResponse {
        return withContext(Dispatchers.IO) {
            try {
                val multipartBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        FORM_DATA_PART_IMAGES,
                        IMAGE_FILE_NAME,
                        RequestBody.create(MIME_TYPE_IMAGE.toMediaTypeOrNull(), image)
                    )
                    .build()

                val request = Request.Builder()
                    .url(API_URL)
                    .post(multipartBody)
                    .addHeader(HEADER_AUTHORIZATION, "Bearer $apiKey")
                    .build()

                client.newCall(request).execute().use { response ->
                    HttpResponseHandler.handleResponse(response) { responseBody ->
                        responseAdapter.fromJson(responseBody)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error identifying plant")
                throw e
            }
        }
    }

    companion object {
        const val API_URL = "https://my-api.plantnet.org/v2/identify/all"
        const val MIME_TYPE_IMAGE = "image/jpeg"
        const val FORM_DATA_PART_IMAGES = "images"
        const val IMAGE_FILE_NAME = "image.jpg"
        const val HEADER_AUTHORIZATION = "Authorization"
    }
}
