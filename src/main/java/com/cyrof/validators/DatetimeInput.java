package com.cyrof.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatetimeInputValidator.class)
@Documented
public @interface DatetimeInput {

    String message() default "Input does not match required pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
