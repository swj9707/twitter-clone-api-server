package com.swj9707.twittercloneapiserver.exception

import com.swj9707.twittercloneapiserver.enum.BaseResponseCode

class BaseException(baseResponseCode: BaseResponseCode): RuntimeException() {
    public val baseResponseCode: BaseResponseCode = baseResponseCode;
}