package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PD extends AppCompatActivity {

    private boolean isDataSent = false; // Flag to check if data has been sent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pd);

        String scannedData = getIntent().getStringExtra("dataKey");
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDataSent) { // Check if data hasn't been sent yet
                    sendData(scannedData);
                    imageButton.setEnabled(false); // Disable the button
                    imageButton.setAlpha(0.4f); // Change button opacity to indicate it's disabled
                }
            }
        });
    }

    private void sendData(String data) {
        // Validate data format
        if (isValidDataFormat(data)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MqttClient client = new MqttClient("tcp://test.mosquitto.org:1883", MqttClient.generateClientId(), null);
                        client.connect();
                        MqttMessage message = new MqttMessage(data.getBytes());
                        client.publish("aalpd2023", message);
                        client.disconnect();
                        isDataSent = true; // Update flag after successful data send
                    } catch (MqttException e) {
                        e.printStackTrace();
                        // Handle error
                    }
                }
            }).start();
        } else {
            // Handle invalid data format
        }
    }

    private boolean isValidDataFormat(String data) {
        // Implement your validation logic here
        // Return true if data is valid, false otherwise
        return true; // Placeholder
    }
}
