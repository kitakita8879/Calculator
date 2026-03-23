package com.example.calculator;

public enum Operator {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("×"),
    DIVIDE("÷");

    private final String mSymbol;

    Operator(String symbol) {
        mSymbol = symbol;
    }

    public String getSymbol() {
        return mSymbol;
    }
}
