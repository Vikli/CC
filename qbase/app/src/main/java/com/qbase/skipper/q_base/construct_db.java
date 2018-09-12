package com.qbase.skipper.q_base;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class construct_db extends AppCompatActivity {
    String ServerURL = "http://qbase.info/create_product.php" ;

    EditText name, email, author, stock;
    Button button;
    TextView user;
    String TempName, TempEmail, TempStock, TempAuthor, username ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construct_db);
        username = getIntent().getExtras().getString("usr");
        name = (EditText)findViewById(R.id.inputName);
        email = (EditText)findViewById(R.id.inputPrice);
        author = (EditText)findViewById(R.id.Author);
        stock = (EditText)findViewById(R.id.inputDesc);

        user = (TextView) findViewById(R.id.user);

        user.setText(username);

        button = (Button)findViewById(R.id.btnSave);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();

                InsertData(TempName, TempEmail, TempStock, TempAuthor );

            }
        });

    }
    public void GetData(){

        TempName = name.getText().toString();
        TempEmail = email.getText().toString();
        TempStock = stock.getText().toString();
        TempAuthor = author.getText().toString();

    }

    public void InsertData(final String name, final String email ,final String stock,  final String author){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String EmailHolder = email ;
                String stockHolder = stock ;
                String authorHolder = author ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("stock", stockHolder));
                nameValuePairs.add(new BasicNameValuePair("Author", authorHolder));

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