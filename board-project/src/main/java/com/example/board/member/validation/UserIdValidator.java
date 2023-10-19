package com.example.board.member.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
3자리 이상 16자리 이하 영문+숫자
 */
public class UserIdValidator implements ConstraintValidator<UserIdCheck,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{3,10}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
