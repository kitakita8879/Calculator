package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView txtAns;
    private TextView txtTmp;

    private Double ansNum = 0.0;
    private Double ansView = 0.0;
    private final Stack<String> formula = new Stack<>();
    private boolean isDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
    }

    private void findView() {
        txtAns = findViewById(R.id.ans);
        txtTmp = findViewById(R.id.tmp);
        TextView txt0 = findViewById(R.id.btn_0);
        TextView txt1 = findViewById(R.id.btn_1);
        TextView txt2 = findViewById(R.id.btn_2);
        TextView txt3 = findViewById(R.id.btn_3);
        TextView txt4 = findViewById(R.id.btn_4);
        TextView txt5 = findViewById(R.id.btn_5);
        TextView txt6 = findViewById(R.id.btn_6);
        TextView txt7 = findViewById(R.id.btn_7);
        TextView txt8 = findViewById(R.id.btn_8);
        TextView txt9 = findViewById(R.id.btn_9);
        TextView txtAdd = findViewById(R.id.add);
        TextView txtSub = findViewById(R.id.sub);
        TextView txtMul = findViewById(R.id.mul);
        TextView txtDiv = findViewById(R.id.div);
        TextView txtDot = findViewById(R.id.dot);
        TextView txtClear = findViewById(R.id.clear);
        TextView txtLeftArrow = findViewById(R.id.left_arr);
        TextView txtRightArrow = findViewById(R.id.right_arr);
        TextView txtEqu = findViewById(R.id.equ);

        txt0.setOnClickListener(mClickListener);
        txt1.setOnClickListener(mClickListener);
        txt2.setOnClickListener(mClickListener);
        txt3.setOnClickListener(mClickListener);
        txt4.setOnClickListener(mClickListener);
        txt5.setOnClickListener(mClickListener);
        txt6.setOnClickListener(mClickListener);
        txt7.setOnClickListener(mClickListener);
        txt8.setOnClickListener(mClickListener);
        txt9.setOnClickListener(mClickListener);
        txtAdd.setOnClickListener(mClickListener);
        txtSub.setOnClickListener(mClickListener);
        txtMul.setOnClickListener(mClickListener);
        txtDiv.setOnClickListener(mClickListener);
        txtClear.setOnClickListener(mClickListener);
        txtDot.setOnClickListener(mClickListener);
        txtEqu.setOnClickListener(mClickListener);
        txtLeftArrow.setOnClickListener(mClickListener);
        txtRightArrow.setOnClickListener(mClickListener);
    }

    private final View.OnClickListener mClickListener = v -> {
        TextView b = (TextView) v;

        double num1;
        double num2;
        String tmpNum, tmpAns;
        //switch(v.getId()){} 從ADT14開始不支援，資源id非final，參考https://web.archive.org/web/20230203152426/http://tools.android.com/tips/non-constant-fields
        int id = v.getId();
        if (id == R.id.clear) {
            txtAns.setText("");
            txtTmp.setText("");
            isDot = false;
            while (!formula.isEmpty()) {
                formula.pop();
            }
        } else if (id == R.id.add) {
            tmpAns = txtAns.getText().toString() + b.getText().toString();
            txtAns.setText(tmpAns);
            tmpNum = txtTmp.getText().toString();
            if (!tmpNum.isEmpty()) {
                if (formula.isEmpty()) {
                    formula.push(tmpNum);
                } else if (formula.peek().equals("Mul")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 * num2;
                    formula.push(Double.toString(num2));
                } else if (formula.peek().equals("Div")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 / num2;
                    formula.push(Double.toString(num2));
                } else {
                    formula.push(tmpNum);
                }
                formula.push("Add");
            }
            txtTmp.setText("");
            isDot = false;
        } else if (id == R.id.sub) {
            tmpAns = txtAns.getText().toString() + b.getText().toString();
            txtAns.setText(tmpAns);
            tmpNum = txtTmp.getText().toString();
            if (!tmpNum.isEmpty()) {
                if (formula.isEmpty()) {
                    formula.push(tmpNum);
                } else if (formula.peek().equals("Mul")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 * num2;
                    formula.push(Double.toString(num2));
                } else if (formula.peek().equals("Div")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 / num2;
                    formula.push(Double.toString(num2));
                } else {
                    formula.push(tmpNum);
                }
                formula.push("Sub");
            }
            txtTmp.setText("");
            isDot = false;
        } else if (id == R.id.mul) {
            tmpAns = txtAns.getText().toString() + b.getText().toString();
            txtAns.setText(tmpAns);
            tmpNum = txtTmp.getText().toString();
            if (!tmpNum.isEmpty()) {
                if (formula.isEmpty()) {
                    formula.push(tmpNum);
                } else if (formula.peek().equals("Mul")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 * num2;
                    formula.push(Double.toString(num2));
                } else if (formula.peek().equals("Div")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 / num2;
                    formula.push(Double.toString(num2));
                } else {
                    formula.push(tmpNum);
                }
                formula.push("Mul");
            }
            txtTmp.setText("");
            isDot = false;
        } else if (id == R.id.div) {
            tmpAns = txtAns.getText().toString() + b.getText().toString();
            txtAns.setText(tmpAns);
            tmpNum = txtTmp.getText().toString();
            if (!tmpNum.isEmpty()) {
                if (formula.isEmpty()) {
                    formula.push(tmpNum);
                } else if (formula.peek().equals("Mul")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 * num2;
                    formula.push(Double.toString(num2));
                } else if (formula.peek().equals("Div")) {
                    num2 = Double.parseDouble(tmpNum);
                    formula.pop();
                    num1 = Double.parseDouble(formula.pop());
                    num2 = num1 / num2;
                    formula.push(Double.toString(num2));
                } else {
                    formula.push(tmpNum);
                }
                formula.push("Div");
            }
            txtTmp.setText("");
            isDot = false;
        } else if (id == R.id.equ) {
            tmpNum = txtTmp.getText().toString();
            if (!tmpNum.isEmpty()) {
                formula.push(tmpNum);
                num2 = Double.parseDouble(formula.pop());
                while (!formula.isEmpty()) {
                    txtTmp.setText(String.valueOf(num2));
                    if (formula.peek().equals("Mul")) {
                        formula.pop();
                        num1 = Double.parseDouble(formula.pop());
                        num2 = num1 * num2;
                        formula.push(Double.toString(num2));
                    } else if (formula.peek().equals("Div")) {
                        formula.pop();
                        num1 = Double.parseDouble(formula.pop());
                        num2 = num1 / num2;
                        formula.push(Double.toString(num2));
                    } else if (formula.peek().equals("Add")) {
                        formula.pop();
                        num1 = Double.parseDouble(formula.pop());
                        num2 = num1 + num2;
                        formula.push(Double.toString(num2));
                    } else if (formula.peek().equals("Sub")) {
                        formula.pop();
                        num1 = Double.parseDouble(formula.pop());
                        num2 = num1 - num2;
                        formula.push(Double.toString(num2));
                    }
                    num2 = Double.parseDouble(formula.pop());
                }
                ansNum = Math.round(num2 * 10000) * 0.0001;
                ansView = Math.round(ansNum * 100) * 0.01;
                txtAns.setText(String.format(ansView.toString()));
                isDot = false;
            }
            txtTmp.setText("");
            isDot = false;
        } else if (id == R.id.left_arr) {
            int i = ansView.toString().length() - ansView.toString().indexOf('.') - 1;
            if (i == 2) {
                ansView = Math.round(ansNum * 10) * 0.1;
            } else if (i == 3) {
                ansView = Math.round(ansNum * 100) * 0.01;
            } else if (i == 4) {
                ansView = Math.round(ansNum * 1000) * 0.001;
            } else if (i == 1) {
                ansView = (double) Math.round(ansNum);
            }
            txtAns.setText(String.format(ansView.toString()));
        } else if (id == R.id.right_arr) {
            int j = ansView.toString().length() - ansView.toString().indexOf('.') - 1;
            if (j == 2) {
                ansView = Math.round(ansNum * 1000) * 0.001;
            } else if (j == 1) {
                ansView = Math.round(ansNum * 100) * 0.01;
            } else if (j == 0) {
                ansView = Math.round(ansNum * 10) * 0.1;
            } else if (j == 3) {
                ansView = Math.round(ansNum * 10000) * 0.0001;
            }
            txtAns.setText(String.format(ansView.toString()));
        } else if (id == R.id.dot) {
            if (!isDot) {
                if (txtTmp.getText().toString().isEmpty()) {
                    txtTmp.setText(("0."));
                    tmpAns = txtAns.getText().toString() + txtTmp.getText().toString();
                    txtAns.setText(tmpAns);
                } else {
                    tmpNum = txtTmp.getText() + b.getText().toString();
                    txtTmp.setText((tmpNum));
                    tmpAns = txtAns.getText().toString() + b.getText().toString();
                    txtAns.setText(tmpAns);
                }
            }
            isDot = true;
        } else {
            if (txtTmp.getText().toString().isEmpty()) {
                txtTmp.setText(b.getText().toString());
            } else {
                tmpNum = txtTmp.getText() + b.getText().toString();
                txtTmp.setText(tmpNum);
            }
            tmpAns = txtAns.getText().toString() + b.getText().toString();
            txtAns.setText(tmpAns);
        }
    };
}
