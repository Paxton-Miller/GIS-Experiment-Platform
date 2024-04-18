package com.example.androidapp.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.LoginActivity;
import com.example.androidapp.R;

public class StudentHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnWeb, btnExit;
    private TextView txtTname, txtTgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        txtTgender = (TextView) findViewById(R.id.txtTgender);
        txtTname = (TextView) findViewById(R.id.txtTname);
        txtTname.setText(((ApplicationTrans) getApplication()).getTname());
        txtTgender.setText(((ApplicationTrans) getApplication()).getTgender());
        btnWeb = (ImageView) findViewById(R.id.btnWeb);
        btnWeb.setOnClickListener(this);
        btnExit = (ImageView) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWeb:
                Uri uri = Uri.parse("http://47.115.226.172/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.btnExit:
                finish();
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
            default:
                break;
        }
    }
}