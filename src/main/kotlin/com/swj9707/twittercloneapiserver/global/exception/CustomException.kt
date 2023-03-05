package com.swj9707.twittercloneapiserver.global.exception

import com.swj9707.twittercloneapiserver.global.common.enum.ResCode

class CustomException(resCode: ResCode): RuntimeException() {
    val resCode: ResCode = resCode;
}