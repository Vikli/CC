package com.qbase.skipper.q_base;

import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class table_redactor extends AppCompatActivity {
    String ServerURL = "http://qbase.info/update_product.php" ;
    EditText name, stock;
    Button button;
    String TempName, TempStock,TempBarcode,TempStatus ;
    TextView barcode,user;
    Spinner users;
    String [] names;
    BufferedInputStream is;
    String line=null, username;
    String result=null;
    private static final String LOGIN_URL = "http://qbase.info/update_product.php";
    private JSONArray jArray = null;

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




        SpinnerUsers();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData();
                InsertData(TempName, TempStock, TempBarcode, TempStatus);

            }
        });

    }
    public void GetData(){
        TempStatus = "approve";
        TempName = name.getText().toString();
        TempStock = stock.getText().toString();
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
    public void SpinnerUsers() {
        try {
            URL url = new URL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //content
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();

        }

//JSON
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            names = new String[ja.length()];

            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                names[i] = jo.getString("Title");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    }
