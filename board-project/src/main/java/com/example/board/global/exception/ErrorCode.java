package com.example.board.global.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;



public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}
