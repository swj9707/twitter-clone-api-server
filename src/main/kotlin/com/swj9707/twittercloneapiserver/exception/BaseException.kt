package com.swj9707.twittercloneapiserver.exception

import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode

class BaseException(baseResponseCode: BaseResponseCode): RuntimeException() {
    val baseResponseCode: BaseResponseCode = baseResponseCode;
}