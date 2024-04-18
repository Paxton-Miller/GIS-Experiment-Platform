package com.example.androidapp.teacher.teacherFragments.notifications;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.ClassDao;
import com.example.androidapp.DB_CRUD.ClassInfo;
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

public class NotificationEditActivity extends AppCompatActivity implements View.OnClickListener {
    private Notification item; //要修改的通知对象
    private int Year, Month, Day, Hour, Minute; //记录picker控件年月日时分
    private String[] classList; //获取班级列表
    //声明控件
    private EditText txtRname, txtRcontent;
    private TextView txtRtime_date, txtRtime_time;
    private Spinner spinnerCname;
    private ArrayAdapter<String> spinnerAdapter = null; //spinner适配器设置
    private Handler mainHandler; //主线程
    private NotificationDao notificationDao; //数据库操作类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_edit);
        initView();
    }

    private void initView() {
        //初始化控件
        spinnerCname = findViewById(R.id.spinnerCname);
        txtRcontent = findViewById(R.id.txtRcontent);
        txtRname = findViewById(R.id.txtRname);
        txtRtime_date = findViewById(R.id.txtRtime_date);
        txtRtime_time = findViewById(R.id.txtRtime_time);
        //绑定点击事件
        txtRtime_date.setOnClickListener(this);
        txtRtime_time.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras(); //接收上一个Activity的数据
        if (bundle != null) {
            item = (Notification) bundle.getSerializable("item");
            //初始化通知对象item
            txtRname.setText(item.getRname());
            txtRcontent.setText(item.getRcontent());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(item.getRtime());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            txtRtime_date.setText(Integer.toString(date.getYear() + 1900) + "年" + Integer.toString(date.getMonth() + 1) + "月" + Integer.toString(date.getDate()) + "日");
            txtRtime_time.setText(Integer.toString(date.getHours()) + "时" + Integer.toString(date.getMinutes()) + "分");
            Year = date.getYear() + 1900;
            Month = date.getMonth() + 1;
            Day = date.getDate(); //getDay的得到的是星期几
            Hour = date.getHours();
            Minute = date.getMinutes();
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
                            spinnerAdapter = new ArrayAdapter<String>(NotificationEditActivity.this, R.layout.spinner_item, R.id.spinnerItem, classList);
                            spinnerCname.setAdapter(spinnerAdapter);
                            spinnerCname.setPrompt("班级列表");
                            spinnerCname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                                    CommonUtils.showShortMsg(NotificationEditActivity.this, classList[pos]);
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
                        }
                    });
                }
            }).start();
        }
        mainHandler = new Handler(getMainLooper());
        notificationDao = new NotificationDao();
    }

    public void btnEditClick(View view) {
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
                    String rid = item.getRid();
                    final Notification item;
                    item = new Notification();
                    item.setRid(rid);
                    item.setCname(cname);
                    item.setRname(rname);
                    item.setRtime(rtime);
                    item.setRcontent(rcontent);
                    item.setCid(new ClassDao().getClassByCname(cname).getCid()); //temporary code
                    item.setTname(((ApplicationTrans) getApplication()).getTname()); //temporary code
                    notificationDao.editNotification(item);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showShortMsg(NotificationEditActivity.this, "修改成功");
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(NotificationEditActivity.this, 2, onDateSetListener, 2022, 5, 1);
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(NotificationEditActivity.this, 2, onTimeSetListener, 00, 00, true);
        timePickerDialog.show();
    }
}