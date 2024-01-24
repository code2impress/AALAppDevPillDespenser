package com.example.aal_appdev_pilldespenser;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

public class ShowQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);

        // Retrieve the data
        String patientName = getIntent().getStringExtra("patientName");
        int mondayValue = getIntent().getIntExtra("mondayValue", 0);
        int tuesdayValue = getIntent().getIntExtra("tuesdayValue", 0);
        int wednesdayValue = getIntent().getIntExtra("wednesdayValue", 0);
        int thursdayValue = getIntent().getIntExtra("thursdayValue", 0);
        int fridayValue = getIntent().getIntExtra("fridayValue", 0);
        int saturdayValue = getIntent().getIntExtra("saturdayValue", 0);
        int sundayValue = getIntent().getIntExtra("sundayValue", 0);

        // Generate QR Code
        QRCodeWriter writer = new QRCodeWriter();
        try {
            String qrContent = patientName + "," +
                    mondayValue + "," +
                    tuesdayValue + "," +
                    wednesdayValue + "," +
                    thursdayValue + "," +
                    fridayValue + "," +
                    saturdayValue + "," +
                    sundayValue;
            Bitmap bitmap = toBitmap(writer.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200));
            ImageView qrCodeImage = findViewById(R.id.qrCodeImageView); // Replace with your ImageView ID
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    // Method to convert the QR Code to Bitmap
    private Bitmap toBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}

