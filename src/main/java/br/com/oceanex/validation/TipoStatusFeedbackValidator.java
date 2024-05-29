package br.com.oceanex.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoStatusFeedbackValidator implements ConstraintValidator<TipoStatusFeedback, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.equals("like") || value.equals("deslike");
    }

}
