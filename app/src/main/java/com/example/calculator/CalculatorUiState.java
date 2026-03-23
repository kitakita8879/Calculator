package com.example.calculator;

import androidx.annotation.NonNull;

public final class CalculatorUiState {
    private final String mExpressionText;
    private final String mDisplayText;
    private final boolean mCanInputDecimal;
    private final boolean mCanDecreasePrecision;
    private final boolean mCanIncreasePrecision;
    private final boolean mShowingError;

    public CalculatorUiState(
            @NonNull String expressionText,
            @NonNull String displayText,
            boolean canInputDecimal,
            boolean canDecreasePrecision,
            boolean canIncreasePrecision,
            boolean showingError
    ) {
        mExpressionText = expressionText;
        mDisplayText = displayText;
        mCanInputDecimal = canInputDecimal;
        mCanDecreasePrecision = canDecreasePrecision;
        mCanIncreasePrecision = canIncreasePrecision;
        mShowingError = showingError;
    }

    @NonNull
    public String getExpressionText() {
        return mExpressionText;
    }

    @NonNull
    public String getDisplayText() {
        return mDisplayText;
    }

    public boolean canInputDecimal() {
        return mCanInputDecimal;
    }

    public boolean canDecreasePrecision() {
        return mCanDecreasePrecision;
    }

    public boolean canIncreasePrecision() {
        return mCanIncreasePrecision;
    }

    public boolean isShowingError() {
        return mShowingError;
    }
}
