package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class docgenqr extends AppCompatActivity {

    // UI Components
    private SeekBar mondaySlider, tuesdaySlider, wednesdaySlider, thursdaySlider, fridaySlider, saturdaySlider, sundaySlider;
    private Spinner patientSpinner;
    private Button addPatientButton;
    private TextView mondayValue, tuesdayValue, wednesdayValue, thursdayValue, fridayValue, saturdayValue, sundayValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docgenqr);

        // Initialize UI Components
        mondaySlider = findViewById(R.id.mondaySlider);
        tuesdaySlider = findViewById(R.id.tuesdaySlider);
        wednesdaySlider = findViewById(R.id.wednesdaySlider);
        thursdaySlider = findViewById(R.id.thursdaySlider);
        fridaySlider = findViewById(R.id.fridaySlider);
        saturdaySlider = findViewById(R.id.saturdaySlider);
        sundaySlider = findViewById(R.id.sundaySlider);
        patientSpinner = findViewById(R.id.patientSpinner);
        addPatientButton = findViewById(R.id.addPatientButton);

        // Initialize TextViews for displaying SeekBar values
        mondayValue = findViewById(R.id.mondayValue);
        tuesdayValue = findViewById(R.id.tuesdayValue);
        wednesdayValue = findViewById(R.id.wednesdayValue);
        thursdayValue = findViewById(R.id.thursdayValue);
        fridayValue = findViewById(R.id.fridayValue);
        saturdayValue = findViewById(R.id.saturdayValue);
        sundayValue = findViewById(R.id.sundayValue);

        initializeSpinner();

        // Handle Add Patient Button Click
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPatient();
            }
        });

        // Handle Spinner Item Selected
        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPatientName = parent.getItemAtPosition(position).toString();
                if (!selectedPatientName.equals("New Patient...")) {
                    loadPatientData(selectedPatientName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set SeekBar change listeners to update the TextViews
        setSeekBarChangeListener(mondaySlider, mondayValue);
        setSeekBarChangeListener(tuesdaySlider, tuesdayValue);
        setSeekBarChangeListener(wednesdaySlider, wednesdayValue);
        setSeekBarChangeListener(thursdaySlider, thursdayValue);
        setSeekBarChangeListener(fridaySlider, fridayValue);
        setSeekBarChangeListener(saturdaySlider, saturdayValue);
        setSeekBarChangeListener(sundaySlider, sundayValue);
    }

    private void setSeekBarChangeListener(SeekBar seekBar, final TextView textView) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the corresponding TextView with the current progress
                textView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }
        });
    }


    private void initializeSpinner() {
        List<String> patients = new ArrayList<>();
        patients.add("New Patient...");
        // TODO: Load existing patients from the database here
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patients);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSpinner.setAdapter(adapter);
    }

    private void addPatient() {
        // Retrieve slider values
        int mondayValue = mondaySlider.getProgress();
        int tuesdayValue = tuesdaySlider.getProgress();
        int wednesdayValue = wednesdaySlider.getProgress();
        int thursdayValue = thursdaySlider.getProgress();
        int fridayValue = fridaySlider.getProgress();
        int saturdayValue = saturdaySlider.getProgress();
        int sundayValue = sundaySlider.getProgress();

        // Get patient name
        String patientName = patientSpinner.getSelectedItem().toString();
        if(patientName.equals("New Patient...")) {
            promptForNewPatientName();
            return;
        }

        // Save the patient in the database
        Patient newPatient = new Patient(patientName, mondayValue, tuesdayValue, wednesdayValue, thursdayValue, fridayValue, saturdayValue, sundayValue);
        PatientDatabaseHelper dbHelper = new PatientDatabaseHelper(this);
        dbHelper.addPatient(newPatient);

        // Notify user
        Toast.makeText(this, "Patient added successfully", Toast.LENGTH_SHORT).show();
    }

    private void promptForNewPatientName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Patient's Name");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String patientName = input.getText().toString();
                if (!patientName.isEmpty()) {
                    addNewPatient(patientName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addNewPatient(String patientName) {
        // Retrieve slider values
        int mondayValue = mondaySlider.getProgress();
        int tuesdayValue = tuesdaySlider.getProgress();
        int wednesdayValue = wednesdaySlider.getProgress();
        int thursdayValue = thursdaySlider.getProgress();
        int fridayValue = fridaySlider.getProgress();
        int saturdayValue = saturdaySlider.getProgress();
        int sundayValue = sundaySlider.getProgress();

        // Save the patient in the database
        Patient newPatient = new Patient(patientName, mondayValue, tuesdayValue, wednesdayValue, thursdayValue, fridayValue, saturdayValue, sundayValue);
        PatientDatabaseHelper dbHelper = new PatientDatabaseHelper(this);
        dbHelper.addPatient(newPatient);

        // Update spinner with new patient
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) patientSpinner.getAdapter();
        adapter.add(patientName);
        adapter.notifyDataSetChanged();
        patientSpinner.setSelection(adapter.getCount() - 1);

        // Notify user
        Toast.makeText(this, "Patient added successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadPatientData(String patientName) {
        // Fetch the patient's data from the database
        PatientDatabaseHelper dbHelper = new PatientDatabaseHelper(this);
        Patient patient = dbHelper.getPatient(patientName);
        if (patient != null) {
            mondaySlider.setProgress(patient.getMondayValue());
            tuesdaySlider.setProgress(patient.getTuesdayValue());
            wednesdaySlider.setProgress(patient.getTuesdayValue());
            thursdaySlider.setProgress(patient.getTuesdayValue());
            fridaySlider.setProgress(patient.getTuesdayValue());
            saturdaySlider.setProgress(patient.getTuesdayValue());
            sundaySlider.setProgress(patient.getTuesdayValue());

        }
    }
}
