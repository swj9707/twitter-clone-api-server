package com.swj9707.twittercloneapiserver.enum

import org.springframework.http.HttpStatus

enum class BaseResponseCode(status: HttpStatus, message: String ) {
    //OK
    OK(HttpStatus.OK, "요청 성공"),

    //NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND,  "리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 조건에 맞는 유저를 찾을 수 없습니다."),

    //UNAUTHORIZED
    PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    //BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),

    //FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN,  "금지된 요청입니다."),

    //CONFLICT
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 계정 이메일입니다."),

    //INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "내부 서버 오류입니다. 관리자에게 문의하세요");

    public val status: HttpStatus = status
    public val message : String = message

}