package com.cyrof.validators;

import com.cyrof.configuration.DatepatternProperties;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DatetimeStartEndValueCompareValidator implements ConstraintValidator<DatetimeStartEndValueCompare, Object> {

    @Autowired
    private DatepatternProperties patternProperties;

    private String start;
    private String end;

    private DateTimeFormatter formatterInput;

    @Override
    public void initialize(DatetimeStartEndValueCompare constraintAnnotation) {
        this.formatterInput = DateTimeFormatter.ofPattern(patternProperties.getInput());
        this.start = constraintAnnotation.start();
        this.end = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        String fieldStart = (String)(new BeanWrapperImpl(value).getPropertyValue(start));
        String fieldEnd = (String)(new BeanWrapperImpl(value).getPropertyValue(end));

        try {
            LocalDateTime startDate = getDate(fieldStart);
            LocalDateTime enddate = getDate(fieldEnd);

            boolean isValid = startDate.isBefore(enddate);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode(start).addConstraintViolation();
            }
            return isValid;

        } catch (DateTimeParseException ex) {
            return false;
        }

    }

    private LocalDateTime getDate(String value) {
        return LocalDateTime.parse(value, formatterInput);
    }
}
