package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public final class CalculatorViewModel extends ViewModel {
    private final CalculatorEngine mEngine = new CalculatorEngine();

    private final ObservableField<String> mDisplayText = new ObservableField<>("");
    private final ObservableField<String> mExpressionText = new ObservableField<>("");
    private final ObservableBoolean mCanInputDecimal = new ObservableBoolean(true);
    private final ObservableBoolean mCanDecreasePrecision = new ObservableBoolean(false);
    private final ObservableBoolean mCanIncreasePrecision = new ObservableBoolean(false);

    public CalculatorViewModel() {
        syncState(mEngine.getUiState());
    }

    public void onDigitClick(int digit) {
        syncState(mEngine.applyAction(CalculatorAction.digit(digit)));
    }

    public void onDecimalClick() {
        syncState(mEngine.applyAction(CalculatorAction.decimal()));
    }

    public void onOperatorClick(@NonNull Operator operator) {
        syncState(mEngine.applyAction(CalculatorAction.operator(operator)));
    }

    public void onClearClick() {
        syncState(mEngine.applyAction(CalculatorAction.clear()));
    }

    public void onDeleteClick() {
        syncState(mEngine.applyAction(CalculatorAction.delete()));
    }

    public void onEqualsClick() {
        syncState(mEngine.applyAction(CalculatorAction.equalsAction()));
    }

    public void onPrecisionDownClick() {
        syncState(mEngine.applyAction(CalculatorAction.precisionDown()));
    }

    public void onPrecisionUpClick() {
        syncState(mEngine.applyAction(CalculatorAction.precisionUp()));
    }

    @NonNull
    public ObservableField<String> getDisplayText() {
        return mDisplayText;
    }

    @NonNull
    public ObservableField<String> getExpressionText() {
        return mExpressionText;
    }

    @NonNull
    public ObservableBoolean getCanInputDecimal() {
        return mCanInputDecimal;
    }

    @NonNull
    public ObservableBoolean getCanDecreasePrecision() {
        return mCanDecreasePrecision;
    }

    @NonNull
    public ObservableBoolean getCanIncreasePrecision() {
        return mCanIncreasePrecision;
    }

    private void syncState(@NonNull CalculatorUiState state) {
        mDisplayText.set(state.getDisplayText());
        mExpressionText.set(state.getExpressionText());
        mCanInputDecimal.set(state.canInputDecimal());
        mCanDecreasePrecision.set(state.canDecreasePrecision());
        mCanIncreasePrecision.set(state.canIncreasePrecision());
    }
}
