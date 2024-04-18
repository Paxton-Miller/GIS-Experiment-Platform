package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidapp.DB_CRUD.StudentDao;
import com.example.androidapp.DB_CRUD.StudentInfo;
import com.example.androidapp.DB_CRUD.TeacherDao;
import com.example.androidapp.DB_CRUD.TeacherInfo;
import com.example.androidapp.teacher.BottomNavActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtId;
    private TextView txtPw;
    private Button btnStudentLogin;
    private Button btnTeacherLogin;
    private StudentDao studentDao;
    private TeacherDao teacherDao;
    private Handler mainHandler; //主线程


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //changed
        initView();
//        Intent intent = new Intent(this, com.example.androidapp.student.BottomNavActivity.class);
//        startActivity(intent);
    }

    private void initView() {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        studentDao = new StudentDao();
        teacherDao = new TeacherDao();
        mainHandler = new Handler(getMainLooper());
        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);
        btnStudentLogin = findViewById(R.id.btnStudentLogin);
        btnTeacherLogin = findViewById(R.id.btnTeacherLogin);
        btnStudentLogin.setOnClickListener(this);
        btnTeacherLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStudentLogin:
                doStudentLogin();
                break;
            case R.id.btnTeacherLogin:
                doTeacherLogin();
                break;
            default:
                break;
        }
    }

    private void doStudentLogin() {
        final String sid = txtId.getText().toString().trim();
        final String spassword = txtPw.getText().toString().trim();
        if (TextUtils.isEmpty(sid)) {
            CommonUtils.showShortMsg(this, "请输入账号");
            txtId.requestFocus();
        } else if (TextUtils.isEmpty(spassword)) {
            CommonUtils.showShortMsg(this, "请输入密码");
            txtPw.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    final StudentInfo item;
                    if (studentDao.getStudentBySid(sid) == null) //检验是否有此用户账号
                        item = null;
                    else if (CommonUtils.checkPw(spassword, studentDao.getStudentBySid(sid).getSpassword())) //检验用户输入密码是否与数据库存储的加密密码一致
                        item = studentDao.getStudentBySid(sid);
                    else item = null; //不一致的情况
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (item == null)
                                CommonUtils.showShortMsg(LoginActivity.this, "用户名或密码错误");
                            else {
                                CommonUtils.showShortMsg(LoginActivity.this, "登录成功");
                                // waiting to be changed
                                ((ApplicationTrans) getApplication()).setRole("学生");
                                ((ApplicationTrans) getApplication()).setSname(item.getSname());
                                ((ApplicationTrans) getApplication()).setSgender(item.getSgender());
                                ((ApplicationTrans) getApplication()).setCname(item.getCname());
//                                CommonUtils.showDialogMsg(LoginActivity.this, "登录成功");
                                Intent intent = new Intent(LoginActivity.this, com.example.androidapp.student.BottomNavActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }).start(); //开启线程
        }
    }

    private void doTeacherLogin() {
        final String tid = txtId.getText().toString().trim();
        final String tpassword = txtPw.getText().toString().trim();
        if (TextUtils.isEmpty(tid)) {
            CommonUtils.showShortMsg(this, "请输入账号");
            txtId.requestFocus();
        } else if (TextUtils.isEmpty(tpassword)) {
            CommonUtils.showShortMsg(this, "请输入密码");
            txtPw.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    final TeacherInfo item; //暂时设为StudentInfo
                    if (teacherDao.getTeacherByTid(tid) == null) //检验是否有此用户账号
                        item = null;
                    else if (CommonUtils.checkPw(tpassword, teacherDao.getTeacherByTid(tid).getTpassword())) //检验用户输入密码是否与数据库存储的加密密码一致
                        item = teacherDao.getTeacherByTid(tid);
                    else item = null; //不一致的情况
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (item == null)
                                CommonUtils.showShortMsg(LoginActivity.this, "用户名或密码错误");
                            else {
                                CommonUtils.showShortMsg(LoginActivity.this, "登录成功");
                                // waiting to be changed
                                ((ApplicationTrans) getApplication()).setRole("教师");
                                ((ApplicationTrans) getApplication()).setTid(item.getTid());
                                ((ApplicationTrans) getApplication()).setTname(item.getTname());
                                ((ApplicationTrans) getApplication()).setTgender(item.getTgender());
//                                CommonUtils.showDialogMsg(MainActivity.this, "登录成功");
                                Intent intent = new Intent(LoginActivity.this, BottomNavActivity.class);
                                startActivity(intent);

                            }
                        }
                    });
                }
            }).start(); //开启线程
        }
    }
}