package com.example.androidapp.teacher.teacherFragments.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.ClassDao;
import com.example.androidapp.DB_CRUD.ClassInfo;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.DB_CRUD.StudentDao;
import com.example.androidapp.R;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class NotificationAddActivity extends AppCompatActivity implements View.OnClickListener {
    //声明控件
    private EditText txtRname, txtRcontent;
    private TextView txtRtime_date, txtRtime_time;
    private int Year, Month, Day, Hour, Minute; //记录年月日时分
    private String[] classList; //获取班级列表
    private Handler mainHandler; //主线程
    private NotificationDao notificationDao; //数据库操作类
    private Spinner spinnerCname; //下拉列表选择框
    private ArrayAdapter<String> spinnerAdapter = null; //自定义适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);
        initView();
    }

    private void initView() {
        //初始化控件
        spinnerCname = findViewById(R.id.spinnerCname);
        txtRname = findViewById(R.id.txtRname);
        txtRtime_date = findViewById(R.id.txtRtime_date);
        txtRtime_date.setOnClickListener(this);
        txtRtime_time = findViewById(R.id.txtRtime_time);
        txtRtime_time.setOnClickListener(this);
        txtRcontent = findViewById(R.id.txtRcontent);
        mainHandler = new Handler(getMainLooper());
        notificationDao = new NotificationDao(); //数据库操作类
        //开启新线程查询全部班级列表，加载到下拉选择框spinner中
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取班级列表
                final List<ClassInfo> classInfoList = new ClassDao().getClassList();
                classList = new String[classInfoList.size()];
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < classInfoList.size(); i++) {
                            classList[i] = classInfoList.get(i).getCname();
                        }
                        //加载下拉框内容
                        spinnerAdapter = new ArrayAdapter<String>(NotificationAddActivity.this, R.layout.spinner_item, R.id.spinnerItem, classList);
                        spinnerCname.setAdapter(spinnerAdapter);
                        spinnerCname.setPrompt("班级列表");
                        spinnerCname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//                String[] cityStrs = getResources().getStringArray(R.array.cities);
//                Log.i(TAG, "onItemSelected pos:" + pos);
                                CommonUtils.showShortMsg(NotificationAddActivity.this, classList[pos]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                });
            }
        }).start();


    }

    public void btnAddClick(View view) {
        final String cname = spinnerCname.getSelectedItem().toString().trim();
        final String rname = txtRname.getText().toString().trim();
        final String rtime = Integer.toString(Year) + "-" + Integer.toString(Month) + "-" + Integer.toString(Day) + " " + Integer.toString(Hour) + ":" + Integer.toString(Minute) + ":00";
        final String rcontent = txtRcontent.getText().toString().trim();

        if (TextUtils.isEmpty(cname) || TextUtils.isEmpty(rname) || TextUtils.isEmpty(rcontent) || Year == 0) {
            CommonUtils.showShortMsg(this, "请输入完整信息");
//            txtSid.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    final Notification item;

                    item = new Notification();
                    item.setCname(cname);
                    item.setRname(rname);
                    item.setRtime(rtime);
                    item.setRcontent(rcontent);

                    item.setCid(new ClassDao().getClassByCname(cname).getCid()); //temporary code
                    item.setTname(((ApplicationTrans) getApplication()).getTname()); //temporary code

                    notificationDao.addNotification(item);

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showShortMsg(NotificationAddActivity.this, "添加成功");
//                                CommonUtils.showDialogMsg(MainActivity.this, "登录成功");
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
            case R.id.txtRtime_date:
                showDatePickerDialog();
                break;
            case R.id.txtRtime_time:
                showTimePickerDialog();
                break;
            default:
                break;
        }
    }


    @SuppressLint("ResourceType")
    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String str = year + "年" + month + "月" + day + "日";//把日期变成字符串格式显示出来
                Year = year;
                Month = month;
                Day = day;
                txtRtime_date.setText(str);//文本框显示的内容设置成经过逻辑处理后的日期
            }
        };
//        Date date = new Date("2015/07/08 00:00:01");//当前时间
//        datePicker.setMaxDate(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(NotificationAddActivity.this, 2, onDateSetListener, 2022, 5, 1);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime()); //当前时间
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String str = hour + "时" + minute + "分";
                Hour = hour;
                Minute = minute;
                txtRtime_time.setText(str);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(NotificationAddActivity.this, 2, onTimeSetListener, 00, 00, true);
        timePickerDialog.show();
    }
}