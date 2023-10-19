package com.example.board.member.validation;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class SerialValidator implements ConstraintValidator<SerialCheck,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^\\d{2}-\\d{8}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
