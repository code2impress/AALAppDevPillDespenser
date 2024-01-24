package com.example.aal_appdev_pilldespenser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import java.io.FileOutputStream;
import java.io.IOException;
import android.print.PageRange;


public class ShowQR extends AppCompatActivity {

    private Bitmap qrBitmap; // To hold the QR code bitmap

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
            qrBitmap = toBitmap(writer.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200));
            ImageView qrCodeImage = findViewById(R.id.qrCodeImageView); // Replace with your ImageView ID
            qrCodeImage.setImageBitmap(qrBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Setup print button
        ImageButton printButton = findViewById(R.id.printButton);
        printButton.setOnClickListener(v -> printQRCode());
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

    // Method to print the QR Code
    private void printQRCode() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " QR Code";

        printManager.print(jobName, new PrintDocumentAdapter() {

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                                 CancellationSignal cancellationSignal,
                                 LayoutResultCallback callback, Bundle extras) {
                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }

                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("qr_code.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1)
                        .build();

                callback.onLayoutFinished(pdi, !newAttributes.equals(oldAttributes));
            }

            @Override
            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                                CancellationSignal cancellationSignal,
                                WriteResultCallback callback) {
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    return;
                }

                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(qrBitmap.getWidth(), qrBitmap.getHeight(), 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                canvas.drawBitmap(qrBitmap, 0, 0, null);
                pdfDocument.finishPage(page);

                try {
                    pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } catch (Exception e) {
                    callback.onWriteFailed(e.getMessage());
                } finally {
                    pdfDocument.close();
                }
            }
        }, null);
    }
}
