package com.example.calculator;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class CalculatorEngine {
    private static final int DEFAULT_RESULT_SCALE = 2;
    private static final int MAX_RESULT_SCALE = 4;
    private static final int MIN_RESULT_SCALE = 0;
    private static final String READY_TEXT = "0";
    private static final String DIVIDE_BY_ZERO_MESSAGE = "無法除以 0";

    private final List<BigDecimal> mOperands = new ArrayList<>();
    private final List<Operator> mOperators = new ArrayList<>();
    private String mCurrentInput = "";
    private String mExpressionText = "";
    private BigDecimal mLastResult = BigDecimal.ZERO;
    private boolean mHasEvaluatedResult = false;
    private boolean mHasError = false;
    private int mResultScale = DEFAULT_RESULT_SCALE;

    @NonNull
    public CalculatorUiState dispatch(@NonNull CalculatorAction action) {
        switch (action.getType()) {
            case DIGIT:
                String digit = action.getDigit();
                if (digit != null) {
                    appendDigit(digit);
                }
                break;
            case DECIMAL:
                appendDecimal();
                break;
            case OPERATOR:
                Operator operator = action.getOperator();
                if (operator != null) {
                    applyOperator(operator);
                }
                break;
            case CLEAR:
                clear();
                break;
            case DELETE:
                delete();
                break;
            case EQUALS:
                evaluate();
                break;
            case PRECISION_DOWN:
                adjustPrecision(false);
                break;
            case PRECISION_UP:
                adjustPrecision(true);
                break;
        }
        return getUiState();
    }

    @NonNull
    public CalculatorUiState getUiState() {
        String visibleExpression = mExpressionText.isEmpty() ? " " : mExpressionText;
        String displayText;
        if (mHasError) {
            displayText = DIVIDE_BY_ZERO_MESSAGE;
        } else if (!mCurrentInput.isEmpty()) {
            displayText = mCurrentInput;
        } else if (mHasEvaluatedResult) {
            displayText = formatResult(mLastResult, mResultScale);
        } else if (!mOperands.isEmpty()) {
            displayText = stripTrailingZeros(mOperands.get(mOperands.size() - 1));
        } else {
            displayText = READY_TEXT;
        }

        return new CalculatorUiState(
                visibleExpression,
                displayText,
                !mCurrentInput.contains("."),
                mHasEvaluatedResult && mResultScale > MIN_RESULT_SCALE,
                mHasEvaluatedResult && mResultScale < MAX_RESULT_SCALE,
                mHasError
        );
    }

    private void clear() {
        mOperands.clear();
        mOperators.clear();
        mCurrentInput = "";
        mExpressionText = "";
        mHasEvaluatedResult = false;
        mHasError = false;
        mResultScale = DEFAULT_RESULT_SCALE;
        mLastResult = BigDecimal.ZERO;
    }

    private void appendDigit(@NonNull String digit) {
        if (digit.isEmpty()) {
            return;
        }

        if (mHasError || shouldStartNewExpression()) {
            clear();
        }

        mCurrentInput += digit;
        mExpressionText += digit;
        mHasEvaluatedResult = false;
    }

    private void appendDecimal() {
        if (mHasError || shouldStartNewExpression()) {
            clear();
        }

        if (mCurrentInput.contains(".")) {
            return;
        }

        if (mCurrentInput.isEmpty()) {
            mCurrentInput = "0.";
            mExpressionText += "0.";
        } else {
            mCurrentInput += ".";
            mExpressionText += ".";
        }

        mHasEvaluatedResult = false;
    }

    private void applyOperator(@NonNull Operator operator) {
        if (mHasError) {
            return;
        }

        if (shouldUseLastResultAsOperand()) {
            mOperands.add(mLastResult);
            mExpressionText = stripTrailingZeros(mLastResult);
            mHasEvaluatedResult = false;
        }

        if (mCurrentInput.isEmpty()) {
            if (mOperators.isEmpty() && mOperands.isEmpty()) {
                return;
            }
            if (!mOperators.isEmpty() && mOperands.size() == mOperators.size()) {
                mOperators.set(mOperators.size() - 1, operator);
                mExpressionText = replaceTrailingOperator(mExpressionText, operator.getSymbol());
            }
            return;
        }

        mOperands.add(new BigDecimal(mCurrentInput));
        mCurrentInput = "";
        mOperators.add(operator);
        mExpressionText += " " + operator.getSymbol() + " ";
    }

    private void delete() {
        if (mHasError) {
            clear();
            return;
        }

        if (mHasEvaluatedResult) {
            clear();
            return;
        }

        if (!mCurrentInput.isEmpty()) {
            mCurrentInput = mCurrentInput.substring(0, mCurrentInput.length() - 1);
            mExpressionText = trimLastCharacter(mExpressionText);
            return;
        }

        if (!mOperators.isEmpty() && mOperands.size() == mOperators.size()) {
            mOperators.remove(mOperators.size() - 1);
            BigDecimal restoredOperand = mOperands.remove(mOperands.size() - 1);
            mCurrentInput = stripTrailingZeros(restoredOperand);
            mExpressionText = mCurrentInput;
        }
    }

    private void evaluate() {
        if (mHasError || (mCurrentInput.isEmpty() && mOperands.isEmpty())) {
            return;
        }

        if (!mCurrentInput.isEmpty()) {
            mOperands.add(new BigDecimal(mCurrentInput));
            mCurrentInput = "";
        }

        while (mOperators.size() >= mOperands.size() && !mOperators.isEmpty()) {
            mOperators.remove(mOperators.size() - 1);
            mExpressionText = trimTrailingOperator(mExpressionText);
        }

        if (mOperands.isEmpty()) {
            return;
        }

        String evaluatedExpression = mExpressionText;

        try {
            BigDecimal result = evaluateWithPrecedence();
            mOperands.clear();
            mOperators.clear();
            mLastResult = result;
            mResultScale = DEFAULT_RESULT_SCALE;
            mHasEvaluatedResult = true;
            mExpressionText = evaluatedExpression + " =";
        } catch (ArithmeticException exception) {
            mOperands.clear();
            mOperators.clear();
            mCurrentInput = "";
            mHasError = true;
        }
    }

    private BigDecimal evaluateWithPrecedence() {
        List<BigDecimal> collapsedOperands = new ArrayList<>();
        List<Operator> collapsedOperators = new ArrayList<>();
        collapsedOperands.add(mOperands.get(0));

        for (int index = 0; index < mOperators.size(); index++) {
            Operator operator = mOperators.get(index);
            BigDecimal nextOperand = mOperands.get(index + 1);
            if (operator == Operator.MULTIPLY || operator == Operator.DIVIDE) {
                BigDecimal leftOperand = collapsedOperands.remove(
                        collapsedOperands.size() - 1);
                collapsedOperands.add(applyBinaryOperator(leftOperand, nextOperand, operator));
            } else {
                collapsedOperators.add(operator);
                collapsedOperands.add(nextOperand);
            }
        }

        BigDecimal result = collapsedOperands.get(0);
        for (int index = 0; index < collapsedOperators.size(); index++) {
            result = applyBinaryOperator(result,
                    collapsedOperands.get(index + 1), collapsedOperators.get(index));
        }

        return result;
    }

    private BigDecimal applyBinaryOperator(BigDecimal left, BigDecimal right,
                                           @NonNull Operator operator) {
        return switch (operator) {
            case ADD -> left.add(right);
            case SUBTRACT -> left.subtract(right);
            case MULTIPLY -> left.multiply(right);
            case DIVIDE -> {
                if (right.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException(DIVIDE_BY_ZERO_MESSAGE);
                }
                yield left.divide(right, MAX_RESULT_SCALE + 4, RoundingMode.HALF_UP);
            }
        };
    }

    private void adjustPrecision(boolean increase) {
        if (!mHasEvaluatedResult || mHasError) {
            return;
        }

        if (increase && mResultScale < MAX_RESULT_SCALE) {
            mResultScale++;
        } else if (!increase && mResultScale > MIN_RESULT_SCALE) {
            mResultScale--;
        }
    }

    private boolean shouldStartNewExpression() {
        return mHasEvaluatedResult && mOperands.isEmpty()
                && mOperators.isEmpty() && mCurrentInput.isEmpty();
    }

    private boolean shouldUseLastResultAsOperand() {
        return mHasEvaluatedResult && mOperands.isEmpty()
                && mOperators.isEmpty() && mCurrentInput.isEmpty();
    }

    @NonNull
    private String replaceTrailingOperator(String source, @NonNull String operatorSymbol) {
        String trimmed = trimTrailingOperator(source);
        return trimmed + " " + operatorSymbol + " ";
    }

    @NonNull
    private String trimTrailingOperator(String source) {
        String trimmed = source == null ? "" : source.trim();
        if (trimmed.endsWith("=")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
        }
        int lastSpace = trimmed.lastIndexOf(' ');
        if (lastSpace < 0) {
            return trimmed;
        }
        return trimmed.substring(0, lastSpace).trim();
    }

    @NonNull
    private String trimLastCharacter(String source) {
        if (source == null || source.isEmpty()) {
            return "";
        }
        return source.substring(0, source.length() - 1);
    }

    private String stripTrailingZeros(@NonNull BigDecimal value) {
        BigDecimal normalized = value.stripTrailingZeros();
        if (normalized.scale() < 0) {
            normalized = normalized.setScale(0, RoundingMode.HALF_UP);
        }
        return normalized.toPlainString();
    }

    private String formatResult(@NonNull BigDecimal value, int scale) {
        BigDecimal scaled = value.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
        if (scaled.scale() < 0) {
            scaled = scaled.setScale(0, RoundingMode.HALF_UP);
        }
        return scaled.toPlainString();
    }
}
