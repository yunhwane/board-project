package com.example.board.member.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/*
군번 custom 유효성 검사
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {SerialValidator.class})
public @interface SerialCheck {

    String message() default "군번은 예시 2자리-8자리 입니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
