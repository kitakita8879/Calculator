package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class CalculatorAction {

    public enum Type {
        DIGIT,
        DECIMAL,
        OPERATOR,
        CLEAR,
        DELETE,
        EQUALS,
        PRECISION_DOWN,
        PRECISION_UP
    }

    private final Type mType;
    private final String mDigit;
    private final Operator mOperator;

    private CalculatorAction(Type type, String digit, Operator operator) {
        mType = type;
        mDigit = digit;
        mOperator = operator;
    }

    @NonNull
    public static CalculatorAction digit(int digit) {
        return new CalculatorAction(Type.DIGIT, String.valueOf(digit), null);
    }

    @NonNull
    public static CalculatorAction decimal() {
        return new CalculatorAction(Type.DECIMAL, null, null);
    }

    @NonNull
    public static CalculatorAction operator(@NonNull Operator operator) {
        return new CalculatorAction(Type.OPERATOR, null, operator);
    }

    @NonNull
    public static CalculatorAction clear() {
        return new CalculatorAction(Type.CLEAR, null, null);
    }

    @NonNull
    public static CalculatorAction delete() {
        return new CalculatorAction(Type.DELETE, null, null);
    }

    @NonNull
    public static CalculatorAction equalsAction() {
        return new CalculatorAction(Type.EQUALS, null, null);
    }

    @NonNull
    public static CalculatorAction precisionDown() {
        return new CalculatorAction(Type.PRECISION_DOWN, null, null);
    }

    @NonNull
    public static CalculatorAction precisionUp() {
        return new CalculatorAction(Type.PRECISION_UP, null, null);
    }

    @NonNull
    public Type getType() {
        return mType;
    }

    @Nullable
    public String getDigit() {
        return mDigit;
    }

    @Nullable
    public Operator getOperator() {
        return mOperator;
    }
}
