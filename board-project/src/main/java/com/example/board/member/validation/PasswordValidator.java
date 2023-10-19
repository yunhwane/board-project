package com.example.board.member.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PasswordValidator implements ConstraintValidator<PasswordCheck,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
