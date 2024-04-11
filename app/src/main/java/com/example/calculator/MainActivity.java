package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView txtAns;
    private TextView txtTmp;

    private Double ansNum = 0.0;
    private Double ansView = 0.0;
    private final Stack<String> formula = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindView();
    }

    private void FindView(){
        txtAns = findViewById(R.id.ans);
        txtTmp = findViewById(R.id.tmp);
        Button btn0 = findViewById(R.id.btn_0);
        Button btn1 = findViewById(R.id.btn_1);
        Button btn2 = findViewById(R.id.btn_2);
        Button btn3 = findViewById(R.id.btn_3);
        Button btn4 = findViewById(R.id.btn_4);
        Button btn5 = findViewById(R.id.btn_5);
        Button btn6 = findViewById(R.id.btn_6);
        Button btn7 = findViewById(R.id.btn_7);
        Button btn8 = findViewById(R.id.btn_8);
        Button btn9 = findViewById(R.id.btn_9);
        Button add = findViewById(R.id.add);
        Button sub = findViewById(R.id.sub);
        Button mul = findViewById(R.id.mul);
        Button div = findViewById(R.id.div);
        Button dot = findViewById(R.id.dot);
        Button clear = findViewById(R.id.clear);
        Button lArrow = findViewById(R.id.left_arr);
        Button rArrow = findViewById(R.id.right_arr);
        //Button del = findViewById(R.id.Del);//
        Button equ = findViewById(R.id.equ);

        btn0.setOnClickListener(clickListen);
        btn1.setOnClickListener(clickListen);
        btn2.setOnClickListener(clickListen);
        btn3.setOnClickListener(clickListen);
        btn4.setOnClickListener(clickListen);
        btn5.setOnClickListener(clickListen);
        btn6.setOnClickListener(clickListen);
        btn7.setOnClickListener(clickListen);
        btn8.setOnClickListener(clickListen);
        btn9.setOnClickListener(clickListen);
        add.setOnClickListener(clickListen);
        sub.setOnClickListener(clickListen);
        mul.setOnClickListener(clickListen);
        div.setOnClickListener(clickListen);
        clear.setOnClickListener(clickListen);
        dot.setOnClickListener(clickListen);
        equ.setOnClickListener(clickListen);
        lArrow.setOnClickListener(clickListen);
        rArrow.setOnClickListener(clickListen);
    }

    private final Button.OnClickListener clickListen = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            Button b = (Button)v;

            double num1;
            double num2;
            String tmpNum, tmpAns;
            //switch(v.getId()){} 從ADT14開始不支援，資源id非final，參考https://web.archive.org/web/20230203152426/http://tools.android.com/tips/non-constant-fields
            int id = v.getId();
            if (id == R.id.clear) {
                txtAns.setText("");
                txtTmp.setText("");
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
                }
                txtTmp.setText("");
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
        }
    };
}
