package com.example.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalculatorEngineTest {

    @Test
    public void evaluatesSimpleAddition() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.digit(1));
        engine.dispatch(CalculatorAction.operator(Operator.ADD));
        engine.dispatch(CalculatorAction.digit(2));
        CalculatorUiState state = engine.dispatch(CalculatorAction.equalsAction());

        assertEquals("3", state.getDisplayText());
    }

    @Test
    public void respectsOperatorPrecedence() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.digit(2));
        engine.dispatch(CalculatorAction.operator(Operator.ADD));
        engine.dispatch(CalculatorAction.digit(3));
        engine.dispatch(CalculatorAction.operator(Operator.MULTIPLY));
        engine.dispatch(CalculatorAction.digit(4));
        CalculatorUiState state = engine.dispatch(CalculatorAction.equalsAction());

        assertEquals("14", state.getDisplayText());
    }

    @Test
    public void supportsDecimalInputAndPrecisionAdjustments() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.digit(1));
        engine.dispatch(CalculatorAction.operator(Operator.DIVIDE));
        engine.dispatch(CalculatorAction.digit(3));
        engine.dispatch(CalculatorAction.equalsAction());
        CalculatorUiState increased = engine.dispatch(CalculatorAction.precisionUp());

        assertEquals("0.333", increased.getDisplayText());
        assertTrue(increased.canDecreasePrecision());
        assertTrue(increased.canIncreasePrecision());
    }

    @Test
    public void ignoresRepeatedDecimalInput() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.decimal());
        engine.dispatch(CalculatorAction.digit(5));
        CalculatorUiState state = engine.dispatch(CalculatorAction.decimal());

        assertEquals("0.5", state.getDisplayText());
        assertFalse(state.canInputDecimal());
    }

    @Test
    public void replacesTrailingOperator() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.digit(8));
        engine.dispatch(CalculatorAction.operator(Operator.ADD));
        CalculatorUiState state = engine.dispatch(CalculatorAction.operator(Operator.SUBTRACT));

        assertEquals("8 - ", state.getExpressionText());
    }

    @Test
    public void clearsAndStartsANewExpressionAfterEquals() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.digit(1));
        engine.dispatch(CalculatorAction.operator(Operator.ADD));
        engine.dispatch(CalculatorAction.digit(2));
        engine.dispatch(CalculatorAction.equalsAction());
        CalculatorUiState state = engine.dispatch(CalculatorAction.digit(7));

        assertEquals("7", state.getDisplayText());
        assertEquals("7", state.getExpressionText());
    }

    @Test
    public void showsErrorWhenDividingByZero() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.digit(8));
        engine.dispatch(CalculatorAction.operator(Operator.DIVIDE));
        engine.dispatch(CalculatorAction.digit(0));
        CalculatorUiState state = engine.dispatch(CalculatorAction.equalsAction());

        assertEquals("無法除以 0", state.getDisplayText());
        assertTrue(state.isShowingError());
    }

    @Test
    public void deletesLastDigitOrOperator() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.dispatch(CalculatorAction.digit(1));
        engine.dispatch(CalculatorAction.digit(2));
        CalculatorUiState digitDeletedState = engine.dispatch(CalculatorAction.delete());
        assertEquals("1", digitDeletedState.getDisplayText());

        engine.dispatch(CalculatorAction.operator(Operator.ADD));
        CalculatorUiState operatorDeletedState = engine.dispatch(CalculatorAction.delete());
        assertEquals("1", operatorDeletedState.getExpressionText());
    }
}
