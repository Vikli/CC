package com.qbase.skipper.q_base;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class inst_db extends AppCompatActivity {
    String urladdress="http://qbase.info/cc_qbase.php";
    String[] name, barcode, email, status;
    ListView listView;
    BufferedInputStream is;
    String line=null, username;
    String result=null;
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_db);

        user = (TextView) findViewById(R.id.user);

        listView=(ListView)findViewById(R.id.inst_db_lv);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
            CustomListView customListView=new CustomListView(this, name, email, barcode, status);
        listView.setAdapter(customListView);
        username = (getIntent().getExtras().getString("usr"));
        user.setText(username);

    }
    private void collectData() {

        try {
            URL url = new URL(urladdress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //content
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
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
            name = new String[ja.length()];
            email = new String[ja.length()];
            barcode = new String[ja.length()];
            status = new String[ja.length()];
            // imagepath=new String[ja.length()];


            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                name[i] = jo.getString("Title");
                email[i] = jo.getString("Name");
                barcode[i] = jo.getString("Barcode");
                status[i] = jo.getString("stat");
                //imagepath[i]=jo.getString("photo");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
