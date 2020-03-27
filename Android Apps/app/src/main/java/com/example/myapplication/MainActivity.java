package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String JSON_RESULT = "com.example.myfirstapp.JSON_Result";

    private int PICK_IMAGE_REQUEST = 1;
    private Button buttonChoose;
    private Button buttonUpload;
    private Uri uri;
    private Bitmap bitmap;
    public String result;
    public URL url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonChoose = findViewById(R.id.button);
        buttonUpload = findViewById(R.id.button2);

        Intent intent = getIntent();
        String ip = intent.getStringExtra(LoginActivity.IP_ADDRESS);
        try {
            url = new URL("http://"+ip+"/find_cell");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                return;
            }
        }

        mainListener();
    }

    public void mainListener(){
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainListener();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            chooseImage();
        }
        if (v == buttonUpload){
            try {
                detectCells();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    class HitAPI extends AsyncTask<Void, Void, Void>{

        String attachmentName = "image_file";
        String attachmentFileName = "image_file.png";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                attachmentFileName = getFileNAme(uri);

//                URL url = new URL("http://192.168.1.13:8082/find_cell");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(50000);
                connection.setConnectTimeout(50000);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + this.boundary);
                DataOutputStream request = new DataOutputStream(
                        connection.getOutputStream());
                request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        this.attachmentName + "\";filename=\"" +
                        this.attachmentFileName + "\"" + this.crlf);
                request.writeBytes(this.crlf);
                request.write(BitMapToString(bitmap));
                request.writeBytes(this.crlf);
                request.writeBytes(this.twoHyphens + this.boundary +
                        this.twoHyphens + this.crlf);
                request.flush();
                request.close();
                connection.connect();
                int responseCode=connection.getResponseCode(); // To Check for 200
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader( new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    result = sb.toString();
                }
                return null;
            } catch (Exception e) {
                Log.d("MainActivity", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nextActivity();
        }

        public byte[] BitMapToString(Bitmap userImage1) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
            byte[] b = baos.toByteArray();
            return b;
        }

        //method to get the file path from uri
        public String getFileNAme(Uri uri) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();

            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                path = path.substring(cut + 1);
            }

            return path;
        }
    }

    public void detectCells(){
        new HitAPI().execute();
    }

    public void nextActivity(){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(JSON_RESULT, result);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.imageView2);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
