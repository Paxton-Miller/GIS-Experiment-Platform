package com.example.androidapp.teacher.teacherFragments.assignmanage;

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
import com.example.androidapp.DB_CRUD.Experiment;
import com.example.androidapp.DB_CRUD.ExperimentDao;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.DB_CRUD.PublishExperiments;
import com.example.androidapp.DB_CRUD.PublishExperimentsDao;
import com.example.androidapp.DB_CRUD.StudentDao;
import com.example.androidapp.DB_CRUD.TeacherDao;
import com.example.androidapp.DB_CRUD.TeacherInfo;
import com.example.androidapp.R;
import com.example.androidapp.teacher.teacherFragments.notifications.NotificationAddActivity;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class AssignmanageAddActivity extends AppCompatActivity implements View.OnClickListener {
    //声明控件
    private EditText txtPbname;
    private TextView txtPbstarttime_date, txtPbstarttime_time;
    private TextView txtPbendtime_date, txtPbendtime_time;
    private int startYear, startMonth, startDay, startHour, startMinute; //记录年月日时分
    private int endYear, endMonth, endDay, endHour, endMinute; //记录年月日时分
    private Handler mainHandler; //主线程
    private PublishExperimentsDao publishExperimentsDao; //数据库操作类
    private String[] enameList; //获取实验列表
    private String[] classList; //获取班级列表
    private Spinner spinnerEname,spinnerCname; //下拉列表选择框
    private ArrayAdapter<String> spinnerAdapterOfCname,spinnerAdapterOfEname = null; //自定义适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignmanage_add);
        initView();
    }

    private void initView() {
        //初始化控件
        spinnerEname = findViewById(R.id.spinnerEname);
        spinnerCname = findViewById(R.id.spinnerCname);
        txtPbname = findViewById(R.id.txtPbname);
        txtPbstarttime_date = findViewById(R.id.txtPbstarttime_date);
        txtPbstarttime_time = findViewById(R.id.txtPbstarttime_time);
        txtPbendtime_date = findViewById(R.id.txtPbendtime_date);
        txtPbendtime_time = findViewById(R.id.txtPbendtime_time);
        txtPbstarttime_date.setOnClickListener(this);
        txtPbstarttime_time.setOnClickListener(this);
        txtPbendtime_date.setOnClickListener(this);
        txtPbendtime_time.setOnClickListener(this);
        mainHandler = new Handler(getMainLooper());
        publishExperimentsDao = new PublishExperimentsDao(); //数据库操作类
        //开启新线程查询全部班级列表，加载到下拉选择框spinner中
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取实验列表
                final List<Experiment> experimentList = new ExperimentDao().getExperimentList();
                enameList = new String[experimentList.size()];
                //获取班级列表
                final List<ClassInfo> classInfoList = new ClassDao().getClassList();
                classList = new String[classInfoList.size()];
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < experimentList.size(); i++) {
                            enameList[i] = experimentList.get(i).getEname();
                        }
                        for (int i = 0; i < classInfoList.size(); i++) {
                            classList[i] = classInfoList.get(i).getCname();
                        }
                        //加载下拉框内容
                        spinnerAdapterOfEname = new ArrayAdapter<String>(AssignmanageAddActivity.this, R.layout.spinner_item, R.id.spinnerItem, enameList);
                        spinnerEname.setAdapter(spinnerAdapterOfEname);
                        spinnerEname.setPrompt("实验列表");
                        spinnerEname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//                String[] cityStrs = getResources().getStringArray(R.array.cities);
//                Log.i(TAG, "onItemSelected pos:" + pos);
                                CommonUtils.showShortMsg(AssignmanageAddActivity.this, enameList[pos]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        //加载下拉框内容
                        spinnerAdapterOfCname = new ArrayAdapter<String>(AssignmanageAddActivity.this, R.layout.spinner_item, R.id.spinnerItem, classList);
                        spinnerCname.setAdapter(spinnerAdapterOfCname);
                        spinnerCname.setPrompt("班级列表");
                        spinnerCname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//                String[] cityStrs = getResources().getStringArray(R.array.cities);
//                Log.i(TAG, "onItemSelected pos:" + pos);
                                CommonUtils.showShortMsg(AssignmanageAddActivity.this, classList[pos]);
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
        final String ename = spinnerEname.getSelectedItem().toString().trim();
        final String cname = spinnerCname.getSelectedItem().toString().trim();
        final String pbname = txtPbname.getText().toString().trim();
        final String pbstarttime = Integer.toString(startYear) + "-" + Integer.toString(startMonth) + "-" + Integer.toString(startDay) + " " + Integer.toString(startHour) + ":" + Integer.toString(startMinute) + ":00";
        final String pbendtime = Integer.toString(endYear) + "-" + Integer.toString(endMonth) + "-" + Integer.toString(endDay) + " " + Integer.toString(endHour) + ":" + Integer.toString(endMinute) + ":00";

        if (TextUtils.isEmpty(ename) || TextUtils.isEmpty(pbname) || TextUtils.isEmpty(cname) || startYear == 0 || endYear == 0) {
            CommonUtils.showShortMsg(this, "请输入完整信息");
//            txtSid.requestFocus();
        } else {
            new Thread(new Runnable() {
                @Override //重写run方法
                public void run() {
                    final PublishExperiments item;

                    item = new PublishExperiments();
                    item.setCname(cname);
                    item.setEname(ename);
                    item.setTid(((ApplicationTrans) getApplication()).getTid());
                    item.setEid(new ExperimentDao().getExperimentByEname(ename).getEid());
                    item.setCid(new ClassDao().getClassByCname(cname).getCid()); //temporary code
                    item.setTname(((ApplicationTrans) getApplication()).getTname()); //temporary code
                    item.setPbname(pbname);
                    item.setPbstarttime(pbstarttime);
                    item.setPbendtime(pbendtime);
                    publishExperimentsDao.addPublishExperiments(item);

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.showShortMsg(AssignmanageAddActivity.this, "添加成功");
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AssignmanageAddActivity.this, 2, onDateSetListener, 2022, 5, 1);
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(AssignmanageAddActivity.this, 2, onTimeSetListener, 00, 00, true);
        timePickerDialog.show();
    }

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AssignmanageAddActivity.this, 2, onDateSetListener, 2022, 5, 1);
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(AssignmanageAddActivity.this, 2, onTimeSetListener, 00, 00, true);
        timePickerDialog.show();
    }
}