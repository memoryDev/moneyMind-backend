package dev.momory.moneymindbackend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidtor implements ConstraintValidator<EnumValid, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValid annotation) {
        this.enumClass = annotation.target();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        if (!enumClass.isInstance(value)) {
            return false; // Enum 타입이 아니면 유효하지 않음
        }

        Object[] enumValues = enumClass.getEnumConstants();
        for (Object enumValue : enumValues) {
            if (value.equals(enumValue)) {
                return true;
            }
        }

        return false;
    }
}
