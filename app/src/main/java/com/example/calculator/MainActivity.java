package com.example.calculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);
        CalculatorViewModel viewModel = new ViewModelProvider(this)
                .get(CalculatorViewModel.class);

        binding.setViewModel(viewModel);
    }
}
