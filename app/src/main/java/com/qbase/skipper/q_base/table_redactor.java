package com.qbase.skipper.q_base;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class table_redactor extends AppCompatActivity {
    String ServerURL = "http://qbase.info/update_product.php" ;
    EditText name, stock;
    Button button;
    String TempName, TempStock,TempBarcode,TempStatus ;
    TextView barcode,user;
    Spinner users, barcodesspin, stocks;
    BufferedInputStream is;
    String  username, bn;
    ArrayList<String> listitem = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> listitemb = new ArrayList<>();
    ArrayAdapter<String> adapterb;
    ArrayList<String> listitems = new ArrayList<>();
    ArrayAdapter<String> adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_redactor);

        user = findViewById(R.id.user);

        user.setText(getIntent().getExtras().getString("usr"));

        username = user.getText().toString().replace(" ","");

        barcode = findViewById(R.id.barcode);

        button = findViewById(R.id.edit_table);

        barcode.setText(getIntent().getExtras().getString("dnum"));


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( barcodesspin.getSelectedItem().toString().equals(barcode.getText().toString())) {
                            GetData();
                            InsertData(TempName, TempStock, TempBarcode, TempStatus);
                            Intent bs = new Intent(table_redactor.this, barcode_scan.class);
                            startActivity(bs);
                            finish();
                        }
                    }
                });
        users  = findViewById(R.id.users);
        barcodesspin = findViewById(R.id.barcodesspin);
        stocks = findViewById(R.id.stock);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_users_lay, R.id.txt, listitem);
        users.setAdapter(adapter);
        adapterb = new ArrayAdapter<String>(this, R.layout.spinner_barcodes_lay, R.id.txt, listitemb);
        barcodesspin.setAdapter(adapterb);
        adapters = new ArrayAdapter<String>(this, R.layout.spinner_stocks_lay, R.id.txt, listitems);
        stocks.setAdapter(adapters);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bn = data.getStringExtra("decode_num");
        barcode.setText(bn);
        barcodesspin.setSelection(adapterb.getPosition(bn));
    }
    protected void onStart(){
        super.onStart();
        SpinnerUsers su = new SpinnerUsers();
        su.execute();
        SpinnerBarcodes sb = new SpinnerBarcodes();
        sb.execute();
        SpinnerStock ss = new SpinnerStock();
        ss.execute();
    }

    public  void Scan(View view){
        Intent scan_screen = new Intent(this, FullscreenActivity.class);
        final int result = 1;
        scan_screen.putExtra("callingActivity", "MainActivity");
        startActivityForResult(scan_screen,result);

    }

    private class SpinnerUsers extends  AsyncTask<Void, Void, Void>{
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list = new ArrayList<>();

        }
        protected Void doInBackground(Void ... params){
            InputStream is =null;
            String result = "";
            try{
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost("http://qbase.info/database/get_users.php");

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                BufferedReader br =  new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line ="";
                while((line = br.readLine())!=null){
                    result += line;
                }
                is.close();

            }catch (IOException e ){
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list.add(jsonObject.getString("Username"));
                }
            }catch (JSONException jse){
                jse.printStackTrace();
            }return null;

        }
        protected void onPostExecute(Void result){
            listitem.addAll(list);
            adapter.notifyDataSetChanged();
        }

    }
    private class SpinnerStock extends  AsyncTask<Void, Void, Void>{
        ArrayList<String> lists;
        protected void onPreExecute(){
            super.onPreExecute();
            lists = new ArrayList<>();
        }
        protected Void doInBackground(Void ... params){
            InputStream is =null;
            String result = "";
            try{
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost("http://qbase.info/database/get_stocks.php");

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                BufferedReader br =  new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line ="";
                while((line = br.readLine())!=null){
                    result += line;
                }
                is.close();

            }catch (IOException e ){
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    lists.add(jsonObject.getString("Title"));
                }
            }catch (JSONException jse){
                jse.printStackTrace();
            }return null;

        }
        protected void onPostExecute(Void result){
            listitems.addAll(lists);
            adapters.notifyDataSetChanged();
        }

    }

    private class SpinnerBarcodes extends  AsyncTask<Void, Void, Void>{
        ArrayList<String> listb;
        protected void onPreExecute(){
            super.onPreExecute();
            listb = new ArrayList<>();
        }
        protected Void doInBackground(Void ... params){
            InputStream isb =null;
            String resultb = "";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("Name", username));

            try{
                HttpClient httpClientb = new DefaultHttpClient();

                HttpPost httpPostb = new HttpPost("http://qbase.info/database/get_barcodes.php");

                httpPostb.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

                HttpResponse httpResponseb = httpClientb.execute(httpPostb);

                HttpEntity httpEntityb = httpResponseb.getEntity();

                isb = httpEntityb.getContent();

            }catch (Exception eb){
                eb.printStackTrace();
            }
            try {
                BufferedReader brb =  new BufferedReader(new InputStreamReader(isb, "UTF-8"));
                String lineb ="";
                while((lineb = brb.readLine())!=null){
                    resultb += lineb;
                }
                isb.close();

            }catch (IOException e ){
                e.printStackTrace();
            }

            try {
                JSONArray jsonArrayb = new JSONArray(resultb);
                for(int i = 0; i<jsonArrayb.length(); i++){
                    JSONObject jsonObjectb = jsonArrayb.getJSONObject(i);
                    listb.add(jsonObjectb.getString("Barcode"));
                }
            }catch (JSONException jse){
                jse.printStackTrace();
            }return null;

        }
        protected void onPostExecute(Void resultb){
            listitemb.addAll(listb);
            adapterb.notifyDataSetChanged();
        }


    }


    public void GetData(){
        TempStatus = "wait";
        TempName = users.getSelectedItem().toString();
        TempStock = stocks.getSelectedItem().toString();
        TempBarcode = bn;
    }

    public void InsertData(final String name,final String stock, final String barcode, final String status){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String stockHolder = stock ;
                String BarcodeHolder = barcode ;
                String StatusHolder = status;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("Name", NameHolder));

                nameValuePairs.add(new BasicNameValuePair("Stock", stockHolder));

                nameValuePairs.add(new BasicNameValuePair("Barcode", BarcodeHolder));

                nameValuePairs.add(new BasicNameValuePair("Status", StatusHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));


                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(table_redactor.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, stock, status);



    }
    }
