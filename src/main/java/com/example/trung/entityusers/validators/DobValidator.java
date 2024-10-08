package com.example.trung.entityusers.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

public class DobValidator implements ConstraintValidator<DobContraints, LocalDate> {
    private int min;

    @Override //Mỗi khi Constraint này đc khởi tạo thì sẽ có nhưng thông số mặc định, diễn ra trước
    public void initialize(DobContraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override //diễn ra sau, mỗi Anotation chỉ nên chịu trách nhiệm cho 1 valid cụ thể
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) return true;


        int years = Period.between(date, LocalDate.now()).getYears();

        return years >= min;
    }
}
