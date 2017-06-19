package com.gaasii.donquebeast2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnTouchListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView forwardButton = (ImageView)findViewById(R.id.forwardButton);
        forwardButton.setOnTouchListener(this);

        ImageView backwardButton = (ImageView)findViewById(R.id.backwardButton);
        backwardButton.setOnTouchListener(this);

        ImageView rightButton = (ImageView)findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(this);

        ImageView leftButton = (ImageView)findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(this);

        ImageView capButton = (ImageView)findViewById(R.id.capture_button);
        capButton.setOnTouchListener(this);

        ImageView lapsButton = (ImageView)findViewById(R.id.laps_button);
        lapsButton.setOnTouchListener(this);



    }


    @Override
    public boolean onTouch(View v, MotionEvent e){
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Log", "ACTION_DOWN");
                if (v.getId() == R.id.forwardButton) {
                    Log.d("Logggg", "Go Forward.");
                    HttpPostTask task = new HttpPostTask(this);
                    task.execute(1);
                } else if (v.getId() == R.id.backwardButton) {
                    Log.d("Logggg", "Go Backward.");
                    HttpPostTask task = new HttpPostTask(this);
                    task.execute(2);
                } else if (v.getId() == R.id.rightButton) {
                    Log.d("Logggg", "Go Right.");
                    HttpPostTask task = new HttpPostTask(this);
                    task.execute(4);
                } else if (v.getId() == R.id.leftButton) {
                    Log.d("Logggg", "Go Left.");
                    HttpPostTask task = new HttpPostTask(this);
                    task.execute(3);
                } else {
                    Log.d("Logggg", "Else");
                }
                break;

            case MotionEvent.ACTION_UP:


                if (v.getId() == R.id.capture_button) {
                    saveIndex();
                }else if (v.getId() == R.id.laps_button) {
                    Intent intent = new Intent(MainActivity.this, TimeLapsActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d("Log", "ACTION_UP");
                    HttpPostTask task = new HttpPostTask(this);
                    task.execute(0);
                }
                break;
        }
        return true;
    }


    public void saveIndex(){
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.TITLE, "cap.jpg");
        values.put("_data", Environment.getExternalStorageDirectory() + "/Pictures/capture/cap.jpeg");
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

    }

}
