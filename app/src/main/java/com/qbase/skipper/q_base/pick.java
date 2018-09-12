package com.qbase.skipper.q_base;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class pick extends AppCompatActivity {
    TextView name, user, title, stock, barcode;
    String titles = "111", barcodes, stocks = "111", names = "111", status, TempStatus, TempName, TempBarcode, bn="0";
    String ServerURL = "http://qbase.info/database/get_stat.php";
    private Downloader_pick downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        user = findViewById(R.id.user);
        barcode = findViewById(R.id.decode_barcode);
        stock = findViewById(R.id.stock_tv);
        name = findViewById(R.id.name_tv);
        title = findViewById(R.id.title_tv);
        user.setText(getIntent().getExtras().getString("usr"));
        bn = getIntent().getExtras().getString("dn");

    }


    public void Refresh(View view) {

        names = user.getText().toString();
        downloader = new Downloader_pick();
        downloader.start(names);

        try {
            downloader.join();
        } catch (InterruptedException ie) {
            Log.e("pass 0", ie.getMessage());
        }

        stocks = downloader.resstock();
        titles = downloader.restitle();
        barcodes = downloader.resbarcode();
        status = downloader.resstatus();

        name.setText(status);
        stock.setText(stocks);
        title.setText(titles);
        barcode.setText(barcodes);
    }

    public  void Scan(View view){
        Intent scan_screen = new Intent(this, FullscreenActivity.class);
        final int result = 1;
        scan_screen.putExtra("callingActivity", "MainActivity");
        startActivityForResult(scan_screen,result);

}

    public void Approve(View view) {
        if(barcodes.equals(bn)){
            GetData();
            InsertData(TempName, TempStatus, TempBarcode);
        }
    }

    public void GetData() {
        TempStatus = "approved";
        TempName = user.getText().toString();
        TempBarcode = barcode.getText().toString();
    }

    public void InsertData(final String name, final String status, final String bc) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name;
                String StatusHolder = status;
                String BCHolder = bc;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("Name", NameHolder));

                nameValuePairs.add(new BasicNameValuePair("Status", StatusHolder));

                nameValuePairs.add(new BasicNameValuePair("Barcode", BCHolder));

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

                Toast.makeText(pick.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, status, bc);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bn = data.getStringExtra("decode_num");

    }

}
