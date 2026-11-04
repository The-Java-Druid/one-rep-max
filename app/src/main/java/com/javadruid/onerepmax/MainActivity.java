package com.javadruid.onerepmax;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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

        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // Load Ad
        final AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Initialize UI components
        etWeight = findViewById(R.id.etWeight);
        etReps = findViewById(R.id.etReps);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);
        layoutBreakdownContainer = findViewById(R.id.layoutBreakdownContainer);

        // Set click listener for the calculate button
        btnCalculate.setOnClickListener(this::onCalculateClick);
        // Trigger calculation when the user presses "Enter" / "Done" on the keyboard in reps field
        etReps.setOnEditorActionListener(this::onEditorEnter);    }

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


    private void populateBreakdownTable(double oneRepMax) {
        // Clear any previous rows
        layoutBreakdownContainer.removeAllViews();
        for (int i = 0; i < percentages.length; i++) {
            final int pct = percentages[i];
            populateRow(pct, i + 1, getTargetWeight(oneRepMax, pct));
        }
    }

    private void populateRow(int pct, int repNum, double targetWeight) {
        final LinearLayout rowLayout = createRowLayout();
        // Reps Text
        final TextView tvRepCol = createTextView(String.valueOf(repNum));
        // Percentage Text
        final TextView tvPctCol = createTextView(pct + "%");
        tvPctCol.setGravity(android.view.Gravity.CENTER);
        // Target Weight Text
        final TextView tvWeightCol = createTextView(String.format("%.1f", targetWeight));
        tvWeightCol.setGravity(android.view.Gravity.END);
        // Add columns to row
        rowLayout.addView(tvRepCol);
        rowLayout.addView(tvPctCol);
        rowLayout.addView(tvWeightCol);
        // Add row to container
        layoutBreakdownContainer.addView(rowLayout);
    }

    @NonNull
    private LinearLayout createRowLayout() {
        // Create a horizontal row layout for each rep level
        final LinearLayout result = new LinearLayout(this);
        result.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        result.setOrientation(LinearLayout.HORIZONTAL);
        result.setPadding(0, 8, 0, 8);
        return result;
    }

    @NonNull
    private TextView createTextView(String text) {
        final TextView result = new TextView(this);
        result.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        result.setTextSize(14);
        result.setText(text);
        return result;
    }

    private static double getOneRepMax(double weight, int reps) {
        // Epley Formula: Weight * (1 + Reps / 30.0)
        return weight * (1.0 + (reps / 30.0));
    }

    private static double getTargetWeight(double oneRepMax, int pct) {
        return oneRepMax * (pct / 100.0);
    }

    private boolean onEditorEnter(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
            onCalculateClick(v);
            return true;
        }
        return false;
    }
}