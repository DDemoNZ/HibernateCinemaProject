package com.dev.cinema.annotation;

import com.dev.cinema.model.dto.request.UserAuthenticateRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<PasswordValid,
        UserAuthenticateRequestDto> {

    @Override
    public void initialize(PasswordValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserAuthenticateRequestDto userAuthenticateRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return userAuthenticateRequestDto
                .getPassword().equals(userAuthenticateRequestDto.getRepeatedPassword());
    }
}
