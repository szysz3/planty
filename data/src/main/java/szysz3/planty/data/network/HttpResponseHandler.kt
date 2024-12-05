package szysz3.planty.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Response

object HttpResponseHandler {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val errorAdapter = moshi.adapter(HttpErrorResponse::class.java)

    fun <T> handleResponse(
        response: Response,
        parseResponse: (String) -> T?
    ): T {
        if (!response.isSuccessful) {
            throwHttpException(response)
        }

        val responseBody = response.body?.string()
            ?: throw UnknownHttpException("Empty response body")

        return parseResponse(responseBody)
            ?: throw UnknownHttpException("Invalid response format")
    }

    private fun throwHttpException(response: Response) {
        val errorBody = response.body?.string()
        val errorResponse = errorBody?.let { errorAdapter.fromJson(it) }

        when (response.code) {
            400 -> throw BadRequestException(errorResponse)
            401 -> throw UnauthorizedException(errorResponse)
            403 -> throw ForbiddenException(errorResponse)
            404 -> throw NotFoundException(errorResponse)
            500 -> throw InternalServerErrorException(errorResponse)
            else -> throw UnknownHttpException(
                "HTTP ${response.code}: ${response.message}",
                errorResponse
            )
        }
    }
}