package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import androidx.appcompat.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.util.Log;
import android.content.SharedPreferences;



public class PD extends AppCompatActivity implements TutorialStepOneDialogFragment.TutorialStepOneListener, TutorialStepTwoDialogFragment.TutorialStepTwoListener {

    private boolean isDataSent = false; // Flag to check if data has been sent
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pd);

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(view -> {
            if (!isDataSent) {
                // Show the first step of the tutorial
                showTutorialStepOne();
            }
        });
    }

    private void showTutorialStepOne() {
        // Display the first tutorial dialog
        new TutorialStepOneDialogFragment().show(getSupportFragmentManager(), "TutorialStepOne");
    }

    @Override
    public void onTutorialStepOneCompleted() {
        // Display the second tutorial dialog when the first step is completed
        new TutorialStepTwoDialogFragment().show(getSupportFragmentManager(), "TutorialStepTwo");
    }

    @Override
    public void onTutorialStepTwoCompleted() {
        // Tutorial is completed, proceed with the data sending process
        String scannedData = getIntent().getStringExtra("dataKey");
        sendData(scannedData);
        imageButton.setEnabled(false); // Disable the button after sending the data
        imageButton.setAlpha(0.4f); // Change the button's opacity to indicate it's disabled
    }

    private void sendData(String data) {
        new Thread(() -> {
            try {
                MqttClient client = new MqttClient("tcp://broker.emqx.io:1883", MqttClient.generateClientId(), null);
                client.connect();
                MqttMessage message = new MqttMessage(data.getBytes());
                client.publish("aalpd2023", message);
                client.disconnect();
                isDataSent = true; // Mark the data as sent

                // Fetch the currently logged-in user's username from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);

                if (username != null) {
                    // Split the data string to get the pills dispensed
                    String[] parts = data.split(",");
                    if (parts.length == 8) { // Ensure there are 8 parts: username and 7 days of pills
                        // Sum the pills dispensed for each day
                        int totalPillsDispensed = 0;
                        for (int i = 1; i <= 7; i++) {
                            totalPillsDispensed += Integer.parseInt(parts[i]);
                        }

                        // Get today's date as a string
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        // Save the total pills dispensed data for the currently logged-in user
                        UserDatabaseHelper dbHelper = new UserDatabaseHelper(PD.this);
                        int userId = dbHelper.getUserIdByUsername(username);
                        if (userId != -1) { // Ensure user is found
                            dbHelper.addPillIntake(userId, date, totalPillsDispensed);
                        }
                    }
                }

                // Delay to show the completion dialog
                new Handler(Looper.getMainLooper()).postDelayed(() -> runOnUiThread(() -> showCompletionDialog(data)), 1000);

            } catch (MqttException | NumberFormatException e) {
                Log.e("PD", "Error processing pill intake data", e);
            }
        }).start();
    }




    private void showCompletionDialog(String data) {
        String patientName = data.split(",")[0]; // Shorten to only the name part

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pills Dispensed");
        builder.setMessage("Pills are now being dispensed for: " + patientName)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(PD.this, MainActivity.class);
                    startActivity(intent); // Navigate back to MainActivity
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        // You might also consider customizing the dialog layout to ensure the text is centered
    }


    private boolean isValidDataFormat(String data) {
        // Implement data validation logic here
        return true; // Placeholder for data format validation
    }
}
