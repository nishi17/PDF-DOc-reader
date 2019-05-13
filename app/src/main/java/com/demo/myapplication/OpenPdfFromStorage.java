package com.demo.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

public class OpenPdfFromStorage extends AppCompatActivity {


    private Context context;

    private AlertDialog.Builder builder;

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 10;

    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 20;

    private CommonClass commonClass;

    private String FileName = "pdffile.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf_from_storage);

        context = OpenPdfFromStorage.this;
        commonClass = new CommonClass(context);

        if (Build.VERSION.SDK_INT >= 23) {

            writeFilePermissions();
        } else {
            checkFolderCreate();
            //copyAssets();
        }

    }

    private void checkFolderCreate() {

        boolean b = commonClass.CreateFolder();

        if (b) {
            String filePath = android.os.Environment.getExternalStorageDirectory().toString() + "/PDFreader";
            File file = new File(filePath + "/" + FileName);


            if (!file.exists()) {

                boolean isCopy = commonClass.copyAssets(context, FileName);
                if (isCopy) {

                    openPDFFile(filePath + "/" + FileName);
                } else {
                    Toast.makeText(context, "Error while Copy File", Toast.LENGTH_SHORT).show();
                }
            } else {
                openPDFFile(filePath + "/" + FileName);
            }
        }
    }

    private void writeFilePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(OpenPdfFromStorage.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);

        } else {
            checkFolderCreate();
            //CreateFolder();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    CreateFolder();
                    checkFolderCreate();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void openPDFFile(String filePath) {

        try {

            File file = new File(filePath);
            if (file.exists()) {


                Uri outputFileUri = FileProvider.getUriForFile(context, "com.demo.myapplication.fileprovider", file);


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(outputFileUri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("No Application Found");
                    builder.setMessage("Download one from Android Market?");
                    builder.setPositiveButton("Yes, Please",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                    marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                                    startActivity(marketIntent);
                                    finish();
                                }
                            });
                    builder.setNegativeButton("No, Thanks", null);
                    builder.create().show();

                }

            } else {

                Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Toast.makeText(context, "Opps something wrong!", Toast.LENGTH_SHORT).show();
        }

    }

}
