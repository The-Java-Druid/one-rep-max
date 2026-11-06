package com.javadruid.onerepmax;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.javadruid.onerepmax.algorithms.BrzyckiAlgorithm;
import com.javadruid.onerepmax.algorithms.EpleyAlgorithm;
import com.javadruid.onerepmax.algorithms.LanderAlgorithm;
import com.javadruid.onerepmax.algorithms.LombardiAlgorithm;
import com.javadruid.onerepmax.algorithms.OneRepMaxAlgorithm;

import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "OneRepMaxPrefs";
    private static final String KEY_SELECTED_ALGORITHM = "selected_algorithm";
    private static final int[] percentages = {
            100, 97, 94, 92, 89, 86, 83, 81, 78, 75, 73, 71, 70, 68, 67
    };

    private EditText etWeight;
    private EditText etReps;
    private TextView tvResult;
    private LinearLayout layoutBreakdownContainer;
    private TextView algorithmDescription;
    // Standard percentage mapping for reps 1 through 15
    private OneRepMaxAlgorithm selectedAlgorithm;
    private boolean isInitialized = false; // Prevents saving during initial layout setup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enable edge-to-edge layout for Android 15+
        EdgeToEdge.enable(this);

        // Initialize UI components
        etWeight = findViewById(R.id.etWeight);
        etReps = findViewById(R.id.etReps);
        final Button btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);
        layoutBreakdownContainer = findViewById(R.id.layoutBreakdownContainer);

        final Spinner spinnerAlgorithm = findViewById(R.id.spinnerAlgorithm);
        algorithmDescription = findViewById(R.id.tvAlgorithmDescription);

        final OneRepMaxAlgorithm[] algorithms = getOneRepMaxAlgorithms();
        final ArrayAdapter<OneRepMaxAlgorithm> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                algorithms
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlgorithm.setAdapter(adapter);

        // Restore previously saved algorithm preference
        final SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        final String savedAlgorithmName = prefs.getString(KEY_SELECTED_ALGORITHM, "Epley");

        final int defaultSelectionIndex = IntStream.range(0, algorithms.length)
                .filter(i -> algorithms[i].name().equals(savedAlgorithmName))
                .findFirst().orElse(0);
        // Initialize selectedAlgorithm immediately with the saved choice
        selectedAlgorithm = algorithms[defaultSelectionIndex];
        // 1. Attach listener BEFORE setting selection so state stays synchronized
        spinnerAlgorithm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAlgorithm = (OneRepMaxAlgorithm) parent.getItemAtPosition(position);
                algorithmDescription.setText(selectedAlgorithm.description());
                // Save preference if user explicitly changed it after initialization
                if (isInitialized) {
                    prefs.edit()
                         .putString(KEY_SELECTED_ALGORITHM, selectedAlgorithm.name())
                         .apply();
                }
                calculateOneRepMax();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAlgorithm = algorithms[0]; // Fallback to Epley
            }
        });
        // 2. Set the spinner selection (triggers onItemSelected safely with listener active)
        spinnerAlgorithm.setSelection(defaultSelectionIndex);
        // 3. Mark initialization complete so future manual selections trigger saves
        isInitialized = true;
        // Set click listener for the calculate button
        btnCalculate.setOnClickListener(this::onCalculateClick);
        // Trigger calculation when the user presses "Enter" / "Done" on the keyboard in reps field
        etReps.setOnEditorActionListener(this::onEditorEnter);    }

    @NonNull
    private static OneRepMaxAlgorithm[] getOneRepMaxAlgorithms() {
        // Setup Algorithms array (Epley is index 0 so it defaults first)
        return new OneRepMaxAlgorithm[]{
                new EpleyAlgorithm(),
                new BrzyckiAlgorithm(),
                new LanderAlgorithm(),
                new LombardiAlgorithm()
        };
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

    private double getOneRepMax(double weight, int reps) {
        return selectedAlgorithm.getOneRepMax(weight, reps);
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