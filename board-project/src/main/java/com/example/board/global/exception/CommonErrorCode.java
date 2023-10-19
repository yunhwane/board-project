package com.example.board.global.exception;

import com.example.board.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND,"카테고리가 없습니다."),
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    NOT_FOUND_MEMBER( HttpStatus.NOT_FOUND, "회원 정보가 없습니다."),
    NOT_FOUND_ID(HttpStatus.NOT_FOUND,"계정을 찾을 수 없습니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT,"아이디가 중복됩니다."),
    SERIAL_DUPLICATED(HttpStatus.CONFLICT,"군번이 중복됩니다."),
    INPUT_VALUE_INVALID(HttpStatus.BAD_REQUEST,"입력이 올바르지 않습니다."),
    LOGIN_FAILURE(HttpStatus.BAD_REQUEST,"아이디와 비밀번호가 유효하지 않습니다."),
    USER_ACCOUNT_DISABLED(HttpStatus.FORBIDDEN,"비활성화된 아이디입니다."),
    //500
    INTERVAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
