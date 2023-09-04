package com.example.qrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class sqc extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView Details,Details2,Details3,Details4;
    private Button Close;

    TextView textView;
    Button buttonView;
    CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqc);

        textView = (TextView) findViewById(R.id.textView);
        buttonView = (Button) findViewById(R.id.buttonview);

        if(checkPermission())
        {
            CodeScannerView scannerView = findViewById(R.id.scanner_view);


            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            textView.setText(result.getText());
                            getQRDetailsDialog(result.getText());
                        }
                    });
                }
            });
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });
        }
        else
        {
            requestPermission();
        }
    }

    public void getQRDetailsDialog(String variable)
    {
        dialogBuilder = new AlertDialog.Builder(this);
        final View qrPopupView = getLayoutInflater().inflate(R.layout.sqc_popup, null);

        Details = (TextView) qrPopupView.findViewById(R.id.details);
        Details2 = (TextView) qrPopupView.findViewById(R.id.details2);
        Details3 = (TextView) qrPopupView.findViewById(R.id.details3);
        Details4 = (TextView) qrPopupView.findViewById(R.id.details4);
        Close = (Button) qrPopupView.findViewById(R.id.close);

        dialogBuilder.setView(qrPopupView);
        dialog = dialogBuilder.create();

        String[] split = variable.split(";");

        Details.setText("Person's Name:" + split[0]);
        Details2.setText("Guardian's Name:" + split[1]);
        Details3.setText("Blood Group:" + split[2]);
        Details4.setText("Address:" + split[3]);

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(sqc.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}