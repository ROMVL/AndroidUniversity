package ua.nure.romanikvladislav.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String ARG_ROW = "arg_row";
    private static final String ARG_OLD_ROWS = "arg_old_rows";
    private static final String ARG_RESULT = "arg_result";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String MULTIPLY = "*";
    private static final String DEVIDE = "/";
    private static final String POINT = ".";
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String THREE = "3";
    private static final String FOUR = "4";
    private static final String FIVE = "5";
    private static final String SIX = "6";
    private static final String SEVEN = "7";
    private static final String EIGHT = "8";
    private static final String NINE = "9";
    private static final String ZERO = "0";
    private static final String IS = "=";
    private static final String NEW_ROW = "\n";

    private TextView tvOldCalculations;
    private TextView tvField;
    private TextView tvResult;

    private String lastChar;

    private StringBuilder container = new StringBuilder(ZERO);
    private StringBuilder oldRows = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String row = null;
        String result = ZERO;
        if (savedInstanceState != null) {
            row = savedInstanceState.getString(ARG_ROW);
            String old = savedInstanceState.getString(ARG_OLD_ROWS);
            result = savedInstanceState.getString(ARG_RESULT);
            if (row != null && !row.isEmpty()) {
                container.delete(0, 1);
                container.append(row);
            }
            if (old != null && !old.isEmpty()) {
                oldRows.append(old);
            }
        }

        findViewById(R.id.btnOne).setOnClickListener(this);
        findViewById(R.id.btnTwo).setOnClickListener(this);
        findViewById(R.id.btnThree).setOnClickListener(this);
        findViewById(R.id.btnFour).setOnClickListener(this);
        findViewById(R.id.btnFive).setOnClickListener(this);
        findViewById(R.id.btnSix).setOnClickListener(this);
        findViewById(R.id.btnSeven).setOnClickListener(this);
        findViewById(R.id.btnEight).setOnClickListener(this);
        findViewById(R.id.btnNine).setOnClickListener(this);
        findViewById(R.id.btnZero).setOnClickListener(this);
        findViewById(R.id.btnPoint).setOnClickListener(this);
        findViewById(R.id.btnPlus).setOnClickListener(this);
        findViewById(R.id.btnMinus).setOnClickListener(this);
        findViewById(R.id.btnMultiply).setOnClickListener(this);
        findViewById(R.id.btnSplit).setOnClickListener(this);
        findViewById(R.id.btnIs).setOnClickListener(this);
        findViewById(R.id.btnRemoveLast).setOnClickListener(this);
        findViewById(R.id.btnClearAll).setOnClickListener(this);
        tvField = findViewById(R.id.tvRow);
        tvResult = findViewById(R.id.tvResult);
        tvOldCalculations = findViewById(R.id.tvOldRows);
        tvOldCalculations.setText(oldRows);
        tvField.setText(container.toString());
        lastChar = String.valueOf(container.charAt(container.length() - 1));
        tvResult.setText(result);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(ARG_ROW, container.toString());
        state.putString(ARG_OLD_ROWS, oldRows.toString());
        state.putString(ARG_RESULT, tvResult.getText().toString());
    }

    private void addNewNum(String string) {
        if (container.toString().equals(ZERO)) {
            container.delete(0, 1);
        }
        lastChar = string;
        container.append(string);
        tvField.setText(container.toString());
    }

    private void addPoint() {
        if (!lastChar.equals(POINT) && !lastChar.equals(PLUS) && !lastChar.equals(MINUS) && !lastChar.equals(MULTIPLY) && !lastChar.equals(DEVIDE)) {
            container.append(POINT);
            lastChar = POINT;
            tvField.setText(container.toString());
        }
    }

    private void addNewExpression(String expression) {
        if (container.length() == 1 && (container.toString().equals(ZERO) || container.toString().equals(MINUS))) {
            if (expression.equals(MINUS)) {
                container.delete(0, 1);
                container.append(expression);
                lastChar = expression;
                tvField.setText(container.toString());
                return;
            }
            return;
        }
        if (!lastChar.equals(PLUS) && !lastChar.equals(MINUS) && !lastChar.equals(MULTIPLY) && !lastChar.equals(DEVIDE)) {
            if (lastChar.equals(POINT)) {
                container.append(ZERO);
            }
            lastChar = expression;
            container.append(expression);
        } else {
            switch (lastChar) {
                case PLUS:
                    container.deleteCharAt(container.lastIndexOf(PLUS));
                    break;
                case MINUS:
                    container.deleteCharAt(container.lastIndexOf(MINUS));
                    break;
                case MULTIPLY:
                    container.deleteCharAt(container.lastIndexOf(MULTIPLY));
                    break;
                case DEVIDE:
                    container.deleteCharAt(container.lastIndexOf(DEVIDE));
                    break;
            }
            container.append(expression);
            lastChar = expression;
        }
        tvField.setText(container.toString());
    }

    private void calculate() {
        if (container.toString().equals(ZERO)) {
            tvResult.setText(ZERO);
            return;
        }
        switch (lastChar) {
            case PLUS:
                container.deleteCharAt(container.lastIndexOf(PLUS));
                break;
            case MINUS:
                container.deleteCharAt(container.lastIndexOf(MINUS));
                break;
            case MULTIPLY:
                container.deleteCharAt(container.lastIndexOf(MULTIPLY));
                break;
            case DEVIDE:
                container.deleteCharAt(container.lastIndexOf(DEVIDE));
                break;
            case POINT:
                container.append(ZERO);
                break;
        }
        tvField.setText(container.toString());
        double result = MathUtils.eval(container.toString());
        oldRows.append(container).append(IS).append(result).append(NEW_ROW);
        container.delete(0, container.length());
        container.append(result);
        lastChar = String.valueOf(container.charAt(container.length() - 1));
        tvField.setText(container);
        tvResult.setText(String.valueOf(result));
        tvOldCalculations.setText(oldRows);
    }

    private void removeLastChar() {
        if (!container.toString().isEmpty()) {
            container.deleteCharAt(container.lastIndexOf(lastChar));
            if (!container.toString().isEmpty()) {
                lastChar = String.valueOf(container.charAt(container.length() - 1));
            } else {
                lastChar = ZERO;
                container.append(ZERO);
            }
        }
        tvField.setText(container);
    }

    private void clearAll() {
        oldRows.delete(0, oldRows.length());
        container.delete(0, container.length());
        container.append(ZERO);
        tvResult.setText(ZERO);
        tvOldCalculations.setText(oldRows);
        tvField.setText(container);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btnOne:
                addNewNum(ONE);
                break;
            case R.id.btnTwo:
                addNewNum(TWO);
                break;
            case R.id.btnThree:
                addNewNum(THREE);
                break;
            case R.id.btnFour:
                addNewNum(FOUR);
                break;
            case R.id.btnFive:
                addNewNum(FIVE);
                break;
            case R.id.btnSix:
                addNewNum(SIX);
                break;
            case R.id.btnSeven:
                addNewNum(SEVEN);
                break;
            case R.id.btnEight:
                addNewNum(EIGHT);
                break;
            case R.id.btnNine:
                addNewNum(NINE);
                break;
            case R.id.btnZero:
                addNewNum(ZERO);
                break;
            case R.id.btnPoint:
                addPoint();
                break;
            case R.id.btnPlus:
                addNewExpression(PLUS);
                break;
            case R.id.btnMinus:
                addNewExpression(MINUS);
                break;
            case R.id.btnMultiply:
                addNewExpression(MULTIPLY);
                break;
            case R.id.btnSplit:
                addNewExpression(DEVIDE);
                break;
            case R.id.btnIs:
                calculate();
                break;
            case R.id.btnRemoveLast:
                removeLastChar();
                break;
            case R.id.btnClearAll:
                clearAll();
                break;
        }
    }
}
