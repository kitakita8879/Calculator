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
    public CalculatorUiState applyAction(@NonNull CalculatorAction action) {
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
                    inputOperator(operator);
                }
                break;
            case CLEAR:
                clear();
                break;
            case DELETE:
                delete();
                break;
            case EQUALS:
                evaluateExpression();
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

        prepareForFreshInput();
        mCurrentInput += digit;
        mHasEvaluatedResult = false;
        refreshExpressionText();
    }

    private void appendDecimal() {
        prepareForFreshInput();
        if (mCurrentInput.contains(".")) {
            return;
        }

        if (mCurrentInput.isEmpty()) {
            mCurrentInput = "0.";
        } else {
            mCurrentInput += ".";
        }

        mHasEvaluatedResult = false;
        refreshExpressionText();
    }

    private void inputOperator(@NonNull Operator operator) {
        if (mHasError) {
            return;
        }

        addReadyResultToOperands();

        if (!addCurrentToOperands()) {
            if (mOperators.isEmpty() && mOperands.isEmpty()) {
                return;
            }
            if (endsWithOperator()) {
                mOperators.set(mOperators.size() - 1, operator);
                refreshExpressionText();
                return;
            }
            mOperators.add(operator);
            refreshExpressionText();
            return;
        }

        mOperators.add(operator);
        refreshExpressionText();
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
            refreshExpressionText();
            return;
        }

        if (endsWithOperator()) {
            mOperators.remove(mOperators.size() - 1);
            BigDecimal restoredOperand = mOperands.remove(mOperands.size() - 1);
            mCurrentInput = stripTrailingZeros(restoredOperand);
            refreshExpressionText();
        }
    }

    private void evaluateExpression() {
        if (!canEvaluate()) {
            return;
        }

        addCurrentToOperands();
        removeExtraOperators();

        if (mOperands.isEmpty()) {
            return;
        }

        String evaluatedExpression = mExpressionText;

        try {
            BigDecimal result = computeWithPrecedence();
            applyEvaluationResult(result, evaluatedExpression);
        } catch (ArithmeticException exception) {
            enterErrorState();
        }
    }

    private BigDecimal computeWithPrecedence() {
        List<BigDecimal> collapsedOperands = new ArrayList<>();
        List<Operator> collapsedOperators = new ArrayList<>();
        collapsedOperands.add(mOperands.get(0));

        for (int index = 0; index < mOperators.size(); index++) {
            Operator operator = mOperators.get(index);
            BigDecimal nextOperand = mOperands.get(index + 1);
            if (operator == Operator.MULTIPLY || operator == Operator.DIVIDE) {
                BigDecimal leftOperand = collapsedOperands.remove(
                        collapsedOperands.size() - 1);
                collapsedOperands.add(computeBinary(leftOperand, nextOperand, operator));
            } else {
                collapsedOperators.add(operator);
                collapsedOperands.add(nextOperand);
            }
        }

        BigDecimal result = collapsedOperands.get(0);
        for (int index = 0; index < collapsedOperators.size(); index++) {
            result = computeBinary(result,
                    collapsedOperands.get(index + 1), collapsedOperators.get(index));
        }

        return result;
    }

    private BigDecimal computeBinary(BigDecimal left, BigDecimal right,
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

    private void prepareForFreshInput() {
        if (mHasError || hasReadyResult()) {
            clear();
        }
    }

    private boolean hasReadyResult() {
        return mHasEvaluatedResult && mOperands.isEmpty()
                && mOperators.isEmpty() && mCurrentInput.isEmpty();
    }

    private boolean addCurrentToOperands() {
        if (mCurrentInput.isEmpty()) {
            return false;
        }

        mOperands.add(new BigDecimal(mCurrentInput));
        mCurrentInput = "";
        return true;
    }

    private void addReadyResultToOperands() {
        if (!hasReadyResult()) {
            return;
        }

        mOperands.add(mLastResult);
        mHasEvaluatedResult = false;
    }

    private boolean endsWithOperator() {
        return !mOperators.isEmpty() && mOperands.size() == mOperators.size();
    }

    private boolean canEvaluate() {
        return !mHasError && (!mCurrentInput.isEmpty() || !mOperands.isEmpty());
    }

    private void removeExtraOperators() {
        while (mOperators.size() >= mOperands.size() && !mOperators.isEmpty()) {
            mOperators.remove(mOperators.size() - 1);
            mExpressionText = trimTrailingOperatorText(mExpressionText);
        }
    }

    private void applyEvaluationResult(@NonNull BigDecimal result,
                                       @NonNull String evaluatedExpression) {
        mOperands.clear();
        mOperators.clear();
        mLastResult = result;
        mResultScale = DEFAULT_RESULT_SCALE;
        mHasEvaluatedResult = true;
        mExpressionText = evaluatedExpression + " =";
    }

    private void enterErrorState() {
        mOperands.clear();
        mOperators.clear();
        mCurrentInput = "";
        mHasError = true;
    }

    @NonNull
    private String trimTrailingOperatorText(String source) {
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
    private String buildExpressionText() {
        if (mOperands.isEmpty() && mOperators.isEmpty() && mCurrentInput.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < mOperands.size(); index++) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(stripTrailingZeros(mOperands.get(index)));

            if (index < mOperators.size()) {
                builder.append(' ')
                        .append(mOperators.get(index).getSymbol())
                        .append(' ');
            }
        }

        if (!mCurrentInput.isEmpty()) {
            if (builder.length() > 0 && builder.charAt(builder.length() - 1) != ' ') {
                builder.append(' ');
            }
            builder.append(mCurrentInput);
        }

        return builder.toString();
    }

    private void refreshExpressionText() {
        mExpressionText = buildExpressionText();
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
