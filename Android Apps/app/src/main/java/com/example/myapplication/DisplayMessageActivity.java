package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayMessageActivity extends AppCompatActivity {

    private ImageView image1;
    private Bitmap[] imageArray;
    private int currentIndex;
    private int startIndex;
    private int endIndex;
    public String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        image1 = (ImageView)findViewById(R.id.imageView);
        startIndex = 0;


//         Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String result = intent.getStringExtra(MainActivity.JSON_RESULT);
        JSONObject json = null;
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.editText);
        try {
            System.out.println(result);
            json = new JSONObject(result);
            textView.setText("Detected cell Count: "+ json.get("cells_count"));
            System.out.println(json.getJSONArray("images").length());
            imageArray = new Bitmap[json.getJSONArray("images").length()];
            urls = new String[json.getJSONArray("images").length()];
            for(int i=0;i<urls.length;i++){
                urls[i] = "http://"+json.getJSONArray("images").getString(i);
            }
            endIndex = urls.length-1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ImageExtractor().execute();

        nextImage();
    }

    class ImageExtractor extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for(int i=0;i<urls.length;i++){
                    URL url = new URL(urls[i]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    imageArray[i] = BitmapFactory.decodeStream(input);
                    Log.d("Check", urls[i]);
                }
                return null;
            } catch (Exception e) {
                Log.d("DisplayMessageActivity", e.getMessage());
                System.out.println(e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void nextImage(){
        image1.setImageBitmap(imageArray[currentIndex]);
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.up);
        currentIndex++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex>endIndex){
                    currentIndex--;
                    previousImage();
                }else{
                    nextImage();
                }

            }
        },5000); // here 1000(1 second) interval to change from current  to next image
        image1.startAnimation(rotateimage);

    }
    public void previousImage() {
        image1.setImageBitmap(imageArray[currentIndex]);
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.down);

        currentIndex--;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentIndex < startIndex) {
                    currentIndex++;
                    nextImage();
                } else {
                    previousImage(); // here 1000(1 second) interval to change from current  to previous image
                }
            }
        }, 5000);
        image1.startAnimation(rotateimage);
    }

}
