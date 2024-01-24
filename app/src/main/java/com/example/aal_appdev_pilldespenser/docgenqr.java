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
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
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

        setSeekBarChangeListener(mondaySlider, mondayValue, "Monday");
        setSeekBarChangeListener(tuesdaySlider, tuesdayValue, "Tuesday");
        setSeekBarChangeListener(wednesdaySlider, wednesdayValue, "Wednesday");
        setSeekBarChangeListener(thursdaySlider, thursdayValue, "Thursday");
        setSeekBarChangeListener(fridaySlider, fridayValue, "Friday");
        setSeekBarChangeListener(saturdaySlider, saturdayValue, "Saturday");
        setSeekBarChangeListener(sundaySlider, sundayValue, "Sunday");


        // Initialize the ImageButton
// Inside onCreate method of docgenqr activity
        ImageButton imageButton = findViewById(R.id.button2);
        imageButton.setOnClickListener(view -> {
            String selectedPatientName = patientSpinner.getSelectedItem().toString();
            PatientDatabaseHelper dbHelper = new PatientDatabaseHelper(this);
            Patient patient = dbHelper.getPatient(selectedPatientName);

            if (patient != null) {
                Intent intent = new Intent(docgenqr.this, ShowQR.class);
                intent.putExtra("patientName", patient.getName());
                intent.putExtra("mondayValue", patient.getMondayValue());
                intent.putExtra("tuesdayValue", patient.getTuesdayValue());
                intent.putExtra("wednesdayValue", patient.getWednesdayValue());
                intent.putExtra("thursdayValue", patient.getThursdayValue());
                intent.putExtra("fridayValue", patient.getFridayValue());
                intent.putExtra("saturdayValue", patient.getSaturdayValue());
                intent.putExtra("sundayValue", patient.getSundayValue());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Patient not found", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setSeekBarChangeListener(SeekBar seekBar, final TextView textView, final String day) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Get the selected patient name
                String patientName = patientSpinner.getSelectedItem().toString();
                if (!patientName.equals("New Patient...")) {
                    // Get the value of the slider
                    int value = seekBar.getProgress();

                    // Update the patient's data in the database
                    PatientDatabaseHelper dbHelper = new PatientDatabaseHelper(docgenqr.this);
                    dbHelper.updatePatientDayValue(patientName, day, value);

                    Toast.makeText(docgenqr.this, "Updated " + day + " value for " + patientName, Toast.LENGTH_SHORT).show();
                }
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
            wednesdaySlider.setProgress(patient.getWednesdayValue());
            thursdaySlider.setProgress(patient.getThursdayValue());
            fridaySlider.setProgress(patient.getFridayValue());
            saturdaySlider.setProgress(patient.getSaturdayValue());
            sundaySlider.setProgress(patient.getSundayValue());


        }
    }
}
