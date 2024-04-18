package com.example.androidapp.teacher.teacherFragments.assignfeedback;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.ClassDao;
import com.example.androidapp.DB_CRUD.ClassInfo;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.DB_CRUD.PerformExperiments;
import com.example.androidapp.DB_CRUD.PerformExperimentsDao;
import com.example.androidapp.DB_CRUD.PublishExperiments;
import com.example.androidapp.DB_CRUD.PublishExperimentsDao;
import com.example.androidapp.R;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
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

public class AssignfeedbackEditActivity extends AppCompatActivity {
    private PerformExperiments item; //要修改的做实验对象
    //声明控件
    private EditText txtPfgrade, txtPfcomment;
    private TextView txtSname, txtCname, txtPbname, txtPfresult;
    private Handler mainHandler; //主线程
    private PerformExperimentsDao performExperimentsDao; //数据库操作类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignfeedback_edit);
        initView();
    }

    private void initView() {
        //初始化控件
        txtSname = findViewById(R.id.txtSname);
        txtCname = findViewById(R.id.txtCname);
        txtPbname = findViewById(R.id.txtPbname);
        txtPfresult = findViewById(R.id.txtPfresult);
        txtPfgrade = findViewById(R.id.txtPfgrade);
        txtPfcomment = findViewById(R.id.txtPfcomment);
        Bundle bundle = getIntent().getExtras(); //接收上一个Activity的数据
        if (bundle != null) {
            item = (PerformExperiments) bundle.getSerializable("item");
            //初始化通知对象item
            txtSname.setText(item.getSname());
            txtCname.setText(item.getCname());
            txtPbname.setText(item.getPbname());
            //"https://assignmanage.oss-cn-nanjing.aliyuncs.com/" + item.getPfresult()
            txtPfresult.setText("点我加载该生附件");
            txtPfresult.setOnClickListener(this::txtPfresultClick);
            txtPfgrade.setText(item.getPfgrade());
            txtPfcomment.setText(item.getPfcomment());
        }
        mainHandler = new Handler(getMainLooper());
        performExperimentsDao = new PerformExperimentsDao();
    }

    public void btnEditClick(View view) {
        final String pfgrade = txtPfgrade.getText().toString().trim();
        final String pfcomment = txtPfcomment.getText().toString().trim();

        if (TextUtils.isEmpty(pfgrade)) {
            CommonUtils.showShortMsg(this, "请输入完整信息");
//            txtSid.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    String pfid = item.getPfid();
                    final PerformExperiments item;
                    item = new PerformExperiments();
                    item.setPfid(pfid);
                    item.setPfgrade(pfgrade);
                    item.setPfcomment(pfcomment);
                    performExperimentsDao.editPerformExperiments(item);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showShortMsg(AssignfeedbackEditActivity.this, "修改成功");
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

    public void txtPfresultClick(View view){
        Uri uri = Uri.parse("https://assignmanage.oss-cn-nanjing.aliyuncs.com/" + item.getPfresult());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void btnCancelClick(View view) {
        finish();
    }

}