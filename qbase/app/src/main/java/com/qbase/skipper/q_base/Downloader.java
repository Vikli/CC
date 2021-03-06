package com.qbase.skipper.q_base;

import android.annotation.SuppressLint;
import android.util.Log;
import com.google.android.gms.ads.internal.gmsg.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Downloader extends Thread {
    String urladdress = "http://qbase.info/get_fio.php";
    String names;
    String stocks;
    String titles;
    String barcodes;
    InputStream is = null;
    String line = null;
    String result = null;


    public void run(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("barcodes", barcodes));


        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urladdress);
            httpPost.setEntity (new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pas1","connection success");
        }catch (Exception e){
            Log.e("Fail 1",e.toString());
        }
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"),8);
        StringBuilder sb = new StringBuilder();
        while((line = reader.readLine())!=null){
            sb.append(line + "\n");
        }
        is.close();
        result = sb.toString();
        Log.e("pass2","connection success" + result);
        } catch (Exception e) {
            Log.e("Fail 2",e.toString());
        }

        try{
            JSONObject json_data = new JSONObject(result);
            stocks = (json_data.getString("Stock"));
            names = (json_data.getString("Name"));
            titles = (json_data.getString("Title"));
            Log.e("pass 3", names);
        }catch(Exception e){
            Log.e("Fail 3",e.toString());
        }


    }
    public void start(String dn)
    {
        this.barcodes = dn;
        this.start();
    }
    public String restitle(){
        return titles;
    }
    public String resname(){
        return names;
    }
    public String resstock(){
        return stocks;
    }
}
