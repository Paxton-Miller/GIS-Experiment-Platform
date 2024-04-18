package com.example.androidapp.teacher.teacherFragments.exptmanage;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.ClassDao;
import com.example.androidapp.DB_CRUD.ClassInfo;
import com.example.androidapp.DB_CRUD.Experiment;
import com.example.androidapp.DB_CRUD.ExperimentDao;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.R;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExptmanageEditActivity extends AppCompatActivity {
    private Experiment item; //要修改的实验对象
    //声明控件
    private EditText txtEname, txtEsteps, txtEdesc;
    private Handler mainHandler; //主线程
    private ExperimentDao experimentDao; //数据库操作类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exptmanage_edit);
        initView();
    }

    private void initView() {
        //初始化控件
        txtEname = findViewById(R.id.txtEname);
        txtEsteps = findViewById(R.id.txtEsteps);
        txtEdesc = findViewById(R.id.txtEdesc);
        Bundle bundle = getIntent().getExtras(); //接收上一个Activity的数据
        if (bundle != null) {
            item = (Experiment) bundle.getSerializable("item");
            //初始化实验对象item
            txtEname.setText(item.getEname());
            txtEsteps.setText(item.getEsteps().replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "").replace(":", "").replace("step",""));
            txtEdesc.setText(item.getEdesc());
        }
        mainHandler = new Handler(getMainLooper());
        experimentDao = new ExperimentDao();
    }

    public void btnEditClick(View view) {
        final String ename = txtEname.getText().toString().trim();
        final String esteps = txtEsteps.getText().toString().trim();
        final String edesc = txtEdesc.getText().toString().trim();

        if (TextUtils.isEmpty(ename) || TextUtils.isEmpty(esteps) || TextUtils.isEmpty(edesc)) {
            CommonUtils.showShortMsg(this, "请输入完整信息");
//            txtSid.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    String eid = item.getEid();
                    final Experiment item;
                    item = new Experiment();
                    item.setEid(eid);
                    item.setEname(ename);
                    item.setEsteps(esteps);
                    item.setEdesc(edesc);
                    experimentDao.editExperiment(item);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showShortMsg(ExptmanageEditActivity.this, "修改成功");
                            setResult(1); //resultCode
                            finish();
//                                Intent intent = new Intent(StudentAddActivity.this, StudentManagerActivity.class);
//                                startActivity(intent);
                        }
                    });
                }
            }).start(); //开启线程
        }
    }

    public void btnCancelClick(View view) {
        finish();
    }

}