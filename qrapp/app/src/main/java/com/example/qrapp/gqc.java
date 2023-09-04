package com.example.qrapp;

import static android.media.MediaScannerConnection.scanFile;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import java.util.Calendar;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class gqc extends AppCompatActivity {

    EditText editName, editGuardian, editBlood, editAddress;
    Button generateBtn;
    ImageView qrCodeImg;
    QRGEncoder qrgEncoder;
    Bitmap qrBits;
    String name, guardian, blood, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gqc);

        editName = (EditText) findViewById(R.id.editText);
        editGuardian = (EditText) findViewById(R.id.editText2);
        editBlood = (EditText) findViewById(R.id.editText3);
        editAddress = (EditText) findViewById(R.id.editText4);
        generateBtn = (Button) findViewById(R.id.downBtn);
        qrCodeImg = (ImageView) findViewById(R.id.qrGen);

        qrCodeImg.buildDrawingCache();
        Bitmap bmp = qrCodeImg.getDrawingCache();

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editName.getText().toString();
                guardian = editGuardian.getText().toString();
                blood = editBlood.getText().toString();
                address = editAddress.getText().toString();

                if (name.isEmpty() || guardian.isEmpty() || blood.isEmpty() || address.isEmpty()) {
                    Toast.makeText(gqc.this, "Please Enter Details", Toast.LENGTH_SHORT).show();
                }
                else {
                    qrgEncoder = new QRGEncoder(name + ";" + guardian + ";" + blood + ";" + address, QRGContents.Type.TEXT, 1000);
                    qrBits = qrgEncoder.getBitmap();
                    qrCodeImg.setImageBitmap(qrBits);

                    String filename;
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
                    String sdf = currentDate +"-"+currentTime;
                    filename =  sdf.format(sdf);

                    try {
                        String root = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).toString();
                        File myDir = new File(root);
                        myDir.mkdirs();
                        String fname = filename + ".jpg";
                        File file = new File(root, fname);

                        FileOutputStream out = new FileOutputStream(file);
                        Bitmap bm = qrgEncoder.getBitmap();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();

                        Toast.makeText(gqc.this, "Saved to Gallery, Successfuly", Toast.LENGTH_SHORT).show();
                        scanner(file.getAbsolutePath());

                    }
                    catch (Exception e)
                    {
                        Log.d("onBtnSavePng", e.toString());
                    }
                }
            }
        });
    }

    private void scanner(String path) {

        MediaScannerConnection.scanFile(gqc.this,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }

    public void onBtnSavePng(View view) {
        String filename;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
        String sdf = currentDate +"-"+currentTime;
        filename =  sdf.format(sdf);

        try {
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = filename + ".jpg";
            File file = new File(root, fname);

            FileOutputStream out = new FileOutputStream(file);
            Bitmap bm = qrgEncoder.getBitmap();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Toast.makeText(gqc.this, "Saved to Gallery, Successfuly", Toast.LENGTH_SHORT).show();
            scanner(file.getAbsolutePath());

        }
        catch (Exception e)
        {
            Log.d("onBtnSavePng", e.toString());
        }
    }
}
