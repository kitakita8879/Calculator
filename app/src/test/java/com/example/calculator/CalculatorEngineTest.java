package com.example.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalculatorEngineTest {

    @Test
    public void evaluatesSimpleAddition() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(1));
        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        engine.applyAction(CalculatorAction.digit(2));
        CalculatorUiState state = engine.applyAction(CalculatorAction.equalsAction());

        assertEquals("3", state.getDisplayText());
    }

    @Test
    public void respectsOperatorPrecedence() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(2));
        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        engine.applyAction(CalculatorAction.digit(3));
        engine.applyAction(CalculatorAction.operator(Operator.MULTIPLY));
        engine.applyAction(CalculatorAction.digit(4));
        CalculatorUiState state = engine.applyAction(CalculatorAction.equalsAction());

        assertEquals("14", state.getDisplayText());
    }

    @Test
    public void supportsDecimalInputAndPrecisionAdjustments() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(1));
        engine.applyAction(CalculatorAction.operator(Operator.DIVIDE));
        engine.applyAction(CalculatorAction.digit(3));
        engine.applyAction(CalculatorAction.equalsAction());
        CalculatorUiState increased = engine.applyAction(CalculatorAction.precisionUp());

        assertEquals("0.333", increased.getDisplayText());
        assertTrue(increased.canDecreasePrecision());
        assertTrue(increased.canIncreasePrecision());
    }

    @Test
    public void ignoresRepeatedDecimalInput() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.decimal());
        engine.applyAction(CalculatorAction.digit(5));
        CalculatorUiState state = engine.applyAction(CalculatorAction.decimal());

        assertEquals("0.5", state.getDisplayText());
        assertFalse(state.canInputDecimal());
    }

    @Test
    public void replacesTrailingOperator() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(8));
        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        CalculatorUiState state = engine.applyAction(CalculatorAction.operator(Operator.SUBTRACT));

        assertEquals("8 - ", state.getExpressionText());
    }

    @Test
    public void clearsAndStartsANewExpressionAfterEquals() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(1));
        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        engine.applyAction(CalculatorAction.digit(2));
        engine.applyAction(CalculatorAction.equalsAction());
        CalculatorUiState state = engine.applyAction(CalculatorAction.digit(7));

        assertEquals("7", state.getDisplayText());
        assertEquals("7", state.getExpressionText());
    }

    @Test
    public void continuesUsingReadyResultWhenOperatorIsTappedAfterEquals() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(1));
        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        engine.applyAction(CalculatorAction.digit(2));
        engine.applyAction(CalculatorAction.equalsAction());
        CalculatorUiState state = engine.applyAction(CalculatorAction.operator(Operator.MULTIPLY));

        assertEquals("3", state.getDisplayText());
        assertEquals("3 × ", state.getExpressionText());
    }

    @Test
    public void showsErrorWhenDividingByZero() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(8));
        engine.applyAction(CalculatorAction.operator(Operator.DIVIDE));
        engine.applyAction(CalculatorAction.digit(0));
        CalculatorUiState state = engine.applyAction(CalculatorAction.equalsAction());

        assertEquals("無法除以 0", state.getDisplayText());
        assertTrue(state.isShowingError());
    }

    @Test
    public void resetsErrorStateWhenDigitIsEntered() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(8));
        engine.applyAction(CalculatorAction.operator(Operator.DIVIDE));
        engine.applyAction(CalculatorAction.digit(0));
        engine.applyAction(CalculatorAction.equalsAction());
        CalculatorUiState state = engine.applyAction(CalculatorAction.digit(7));

        assertEquals("7", state.getDisplayText());
        assertEquals("7", state.getExpressionText());
        assertFalse(state.isShowingError());
    }

    @Test
    public void deletesLastDigitOrOperator() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(1));
        engine.applyAction(CalculatorAction.digit(2));
        CalculatorUiState digitDeletedState = engine.applyAction(CalculatorAction.delete());
        assertEquals("1", digitDeletedState.getDisplayText());

        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        CalculatorUiState operatorDeletedState = engine.applyAction(CalculatorAction.delete());
        assertEquals("1", operatorDeletedState.getExpressionText());
    }

    @Test
    public void restoresFullExpressionWhenDeletingTrailingOperator() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(6));
        engine.applyAction(CalculatorAction.digit(6));
        engine.applyAction(CalculatorAction.digit(6));
        engine.applyAction(CalculatorAction.digit(6));
        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        engine.applyAction(CalculatorAction.digit(9));
        engine.applyAction(CalculatorAction.digit(9));
        engine.applyAction(CalculatorAction.digit(9));
        engine.applyAction(CalculatorAction.digit(9));
        engine.applyAction(CalculatorAction.decimal());
        engine.applyAction(CalculatorAction.digit(8));
        engine.applyAction(CalculatorAction.digit(8));
        engine.applyAction(CalculatorAction.digit(8));
        engine.applyAction(CalculatorAction.operator(Operator.MULTIPLY));
        engine.applyAction(CalculatorAction.digit(7));
        engine.applyAction(CalculatorAction.digit(7));
        engine.applyAction(CalculatorAction.digit(7));

        engine.applyAction(CalculatorAction.delete());
        engine.applyAction(CalculatorAction.delete());
        engine.applyAction(CalculatorAction.delete());

        CalculatorUiState state = engine.applyAction(CalculatorAction.delete());

        assertEquals("9999.888", state.getDisplayText());
        assertEquals("6666 + 9999.888", state.getExpressionText());
    }

    @Test
    public void keepsTrailingOperatorVisibleAfterReplacingIt() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.applyAction(CalculatorAction.digit(1));
        engine.applyAction(CalculatorAction.operator(Operator.ADD));
        CalculatorUiState state = engine.applyAction(CalculatorAction.operator(Operator.MULTIPLY));

        assertEquals("1 × ", state.getExpressionText());
    }
}
