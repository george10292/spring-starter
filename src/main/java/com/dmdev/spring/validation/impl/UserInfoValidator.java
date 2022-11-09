package com.dmdev.spring.validation.impl;

import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.validation.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UserInfoValidator implements ConstraintValidator <UserInfo, UserCreateEditDto> {

    @Override
    public boolean isValid(UserCreateEditDto value, ConstraintValidatorContext context) {
        return StringUtils.hasText( value.getFirstname()) || StringUtils.hasText( value.getLastname() );
    }
}
