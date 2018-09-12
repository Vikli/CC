package com.qbase.skipper.q_base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

public class search_by_code extends AppCompatActivity {
    private Downloader downloader;
    TextView name,user,title,stock, bnnu;
    EditText barcode;
    String titles= "111", barcodes,stocks= "111",names ="111";
    Button scan_btn;
    String bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_code);

        user = findViewById(R.id.user);

        stock = findViewById(R.id.stock_tv);
        name = findViewById(R.id.name_tv);
        title =  findViewById(R.id.title_tv);
        barcode =  findViewById(R.id.decode_barcode);

        bnnu = findViewById(R.id.bnnu);

        scan_btn = findViewById(R.id.button4);
        user.setText(getIntent().getExtras().getString("usr"));


    }
    public  void Scan(View view){
        Intent scan_screen = new Intent(this, FullscreenActivity.class);
        final int result = 1;
        scan_screen.putExtra("callingActivity", "MainActivity");
        startActivityForResult(scan_screen,result);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bn = data.getStringExtra("decode_num");
        bnnu.setText(bn);
    }
    public void OnClick(View view){
        if (barcode.getText().length() != 0){
          bn = barcode.getText().toString().replace(" ","");
          bnnu.setText(bn);
        }
        barcodes = bn;
        downloader = new Downloader();
        downloader.start(barcodes);

        try{
            downloader.join();
        }catch (InterruptedException ie){
            Log.e("pass 0", ie.getMessage());
        }
        names = downloader.resname();
        stocks =  downloader.resstock();
        titles = downloader.restitle();

        name.setText(names);
        stock.setText(stocks);
        title.setText(titles);
    }

}

