package com.javadruid.onerepmax;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight;
    private EditText etReps;
    private TextView tvResult;
    private LinearLayout layoutBreakdownContainer;

    // Standard percentage mapping for reps 1 through 15
    private final int[] percentages = {
            100, 97, 94, 92, 89, 86, 83, 81, 78, 75, 73, 71, 70, 68, 67
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        etWeight = findViewById(R.id.etWeight);
        etReps = findViewById(R.id.etReps);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);
        layoutBreakdownContainer = findViewById(R.id.layoutBreakdownContainer);

        // Set click listener for the calculate button
        btnCalculate.setOnClickListener(this::onCalculateClick);
    }

    private void onCalculateClick(View v) {
        calculateOneRepMax();
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
                layoutBreakdownContainer.removeAllViews();
                return;
            }

            final double oneRepMax = getOneRepMax(weight, reps);

            // Format and display result
            tvResult.setText(String.format("%.1f", oneRepMax));

            // Populate the 1 to 15 rep breakdown table
            populateBreakdownTable(oneRepMax);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private static double getOneRepMax(double weight, int reps) {
        // Epley Formula: Weight * (1 + Reps / 30.0)
        return weight * (1.0 + (reps / 30.0));
    }

    private void populateBreakdownTable(double oneRepMax) {
        // Clear any previous rows
        layoutBreakdownContainer.removeAllViews();

        for (int i = 0; i < percentages.length; i++) {
            int repNum = i + 1;
            int pct = percentages[i];
            double targetWeight = oneRepMax * (pct / 100.0);

            // Create a horizontal row layout for each rep level
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setPadding(0, 8, 0, 8);

            // Reps Text
            TextView tvRepCol = new TextView(this);
            tvRepCol.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            tvRepCol.setText(String.valueOf(repNum));
            tvRepCol.setTextSize(14);

            // Percentage Text
            TextView tvPctCol = new TextView(this);
            tvPctCol.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            tvPctCol.setText(pct + "%");
            tvPctCol.setTextSize(14);
            tvPctCol.setGravity(android.view.Gravity.CENTER);

            // Target Weight Text
            TextView tvWeightCol = new TextView(this);
            tvWeightCol.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            tvWeightCol.setText(String.format("%.1f", targetWeight));
            tvWeightCol.setTextSize(14);
            //tvWeightCol.setTextStyle(android.graphics.Typeface.BOLD);
            tvWeightCol.setGravity(android.view.Gravity.END);

            // Add columns to row
            rowLayout.addView(tvRepCol);
            rowLayout.addView(tvPctCol);
            rowLayout.addView(tvWeightCol);

            // Add row to container
            layoutBreakdownContainer.addView(rowLayout);
        }
    }
}