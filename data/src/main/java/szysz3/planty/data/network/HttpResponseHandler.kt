package szysz3.planty.data.network

import okhttp3.Response

object HttpResponseHandler {

    fun <T> handleResponse(
        response: Response,
        parseResponse: (String) -> T?
    ): T {
        if (!response.isSuccessful) {
            throwHttpException(response)
        }

        val responseBody = response.body?.string()
            ?: throw InvalidResponseException("Empty response body")

        return parseResponse(responseBody)
            ?: throw InvalidResponseException("Invalid response format")
    }

    private fun throwHttpException(response: Response) {
        when (response.code) {
            400 -> throw BadRequestException()
            401 -> throw UnauthorizedException()
            403 -> throw ForbiddenException()
            404 -> throw NotFoundException()
            500 -> throw InternalServerErrorException()
            else -> throw UnknownHttpException("HTTP ${response.code}: ${response.message}")
        }
    }
}