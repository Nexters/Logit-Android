package com.useai.core.network.error

import retrofit2.HttpException
import retrofit2.Response

/**
 * 400, 500번대 에러 발생 시 던져지는 에러
 */
internal data class RemoteError(
    val response: Response<*>,
    val errorCode: Int,
    override val message: String,
) : HttpException(response) {

    val statusCode: Int = response.code()
    val httpErrorMessage: String = mapHttpError(statusCode)
}

private fun mapHttpError(code: Int) = when (code) {
    400 -> "Bad Request: 잘못된 요청입니다."
    401 -> "Unauthorized: 인증되지 않은 사용자입니다."
    403 -> "Forbidden: 접근 권한이 없습니다."
    404 -> "Not Found: 요청한 리소스를 찾을 수 없습니다."
    in 500 until 600 -> "Internal Server Error: 서버 내부 오류입니다."
    else -> "Unknown Error: 알 수 없는 오류입니다."
}
