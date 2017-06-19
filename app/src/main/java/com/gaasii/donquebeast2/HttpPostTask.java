package com.gaasii.donquebeast2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by negi on 2017/06/19.
 */

public class HttpPostTask extends AsyncTask<Integer, Void, Void> {

    private Activity parentActivity;
    private ProgressDialog dialog = null;

    //http://192.168.11.124/~pi/ledtest.php?num=3&stat=1
    private final String DEFAULTURL = "http://172.16.2.78:8000/cgi-bin/control.py?";
    private String uri = null;

    public HttpPostTask(Activity parentActivity){
        this.parentActivity = parentActivity;
    }


    @Override
    protected void onPreExecute(){
        //dialog = new ProgressDialog(parentActivity);
        //dialog.setMessage("connecting ...");
        //dialog.show();
    }

    @Override
    protected Void doInBackground(Integer... arg0){
        //uri = DEFAULTURL+"num="+arg0[0].toString()+"&stat="+arg0[1].toString();
        uri = DEFAULTURL+arg0[0].toString();
        Log.d("", uri);
        exec_get();
        return null;
    }


    @Override
    protected void onPostExecute(Void result){
        //dialog.dismiss();
    }


    private String exec_get() {
        HttpURLConnection http = null;
        InputStream in = null;
        String src = new String();
        try {
            URL url = new URL(uri);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();

            in = http.getInputStream();

            byte[] line = new byte[1024];
            int size;
            while (true) {
                size = in.read(line);
                if (size <= 0) {
                    break;
                }
                src += new String(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (http != null)
                    http.disconnect();

                if (in != null)
                    in.close();

            } catch (Exception e) {
            }
        }
        return src;

    }
}

