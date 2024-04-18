package com.example.androidapp.teacher.teacherFragments.studentmanage;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.ClassDao;
import com.example.androidapp.DB_CRUD.ClassInfo;
import com.example.androidapp.DB_CRUD.StudentDao;
import com.example.androidapp.DB_CRUD.StudentInfo;
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

public class StudentmanageEditActivity extends AppCompatActivity {
    private StudentInfo item; //要修改的通知对象
    private String[] classList; //获取班级列表
    //声明控件
    private EditText txtSname, txtSid, txtSphone, txtSemail, txtSlocation, txtSpassword;
    private Spinner spinnerCname, spinnerSgender;
    private ArrayAdapter<String> spinnerAdapterOfCname = null; //spinner适配器设置
    private ArrayAdapter<String> spinnerAdapterOfSgender = null; //spinner适配器设置
    private Handler mainHandler; //主线程
    private StudentDao studentDao; //数据库操作类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentmanage_edit);
        initView();
    }

    private void initView() {
        //初始化控件
        spinnerCname = findViewById(R.id.spinnerCname);
        spinnerSgender = findViewById(R.id.spinnerSgender);
        txtSid = findViewById(R.id.txtSid);
        txtSpassword = findViewById(R.id.txtSpassword);
        txtSname = findViewById(R.id.txtSname);
        txtSlocation = findViewById(R.id.txtSlocation);
        txtSphone = findViewById(R.id.txtSphone);
        txtSemail = findViewById(R.id.txtSemail);
        Bundle bundle = getIntent().getExtras(); //接收上一个Activity的数据
        if (bundle != null) {
            item = (StudentInfo) bundle.getSerializable("item");
            //初始化通知对象item
            txtSid.setText(item.getSid());
            txtSname.setText(item.getSname());

            txtSlocation.setText(item.getSlocation());
            txtSphone.setText(item.getSphone());
            txtSemail.setText(item.getSemail());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取classInfoList和classList
                    final List<ClassInfo> classInfoList;
                    classInfoList = new ClassDao().getClassList();
                    classList = new String[classInfoList.size()];
                    for (int i = 0; i < classInfoList.size(); i++) {
                        classList[i] = classInfoList.get(i).getCname();
                    }
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //绑定适配器和选中事件
                            spinnerAdapterOfCname = new ArrayAdapter<String>(StudentmanageEditActivity.this, R.layout.spinner_item, R.id.spinnerItem, classList);
                            spinnerCname.setAdapter(spinnerAdapterOfCname);
                            spinnerCname.setPrompt("班级列表");
                            spinnerCname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                                    CommonUtils.showShortMsg(StudentmanageEditActivity.this, classList[pos]);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                            //初始化通知对象item的Cname
                            for (int i = 0; i < classInfoList.size(); i++) {
                                if (classInfoList.get(i).getCname().equals(item.getCname())) { //不能用==判断字符串是否相等
                                    spinnerCname.setSelection(i, true);
                                }
                            }
                            spinnerAdapterOfSgender = new ArrayAdapter<String>(StudentmanageEditActivity.this, R.layout.spinner_item, R.id.spinnerItem, new String[]{"男", "女"});
                            spinnerSgender.setAdapter(spinnerAdapterOfSgender);
                            spinnerSgender.setPrompt("性别");
                            spinnerSgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                                    CommonUtils.showShortMsg(StudentmanageEditActivity.this, new String[]{"男", "女"}[pos]);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                            if (item.getSgender().equals("男"))
                                spinnerSgender.setSelection(0);
                            else
                                spinnerSgender.setSelection(1);
                        }
                    });
                }
            }).start();
        }
        mainHandler = new Handler(getMainLooper());
        studentDao = new StudentDao();
    }

    public void btnEditClick(View view) {
        final String sid = txtSid.getText().toString().trim();
        final String spassword = txtSpassword.getText().toString().trim();
        final String sname = txtSname.getText().toString().trim();
        final String sgender = spinnerSgender.getSelectedItem().toString().trim();
        final String slocation = txtSlocation.getText().toString().trim();
        final String sphone = txtSphone.getText().toString().trim();
        final String semail = txtSemail.getText().toString().trim();
        final String cname = spinnerCname.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(sid) || TextUtils.isEmpty(sname) || TextUtils.isEmpty(spassword) || TextUtils.isEmpty(sgender) || TextUtils.isEmpty(slocation) || TextUtils.isEmpty(sphone) || TextUtils.isEmpty(semail) || TextUtils.isEmpty(cname)) {
            CommonUtils.showShortMsg(this, "请输入完整信息");
//            txtSid.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    String sid = item.getSid();
                    String originalSpassword = item.getSpassword();
                    final StudentInfo item;
                    item = new StudentInfo();
                    item.setSid(sid);
                    item.setSname(sname);
                    if (originalSpassword.equals(spassword))
                        item.setSpassword(spassword);
                    else
                        item.setSpassword(CommonUtils.getBcryptHash(spassword));
                    item.setSgender(sgender);
                    item.setSlocation(slocation);
                    item.setSphone(sphone);
                    item.setSemail(semail);
                    item.setCname(cname);
                    item.setCid(new ClassDao().getClassByCname(cname).getCid()); //temporary code
                    studentDao.editStudent(item);

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            studentDao.editStudent(item);
                            CommonUtils.showShortMsg(StudentmanageEditActivity.this, "修改成功");
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