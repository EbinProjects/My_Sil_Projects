package com.example.printingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.printingapp.bluetooth.BluetoothChat;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FileUtils;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "MainActivity";
    private  static MaterialButton btn_file_chooser,btn_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_file_chooser=findViewById(R.id.btn_file_chooser);
        btn_print=findViewById(R.id.btn_print);
        btn_print.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BluetoothChat.class);
                startActivity(intent);
            }
        });

        btn_file_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    String path = null;
                    try {
                        File file=toFile(uri);
                        Log.d(TAG, "File size: " + file.length());
                        pdfToPng(file);
//                        path = FileUtils.getPath(this, uri);
                    } catch (Exception e) {
                        Log.d(TAG, "catch (URISyntaxException e) " + e.getMessage());
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @SuppressLint("Range")
    private File toFile(Uri uri) throws IOException {
        String displayName = "";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            try {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }finally {
                cursor.close();
            }
        }

        File file =  File.createTempFile( FilenameUtils.getBaseName(displayName), "."+FilenameUtils.getExtension(displayName));
        InputStream inputStream = getContentResolver().openInputStream(uri);
        FileUtils.copyInputStreamToFile(inputStream, file);
        return file;
    }

    private  void pdfToPng(File pdfFile) throws IOException {

        if (isStoragePermissionGranted()) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root, "/printer");
            if(myDir.exists()){
            FileUtils.deleteDirectory(myDir);}

        }

        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer renderer = new PdfRenderer(fileDescriptor);
        final int pageCount = renderer.getPageCount();
        Log.e("abcd","pageCount ==> "+pageCount);

        if(pageCount>0){
            btn_print.setEnabled(true);
            Log.e("abcd","pageCount ==>  btn_print.setEnabled(true); ");
        }else {
            btn_print.setEnabled(false);
            Log.e("abcd","pageCount ==>  btn_print.setEnabled(false); ");
        }


        for (int i = 0; i < pageCount; i++) {
            PdfRenderer.Page page = renderer.openPage(i);
            Bitmap bitmap = Bitmap.createBitmap(200, 500, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();

            if (bitmap == null)
                return;
            String root = Environment.getExternalStorageDirectory().toString();

            if (isStoragePermissionGranted()) {
                File myDir = new File(root, "/printer");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }

                String fname = "img"+i+".png";
                File file = new File(myDir, fname);
                if (file.exists()) {
                    file.delete();
                }


                try {
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    Log.v("Saved Image - ", file.getAbsolutePath());
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Log.v("abcd", e.getMessage());
                    e.printStackTrace();
                }

                MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null);
            }

        }

    }

    public boolean isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

}