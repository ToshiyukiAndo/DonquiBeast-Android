package com.gaasii.donquebeast2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Timer;

public class TimeLapsActivity extends Activity {

    ImageView imv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_laps);


        imv = (ImageView)findViewById(R.id.mainimage);


        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            int count = 0;
            int bitmun = 0;
            @Override
            public void run() {
                // UIスレッド
                count++;
                if (count > 200) { // x回実行したら終了
                    return;
                }
                File srcFile = new File(Environment.getExternalStorageDirectory() + "/Pictures/capture/cap" + bitmun +".jpeg");
                Log.d("Cap", "number:"+bitmun);
                bitmun++;
                try{
                    InputStream is = new FileInputStream(srcFile);
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    imv.setImageBitmap(bm);
                }catch (FileNotFoundException e){
                    for (int i = 0; i < bitmun; i++){
                        srcFile = new File(Environment.getExternalStorageDirectory() + "/Pictures/capture/cap" + i +".jpeg");
                        srcFile.delete();
                    }
                    Intent intent = new Intent(TimeLapsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                handler.postDelayed(this, 200);
            }
        };
        handler.post(r);



    }
}
