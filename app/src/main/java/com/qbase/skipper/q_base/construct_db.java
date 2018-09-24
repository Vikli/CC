package com.qbase.skipper.q_base;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class construct_db extends AppCompatActivity {
    String ServerURL = "http://qbase.info/create_product.php" ;

    EditText name, email, author, stock;
    Button button;
    TextView user;
    String TempName, TempEmail, TempStock, TempAuthor, username ;
    ArrayList<String> listitem = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner users, stocks;
    private String TempStatus;
    ArrayList<String> listitems = new ArrayList<>();
    ArrayAdapter<String> adapters;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construct_db);
        username = getIntent().getExtras().getString("usr");
        name = findViewById(R.id.inputName);
        email = findViewById(R.id.inputPrice);
        stocks = findViewById(R.id.stock2);

        user = findViewById(R.id.user);

        user.setText(username);

        button = findViewById(R.id.btnSave);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();

                InsertData(TempName, TempEmail, TempStock, TempAuthor, TempStatus );

            }
        });
        users  = findViewById(R.id.users);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_users_lay, R.id.txt, listitem);
        users.setAdapter(adapter);
        adapters = new ArrayAdapter<String>(this, R.layout.spinner_stocks_lay, R.id.txt, listitems);
        stocks.setAdapter(adapters);
    }
    protected void onStart(){
        super.onStart();
        construct_db.SpinnerUsers su = new construct_db.SpinnerUsers();
        su.execute();
        construct_db.SpinnerStock ss = new  construct_db.SpinnerStock();
        ss.execute();
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

    public void GetData(){
        TempStatus = "wait";
        TempName = name.getText().toString();
        TempEmail = email.getText().toString();
        TempStock = stocks.getSelectedItem().toString();
        TempAuthor = users.getSelectedItem().toString();

    }

    public void InsertData(final String name, final String email ,final String stock,  final String author, final String status){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String StatusHolder = status;
                String NameHolder = name ;
                String EmailHolder = email ;
                String stockHolder = stock ;
                String authorHolder = author ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("stock", stockHolder));
                nameValuePairs.add(new BasicNameValuePair("Author", authorHolder));
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

                Toast.makeText(construct_db.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, email);
    }
}