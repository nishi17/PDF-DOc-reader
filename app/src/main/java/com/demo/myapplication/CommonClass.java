package com.demo.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommonClass {

    public Context commonClassContext;

    public CommonClass(Context commonClassContext) {
        this.commonClassContext = commonClassContext;
    }

    public boolean CreateFolder() {

        try {
            File folderMain = new File(Environment.getExternalStorageDirectory() + "/PDFreader");

            if (!folderMain.exists()) {

                folderMain.mkdir();
            }
            return true;

        } catch (Exception e) {
            return false;
        }


    }


    public boolean copyAssets(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        String filePath = android.os.Environment.getExternalStorageDirectory().toString()
                + "/PDFreader";
       /* try {

            files = assetManager.list(sAssets);
        } catch (IOException e) {
            Log.e(""error"", "Failed to get asset file list.", e);
        }*/
//        if (files != null) for (String filename : files) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(filePath, fileName);
            out = new FileOutputStream(outFile);
            copyFile(in, out);

            //openDocumentFile(filePath + "/" + fileName);
            return true;
        } catch (IOException e) {
            Log.e("error", "Failed to copy asset file: " + fileName, e);
            return false;
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
            return true;
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
