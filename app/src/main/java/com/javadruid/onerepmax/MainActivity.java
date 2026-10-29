package com.javadruid.onerepmax;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight;
    private EditText etReps;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        etWeight = findViewById(R.id.etWeight);
        etReps = findViewById(R.id.etReps);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);

        // Set click listener for the calculate button
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateOneRepMax();
            }
        });
    }

    private void calculateOneRepMax() {
        String weightStr = etWeight.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();

        // Validate inputs are not empty
        if (weightStr.isEmpty() || repsStr.isEmpty()) {
            Toast.makeText(this, "Please enter both weight and reps", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            int reps = Integer.parseInt(repsStr);

            if (weight <= 0 || reps <= 0) {
                tvResult.setText("--");
                return;
            }

            // Epley Formula: Weight * (1 + Reps / 30.0)
            final double oneRepMax = weight * (1.0 + (reps / 30.0));

            // Format and display result
            tvResult.setText(String.format("%.1f", oneRepMax));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}