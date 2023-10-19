package com.example.board.member.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface PasswordCheck {
    String message() default "비밀번호는 영문+숫자+특수기호 8자 이상입니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
