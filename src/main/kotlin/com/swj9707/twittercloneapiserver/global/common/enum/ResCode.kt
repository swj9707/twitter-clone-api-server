package com.swj9707.twittercloneapiserver.global.common.enum

import org.springframework.http.HttpStatus

enum class ResCode(status: HttpStatus, message: String, errorCode : String ) {
    //OK
    OK(HttpStatus.OK, "요청 성공", "OK"),

    //NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND,  "리소스를 찾을 수 없습니다.", "ERROR-NF-00"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 조건에 맞는 유저를 찾을 수 없습니다.", "ERROR-NF-01"),
    TWEET_NOT_FOUND(HttpStatus.NOT_FOUND, "조건에 맞는 트윗 데이터를 찾을 수 없습니다.", "ERROR-NF-02"),

    //UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", "ERROR-UA-00"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token Expired", "ERROR-UA-01"),
    TOKEN_ILLEGAL_ARGUMENT(HttpStatus.UNAUTHORIZED, "잘못 된 토큰 요청입니다.", "ERROR-UA-002"),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰 양식입니다.", "ERROR-UA-003"),
    TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "잘못 생성 된 토큰 입니다.", "ERROR-UA-004"),
    TOKEN_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "시그니쳐 키가 잘못 되었습니다.", "ERROR-UA-005"),

    //BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다.", "ERROR-BR-00"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다.", "ERROR-BR-01"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "올바르지 않은 토큰 요청입니다.", "ERROR-BR-02"),

    //FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN,  "금지된 요청입니다.", "ERROR-FB-00"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "리프레쉬 토큰이 만료되었습니다.", "ERROR-FB-01"),

    //CONFLICT
    DUPLICATE(HttpStatus.CONFLICT, "요청에 충돌이 일어났습니다.", "ERROR-CF-00"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 계정 이메일입니다.", "ERROR-CF-01"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 유저네임입니다.", "ERROR-CF-02"),
    DUPLICATE_USER_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 유저 닉네임입니다.", "ERROR-CF-03"),
    LOGIN_FAILED(HttpStatus.CONFLICT, "로그인에 실패하였습니다.", "ERROR-CF-04"),

    //INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "내부 서버 오류입니다. 관리자에게 문의하세요", "ERROR-ISE-00"),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하였습니다.", "ERROR-ISE-01");

    val status: HttpStatus = status
    val message : String = message
    val errorCode : String = errorCode

}