package com.qbase.skipper.q_base;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    Spinner users;
    BufferedInputStream is;
    String  username;
    ArrayList<String> listitem = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_redactor);

        user = (TextView) findViewById(R.id.user);

        user.setText(getIntent().getExtras().getString("usr"));

        stock = (EditText)findViewById(R.id.inst_table);

        barcode = (TextView) findViewById(R.id.barcode);

        button = (Button)findViewById(R.id.edit_table);

        barcode.setText(getIntent().getExtras().getString("dnum"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData();
                InsertData(TempName, TempStock, TempBarcode, TempStatus);
                Intent bs = new Intent(table_redactor.this, barcode_scan.class);
                startActivity(bs);
                finish();

            }
        });
        users  = findViewById(R.id.users);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_users_lay, R.id.txt, listitem);
        users.setAdapter(adapter);


    }
    protected void onStart(){
        super.onStart();
        SpinnerUsers su = new SpinnerUsers();
        su.execute();
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
    public void GetData(){
        TempStatus = "wait";
        TempName = users.getSelectedItem().toString();
        TempStock = stock.getText().toString();
        Toast.makeText(table_redactor.this, users.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        TempBarcode = barcode.getText().toString();
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
