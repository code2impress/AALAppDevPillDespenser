package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScanQR extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScannerView;
    private TextView scannedDataTextView;
    private PatientDatabaseHelper databaseHelper;

    // Callback for barcode scanning results
    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            String scannedData = result.getText();
            scannedDataTextView.setText(scannedData);

            // Parse the QR code data
            String[] qrData = scannedData.split(",");
            String patientName = qrData[0];

            // Check if patient exists in the database
            Patient patient = databaseHelper.getPatient(patientName);
            if (patient != null) {
                // If patient exists, navigate to PD activity and pass the scanned data
                Intent intent = new Intent(ScanQR.this, PD.class);
                intent.putExtra("dataKey", scannedData); // Use this to pass the scanned data
                startActivity(intent);
            } else {
                // If patient does not exist, show a toast message
                Toast.makeText(ScanQR.this, "Patient not found", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        // Initialize views and database helper
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        scannedDataTextView = findViewById(R.id.scannedDataTextView);
        databaseHelper = new PatientDatabaseHelper(this);

        // Initialize barcode scanner view and start continuous decoding
        barcodeScannerView.initializeFromIntent(getIntent());
        barcodeScannerView.decodeContinuous(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume barcode scanner view
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause barcode scanner view
        barcodeScannerView.pause();
    }
}
