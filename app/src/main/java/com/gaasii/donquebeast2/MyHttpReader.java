package com.gaasii.donquebeast2;

/**
 * Created by negi on 2017/06/18.
 */

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Base64;
import android.util.Log;

/*
 * サーバーのデータを読む
 * 注）必ず、別スレッドで実行します。
 */
public class MyHttpReader {
    String mUri,	// "/" , "index.html" , "/?action=snapshot"
            yid,		// "" or "your id"
            ypass;		// "" or "your password"
    String host,	// host ip -> "192.158.1.180"
            scheme,		// "http"
            domain;		// "" or virtual domain name
    int port;		// 80,8080 etc
    int rc;
    HttpEntity mHttpEntity;
    DefaultHttpClient mHttpClient;
    HttpHost mHttpHost;
    boolean shutdown_f,keep_alive_f;
    HttpRequest mHttpRequest;

    public MyHttpReader(){
        mUri="/?action=snapshot";	// get a jpeg data from mjpeg-streamer server with snapshot
        //mUri="/?action=stream&type=mjpg";	// multi-part -> not support
        yid="pi";
        //ypass="raspberry";
        ypass="Ta7ka26";
        //host="172.16.2.109";//GaaSii
        host="172.16.2.78";//KK
        port=9000;
        scheme="http";
        domain="";
        rc=0;
        mHttpEntity=null;
        keep_alive_f=true;
        initHttpClient();
    }
    /*
     * void initHttpClient()
     */
    public void initHttpClient(){
        mHttpHost = new HttpHost(host,port,scheme);
        // set shutdown to HttpClient status flag.
        shutdown_f=true;
        try {
            mHttpRequest = new DefaultHttpRequestFactory().newHttpRequest("GET",mUri);

            // Virtual Domain is required
            if(domain.equals("") != true){
                mHttpRequest.setHeader("Host", domain);
            }
            if(keep_alive_f == true){
                mHttpRequest.setHeader("Connection","Keep-Alive");
            }
            if(yid.equals("")!=true && ypass.equals("")!=true){
                mHttpRequest.setHeader("Authorization", "Basic "+Base64.encodeToString((yid+":"+ypass).getBytes(), Base64.DEFAULT));
            }
        }
        catch (MethodNotSupportedException e) {
            // TODO 自動生成された catch ブロック
            //e.printStackTrace();
            //Log.e("MyHttpReader","initHttpClient() #16:"+e.toString());
        }
    }
    /*
     *  get()
     */
    public void get(){
        try{
            mHttpEntity=null;
            //Log.d("MyHttpReader","get() #3: mUri="+mUri);
            // HttpClient is closed.
            if(shutdown_f == true){
                mHttpClient = new DefaultHttpClient();
                shutdown_f=false;
            }
            //Log.d("MyHttpReader","get() #5: mHttpHost.toURI()="+mHttpHost.toURI());
            HttpResponse response = mHttpClient.execute(mHttpHost,mHttpRequest);

            //Log.d("MyHttpReader","get() #6:passed!");
            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK){
                //Log.d("MyHttpReader","get() #7: data OK");
                mHttpEntity = response.getEntity();
                // レスポンスデータを文字列として取得する。
                // String data= EntityUtils.toString(response.getEntity(), "UTF-8");
                // byte[]として読み出したいときは EntityUtils.toByteArray()を使う。
                // byte[] data_b = EntityUtils.toByteArray(response.getEntity());
                rc=1;
            }
            //データ無し
            else if(status == HttpStatus.SC_NOT_FOUND){
                //Log.d("MyHttpReader","get() #8: data nothing");
                rc=2;
            }
            else{
                //Log.e("MyHttpReader","get() #9: error occured status code="+status);
                rc=-1;
            }
            //ここでshutdown()すると、上位で、mHttpEntity のアクセスが終了しない内に通信が切断されるので、
            //上位で行います。
            //又、HttpClientを使い回す場合は、適切なところで行うこと。
            //mHttpClient.getConnectionManager().shutdown();
        }
        catch(ClientProtocolException e){
            //Log.e("MyHttpReader","get() #14:"+e.toString());
            rc=-3;
        }
        catch(IOException e){
            //Log.e("MyHttpReader","get() #15:"+e.toString());
            rc=-4;
        }
    }
    /*
     * shutdown()
     */
    public void shutdown(){
        //Log.d("MyHttpReader","shutdown() #1:client shutdown");
        //HttpClientを使い回す場合は、上位が、 適切なところでコールします。
        //当然だがshutdown()したインスタンスは通信できなくなる。
        //プログラムの終了時には、必ずコールしてください。
        mHttpClient.getConnectionManager().shutdown();
        shutdown_f=true;
    }
}

