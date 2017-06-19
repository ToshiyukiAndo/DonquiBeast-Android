package com.gaasii.donquebeast2;

/**
 * Created by negi on 2017/06/18.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class MySurfaceView extends SurfaceView  implements Runnable, SurfaceHolder.Callback {
    private static final boolean DEBUG=false;
    private static final double SCALE=2;	// scale 1/2
    boolean isLoop = true;
    MyHttpReader mMyHttpReader;
    Thread mThread;
    Bitmap bitmap2;

    int bitmun = 0;
    int bitnum2 = 0;



    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(DEBUG) Log.d("MySurfaceView","MySurfaceView() :#1 called!!");
        // TODO 自動生成されたコンストラクター・スタブ
        getHolder().addCallback(this);
        initMySurfaceView();

    }

    public MySurfaceView(Context context) {
        super(context);
        if(DEBUG) Log.d("MySurfaceView","MySurfaceView() :#1 called!!");
        // TODO 自動生成されたコンストラクター・スタブ
        getHolder().addCallback(this);
        initMySurfaceView();
    }
    /*
     * void initMySurfaceView()
     */
    public void initMySurfaceView(){
        mMyHttpReader = new MyHttpReader();
    }
    /*
     * my thread
     * void run()
     */
    @Override
    public void run() {
        // TODO 自動生成されたメソッド・スタブ


        while(isLoop){
            // Mjpeg-Streamer access
            mMyHttpReader.get();
            // access ok
            if(mMyHttpReader.mHttpEntity != null){
                try {
                    // convert jpeg to Bitmap object
                    Bitmap bitmap = BitmapFactory.decodeStream(mMyHttpReader.mHttpEntity.getContent());
                    // convert jpeg to Bitmap OK
                    if(bitmap != null){
                        Canvas mCanvas = getHolder().lockCanvas();
                        if(mCanvas != null){
                            bitmap2= Bitmap.createScaledBitmap(bitmap, (int)(640*SCALE), (int)(480*SCALE), false);
                            mCanvas.drawBitmap(bitmap2, 0, 0, null);
                            if(bitnum2%4 == 0) {
                                savePic();
                            }
                            bitnum2++;
                            getHolder().unlockCanvasAndPost(mCanvas);
                        }
                    }
                    else{
                        if(DEBUG) Log.e("MySurfaceView","run() #3: bitmap == null");
                    }
                }
                catch (IllegalStateException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
                catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
            }
            // 毎回 shutdown しなくても良いみたい。
            //mMyHttpReader.shutdown();
            if(isLoop==true){
                //try{
                //	Thread.sleep(5000); //5000ミリ秒Sleepする
                //}
                //catch(InterruptedException e){}
            }
        }
        // HttpClient shutdown
        mMyHttpReader.shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO 自動生成されたメソッド・スタブ
        if(DEBUG) Log.d("MySurfaceView","surfaceChanged() :#1 called!!");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO 自動生成されたメソッド・スタブ
        if(DEBUG) Log.d("MySurfaceView","surfaceCreated() :#1 called!!");
        isLoop = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO 自動生成されたメソッド・スタブ
        if(DEBUG) Log.d("MySurfaceView","surfaceDestroyed() :#1 called!!");
        // require my thread stop
        isLoop = false;
        try {
            // wait for my thread stop
            mThread.join();
        }
        catch (InterruptedException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }


    public void savePic(){

        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/capture/cap" + bitmun +".jpeg");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/Pictures/capture/cap.jpeg");

        bitmun++;


        FileOutputStream fos = null;
        FileOutputStream fos2 = null;
        try {
            fos = new FileOutputStream(file, false);
            fos2 = new FileOutputStream(file2, false);
            // 画像のフォーマットと画質と出力先を指定して保存
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos2);

            Log.d("Log", "save");
            fos.flush();
            fos2.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos2.close();
                } catch (IOException ie) {
                    fos = null;
                }
            }
        }
    }

}

