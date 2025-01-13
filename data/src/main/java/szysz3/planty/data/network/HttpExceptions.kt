package szysz3.planty.data.network

open class HttpException(
    private val errorResponse: HttpErrorResponse?
) : Exception(errorResponse?.message)

class BadRequestException(errorResponse: HttpErrorResponse? = null) : HttpException(errorResponse)
class UnauthorizedException(errorResponse: HttpErrorResponse? = null) : HttpException(errorResponse)
class ForbiddenException(errorResponse: HttpErrorResponse? = null) : HttpException(errorResponse)
class NotFoundException(errorResponse: HttpErrorResponse? = null) : HttpException(errorResponse)
class InternalServerErrorException(errorResponse: HttpErrorResponse? = null) :
    HttpException(errorResponse)

class UnknownHttpException(message: String, errorResponse: HttpErrorResponse? = null) :
    HttpException(errorResponse) {
    init {
        initCause(Exception(message))
    }
}