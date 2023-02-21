package com.swj9707.twittercloneapiserver.constant.enum

import org.springframework.http.HttpStatus

enum class BaseResponseCode(status: HttpStatus, message: String ) {
    //OK
    OK(HttpStatus.OK, "요청 성공"),

    //NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND,  "리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 조건에 맞는 유저를 찾을 수 없습니다."),
    TWEET_NOT_FOUND(HttpStatus.NOT_FOUND, "조건에 맞는 트윗 데이터를 찾을 수 없습니다."),

    //UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token Expired"),

    //BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "올바르지 않은 토큰 요청입니다."),

    //FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN,  "금지된 요청입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "리프레쉬 토큰이 만료되었습니다."),

    //CONFLICT
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 계정 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 유저네임입니다."),
    DUPLICATE_USERNICKNAME(HttpStatus.CONFLICT, "이미 존재하는 유저 닉네임입니다."),
    LOGIN_FAILED(HttpStatus.CONFLICT, "로그인에 실패하였습니다."),

    //INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "내부 서버 오류입니다. 관리자에게 문의하세요"),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하였습니다.");

    val status: HttpStatus = status
    val message : String = message

}