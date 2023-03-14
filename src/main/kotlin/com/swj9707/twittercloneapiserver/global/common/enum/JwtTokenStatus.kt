package com.swj9707.twittercloneapiserver.global.common.enum

enum class JwtTokenStatus(message: String, errorCode: String) {
    VALID("valid", "TK-OK"), INVALID_SIGNATURE(
        "Invalid JWT Signature",
        "ERROR-TK-01"
    ),
    MALFORMED("Malformed JWT Signature", "ERROR-TK-02"), UNSUPPORTED(
        "Unsupported JWT Token", "ERROR-TK-03"
    ),
    ILLEGAL_ARGUMENT(
        "JWT token compact of handler are invalid",
        "ERROR-TK-04"
    ),
    EXPIRED("Token Expired", "ERROR-TK-05");

    val message: String = message;
    val errorCode : String = errorCode;
}