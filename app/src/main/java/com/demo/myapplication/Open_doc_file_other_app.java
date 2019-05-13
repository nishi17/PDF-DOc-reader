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

public class Open_doc_file_other_app extends AppCompatActivity {

    private Context context;

    private AlertDialog.Builder builder;

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 10;

    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 20;

    private CommonClass commonClass;

    private String FileName = "Final_Handbook_2019.doc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_doc_file_other_app);

        context = Open_doc_file_other_app.this;
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

                    openDocumentFile(filePath + "/Final_Handbook_2019.doc");
                } else {
                    Toast.makeText(context, "Error while Copy File", Toast.LENGTH_SHORT).show();
                }
            } else {
                openDocumentFile(filePath + "/" + FileName);
            }
        }
    }

    private void writeFilePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(Open_doc_file_other_app.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Open_doc_file_other_app.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);

        } else {
            checkFolderCreate();
            //CreateFolder();
        }
    }

   /* private void CreateFolder() {
        File folderMain = new File(Environment.getExternalStorageDirectory() + "/PDFreader");

        if (!folderMain.exists()) {

            folderMain.mkdir();
        }


        copyAssets();


    }*/

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

    /*private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        String filePath = android.os.Environment.getExternalStorageDirectory().toString()
                + "/PDFreader";
       *//* try {

            files = assetManager.list(sAssets);
        } catch (IOException e) {
            Log.e(""error"", "Failed to get asset file list.", e);
        }*//*
//        if (files != null) for (String filename : files) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open("Final_Handbook_2019.doc");
            File outFile = new File(filePath, "Final_Handbook_2019.doc");
            out = new FileOutputStream(outFile);
            copyFile(in, out);

            openDocumentFile(filePath + "/Final_Handbook_2019.doc");
        } catch (IOException e) {
            Log.e("error", "Failed to copy asset file: Final_Handbook_2019.doc", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }
//        }
    }*/


   /* private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }*/

    private void openDocumentFile(String filePath) {

        try {

            File file = new File(filePath);
            if (file.exists()) {

                boolean isAppInstalledIN_microsoft = appInstalledOrNot("com.microsoft.office.word");

                boolean isAppInstalledINDocs = appInstalledOrNot("com.google.android.apps.docs.editors.docs");

                if (isAppInstalledIN_microsoft || isAppInstalledINDocs) {

                    Uri outputFileUri = FileProvider.getUriForFile(context, "com.demo.myapplication.fileprovider", file);

                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);

                    viewIntent.setDataAndType(outputFileUri, "application/msword");

                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivity(viewIntent);

                    finish();

                } else {
                    Toast.makeText(context, "Install Microsoft Word Or Google Docs!", Toast.LENGTH_SHORT).show();

                    builder = new AlertDialog.Builder(this);

                    builder.setMessage("Do you want to install Google Docs ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    final String appPackageName = "com.google.android.apps.docs.editors.docs"; // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        finish();
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        finish();
                                    }

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Microsoft Word or Google Docs app are not install in your phone.Please Install it",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Not proper source to read");
                    alert.show();

                }

            } else {

                Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Toast.makeText(context, "Opps something wrong!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean appInstalledOrNot(String uri) {

        PackageManager pm = context.getPackageManager();

        try {

            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);

            return true;

        } catch (PackageManager.NameNotFoundException e) {

        }

        return false;

    }

}
