package com.example.board.member.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {UserIdValidator.class})
public @interface UserIdCheck {
    String message() default "아이디는 최소 3 ~ 10 자리 영문 숫자 입니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
