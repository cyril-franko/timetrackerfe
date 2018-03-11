package com.cyrof.validators;

import com.cyrof.configuration.DatepatternProperties;
import groovy.util.logging.Log4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Log4j
public class DatetimeInputValidator implements ConstraintValidator<DatetimeInput, String> {

    @Autowired
    private DatepatternProperties patternProperties;

    private DateTimeFormatter formatterInput;

    @Override
    public void initialize(DatetimeInput constraintAnnotation) {
        this.formatterInput = DateTimeFormatter.ofPattern(patternProperties.getInput());
        constraintAnnotation.message();
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValidInput(value);
    }

    private boolean isValidInput(String input){
        try {
            LocalDateTime.parse(input, formatterInput);
        } catch (DateTimeParseException ex) {
            return false;
        }
        return true;
    }
}
