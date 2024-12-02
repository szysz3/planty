package szysz3.planty.data.network

sealed class HttpException(message: String) : Exception(message)

class InvalidResponseException(message: String = "Invalid response format") : HttpException(message)

class BadRequestException(message: String = "Bad Request") : HttpException(message)

class UnauthorizedException(message: String = "Unauthorized: Invalid API key") :
    HttpException(message)

class ForbiddenException(message: String = "Forbidden: Access denied") : HttpException(message)

class NotFoundException(message: String = "Not Found: Endpoint does not exist") :
    HttpException(message)

class InternalServerErrorException(message: String = "Internal Server Error") :
    HttpException(message)

class UnknownHttpException(message: String) : HttpException(message)