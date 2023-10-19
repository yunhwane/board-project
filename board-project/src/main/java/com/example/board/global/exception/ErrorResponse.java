package com.example.board.global.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;


@Getter
@Builder
@RequiredArgsConstructor
@ApiModel(description = "에러 응답 모델")
public class ErrorResponse {

    @ApiModelProperty(value = "에러 코드")
    private final int code;
    @ApiModelProperty(value = "에러 메시지")
    private final String message;


    // Errors가 없다면 응답이 내려가지 않게 처리
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        // @Valid 로 에러가 들어왔을 때, 어느 필드에서 에러가 발생했는 지에 대한 응답 처리
        private final String field;
        private final String message;
        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
