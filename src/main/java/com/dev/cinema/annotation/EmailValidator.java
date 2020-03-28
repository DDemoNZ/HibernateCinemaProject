package com.dev.cinema.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailValid, String> {

    @Override
    public void initialize(EmailValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+"
                + "(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
