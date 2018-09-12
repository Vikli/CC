package com.qbase.skipper.q_base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Main extends AppCompatActivity {
    String username = "Guest";
    TextView user;
    Button btn, btn_help, btn_add, inst_btn, search_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.user);
        user.setText(getIntent().getExtras().getString("username"));

        btn = findViewById(R.id.scan_btn);
        btn_add = findViewById(R.id.add_btn);
        inst_btn = findViewById(R.id.inst_btn);
        search_btn = findViewById(R.id.move_btn);
        btn_help = findViewById(R.id.button2);

        Intent t_r = new Intent(Main.this,table_redactor.class);
        t_r.putExtra("usr", username);


        CheckPermission();

        username = user.getText().toString().replace(" ","");
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = user.getText().toString().replace(" ","");
                Intent b_s = new Intent(Main.this,give_or_pick.class);
                b_s.putExtra("usr", username);
                startActivity(b_s);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                Intent db_r = new Intent(Main.this,construct_db.class);
                db_r.putExtra("usr", username);
                startActivity(db_r);
            }
        });

        // btn exit
        ImageView exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                startActivity(new Intent(Main.this, profile_act.class));
            }
        });

        // btn instrument
        inst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = getIntent().getExtras().getString("username");
                Intent ins_r = new Intent(Main.this,inst_db.class);
                ins_r.putExtra("usr",username );
                startActivity(ins_r);
           }
        });

        // btn add
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, user_authorization.class));
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                Intent b_r = new Intent(Main.this, search_by_code.class);
                b_r.putExtra("usr", username);
                startActivity(b_r);
            }
        });

    }
public void CheckPermission(){
    String using = user.getText().toString().replace(" ","");
        if(using.equals("user")){
        Toast.makeText(Main.this, using.toString(), Toast.LENGTH_LONG).show();
        btn.setEnabled(true);
        search_btn.setEnabled(true);
        inst_btn.setEnabled(true);
        btn_add.setEnabled(true);
        }
    if (!using.equals("user")){
            btn.setEnabled(false);
        search_btn.setEnabled(false);
        inst_btn.setEnabled(false);
        btn_add.setEnabled(false);
    }
    if (!using.equals("admin")){
        //inst_btn.setEnabled(false);
       // btn_add.setEnabled(false);
    }
}
}


