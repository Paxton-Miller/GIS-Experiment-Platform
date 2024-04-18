package com.example.androidapp.teacher.teacherFragments.assignmanage;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.ClassDao;
import com.example.androidapp.DB_CRUD.ClassInfo;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.DB_CRUD.PublishExperiments;
import com.example.androidapp.DB_CRUD.PublishExperimentsDao;
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

public class AssignmanageEditActivity extends AppCompatActivity implements View.OnClickListener {
    private PublishExperiments item; //要修改的通知对象
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute; //记录picker控件年月日时分
    //声明控件
    private EditText txtPbname;
    private TextView txtPbstarttime_date, txtPbstarttime_time, txtPbendtime_date, txtPbendtime_time;
    private Handler mainHandler; //主线程
    private PublishExperimentsDao publishExperimentsDao; //数据库操作类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignmanage_edit);
        initView();
    }

    private void initView() {
        //初始化控件
        txtPbname = findViewById(R.id.txtPbname);
        txtPbname = findViewById(R.id.txtPbname);
        txtPbstarttime_date = findViewById(R.id.txtPbstarttime_date);
        txtPbstarttime_time = findViewById(R.id.txtPbstarttime_time);
        txtPbendtime_date = findViewById(R.id.txtPbendtime_date);
        txtPbendtime_time = findViewById(R.id.txtPbendtime_time);
        txtPbstarttime_date.setOnClickListener(this);
        txtPbstarttime_time.setOnClickListener(this);
        txtPbendtime_date.setOnClickListener(this);
        txtPbendtime_time.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras(); //接收上一个Activity的数据
        if (bundle != null) {
            item = (PublishExperiments) bundle.getSerializable("item");
            //初始化通知对象item
            txtPbname.setText(item.getPbname());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(item.getPbstarttime());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            txtPbstarttime_date.setText(Integer.toString(date.getYear() + 1900) + "年" + Integer.toString(date.getMonth() + 1) + "月" + Integer.toString(date.getDate()) + "日");
            txtPbstarttime_time.setText(Integer.toString(date.getHours()) + "时" + Integer.toString(date.getMinutes()) + "分");
            startYear = date.getYear() + 1900;
            startMonth = date.getMonth() + 1;
            startDay = date.getDate(); //getDay的得到的是星期几
            startHour = date.getHours();
            startMinute = date.getMinutes();
            try {
                date = format.parse(item.getPbendtime());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            txtPbendtime_date.setText(Integer.toString(date.getYear() + 1900) + "年" + Integer.toString(date.getMonth() + 1) + "月" + Integer.toString(date.getDate()) + "日");
            txtPbendtime_time.setText(Integer.toString(date.getHours()) + "时" + Integer.toString(date.getMinutes()) + "分");
            endYear = date.getYear() + 1900;
            endMonth = date.getMonth() + 1;
            endDay = date.getDate(); //getDay的得到的是星期几
            endHour = date.getHours();
            endMinute = date.getMinutes();
        }
        mainHandler = new Handler(getMainLooper());
        publishExperimentsDao = new PublishExperimentsDao();
    }

    public void btnEditClick(View view) {
        final String pbname = txtPbname.getText().toString().trim();
        final String pbstarttime = Integer.toString(startYear) + "-" + Integer.toString(startMonth) + "-" + Integer.toString(startDay) + " " + Integer.toString(startHour) + ":" + Integer.toString(startMinute) + ":00";
        final String pbendtime = Integer.toString(endYear) + "-" + Integer.toString(endMonth) + "-" + Integer.toString(endDay) + " " + Integer.toString(endHour) + ":" + Integer.toString(endMinute) + ":00";

        if (TextUtils.isEmpty(pbname)) {
            CommonUtils.showShortMsg(this, "请输入完整信息");
//            txtSid.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    String pbid = item.getPbid();
                    final PublishExperiments item;
                    item = new PublishExperiments();
                    item.setPbid(pbid);
                    item.setPbname(pbname);
                    item.setPbstarttime(pbstarttime);
                    item.setPbendtime(pbendtime);
                    publishExperimentsDao.editPublishExperiments(item);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showShortMsg(AssignmanageEditActivity.this, "修改成功");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtPbstarttime_date:
                showStartDatePickerDialog();
                break;
            case R.id.txtPbstarttime_time:
                showStartTimePickerDialog();
                break;
            case R.id.txtPbendtime_date:
                showEndDatePickerDialog();
                break;
            case R.id.txtPbendtime_time:
                showEndTimePickerDialog();
                break;
            default:
                break;
        }
    }


    @SuppressLint("ResourceType")
    private void showStartDatePickerDialog() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String str = year + "年" + month + "月" + day + "日";//把日期变成字符串格式显示出来
                startYear = year;
                startMonth = month;
                startDay = day;
                txtPbstarttime_date.setText(str);//文本框显示的内容设置成经过逻辑处理后的日期
            }
        };
//        Date date = new Date("2015/07/08 00:00:01");//当前时间
//        datePicker.setMaxDate(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(AssignmanageEditActivity.this, 2, onDateSetListener, 2022, 5, 1);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime()); //当前时间
        datePickerDialog.show();
    }

    private void showStartTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String str = hour + "时" + minute + "分";
                startHour = hour;
                startMinute = minute;
                txtPbstarttime_time.setText(str);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(AssignmanageEditActivity.this, 2, onTimeSetListener, 00, 00, true);
        timePickerDialog.show();
    }

    @SuppressLint("ResourceType")
    private void showEndDatePickerDialog() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String str = year + "年" + month + "月" + day + "日";//把日期变成字符串格式显示出来
                endYear = year;
                endMonth = month;
                endDay = day;
                txtPbendtime_date.setText(str);//文本框显示的内容设置成经过逻辑处理后的日期
            }
        };
//        Date date = new Date("2015/07/08 00:00:01");//当前时间
//        datePicker.setMaxDate(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(AssignmanageEditActivity.this, 2, onDateSetListener, 2022, 5, 1);
        datePickerDialog.getDatePicker().setMinDate(new Date(startYear + "/" + startMonth + "/" + startDay + " 00:00:01").getTime()); //当前时间
        datePickerDialog.show();
    }

    private void showEndTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String str = hour + "时" + minute + "分";
                endHour = hour;
                endMinute = minute;
                txtPbendtime_time.setText(str);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(AssignmanageEditActivity.this, 2, onTimeSetListener, 00, 00, true);
        timePickerDialog.show();
    }
}