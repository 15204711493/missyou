package com.lin.missyou.dto.validators;

import com.lin.missyou.dto.TokenGetDTO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TokenPasswordValidator implements ConstraintValidator<TokenPassword, String> {

    private Integer min;
    private Integer max;

    @Override
    public void initialize(TokenPassword constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isEmpty(s)){
            return true;
        }
        return  s.length() >= this.min && s.length()<=this.max;
    }
}
