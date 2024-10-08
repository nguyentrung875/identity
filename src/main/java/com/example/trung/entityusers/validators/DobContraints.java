package com.example.trung.entityusers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint( //class chịu trách nhiệm cho annotation này
        validatedBy = {DobValidator.class}
)
public @interface DobContraints {
    String message() default "Invalid birthday";

    int min(); //khai báo giá trị tối thiểu

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
