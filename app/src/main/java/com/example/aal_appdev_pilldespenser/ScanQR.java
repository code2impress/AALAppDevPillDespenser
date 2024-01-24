package com.example.aal_appdev_pilldespenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScanQR extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScannerView;
    private TextView scannedDataTextView;

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            // Handle the scanned data here
            String scannedData = result.getText();
            scannedDataTextView.setText(scannedData);
            // If you would like to resume scanning, call this method below:
            // barcodeScannerView.resume();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr); // Use your layout file
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        scannedDataTextView = findViewById(R.id.scannedDataTextView);

        barcodeScannerView.initializeFromIntent(getIntent());
        barcodeScannerView.decodeContinuous(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }
}
