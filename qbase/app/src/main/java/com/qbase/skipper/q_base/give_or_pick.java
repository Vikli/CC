package com.qbase.skipper.q_base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.internal.overlay.zzo;

public class give_or_pick extends AppCompatActivity {

    Button giv, pic;
    TextView user;
    String username = "Guest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_or_pick);


        pic = findViewById(R.id.pic);
        giv = findViewById(R.id.giv);
        user = findViewById(R.id.user);
        user.setText(getIntent().getExtras().getString("usr"));
        username = getIntent().getExtras().getString("usr");

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent givin = new Intent(give_or_pick.this, give.class);
                givin.putExtra("usr", username);
                startActivity(givin);


            }
        });

        giv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picin = new Intent(give_or_pick.this,barcode_scan.class);
                picin.putExtra("usr", username);
                startActivity(picin);
            }
        });

    }

}
