package com.example.androidapp.teacher.teacherFragments.assignfeedback;

import static android.os.Looper.getMainLooper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.ClassDao;
import com.example.androidapp.DB_CRUD.ClassInfo;
import com.example.androidapp.DB_CRUD.Experiment;
import com.example.androidapp.DB_CRUD.ExperimentDao;
import com.example.androidapp.DB_CRUD.PerformExperiments;
import com.example.androidapp.DB_CRUD.PerformExperimentsDao;
import com.example.androidapp.DB_CRUD.PublishExperiments;
import com.example.androidapp.DB_CRUD.PublishExperimentsDao;
import com.example.androidapp.R;
import com.example.androidapp.teacher.TeacherHomeActivity;
import com.example.androidapp.teacher.teacherFragments.assignmanage.AssignmanageAddActivity;

import java.util.List;

public class AssignfeedbackFragment extends Fragment implements View.OnClickListener {
    private ListViewAssignfeedbackAdapter lvAssignfeedbackAdapter; //用户信息数据适配器
    private String query_pbname = "", query_cname = "";

    private ListView lvAssignfeedback;
    private ImageView btnBack, btnHome;
    private String[] pbnameList; //获取实验列表
    private String[] classList; //获取班级列表
    private Spinner spinnerCname; //下拉列表选择框
    private ArrayAdapter<String> spinnerAdapterOfCname, spinnerAdapterOfPbname; //自定义适配器

    private List<PerformExperiments> performExperimentsList;
    private PerformExperimentsDao performExperimentsDao;
    private Handler mainHandler;

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private String title;
    private int img;

    public static AssignfeedbackFragment newInstance(String title, int img) {
        AssignfeedbackFragment fragment = new AssignfeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putInt(ARG_PARAM2, img);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            img = getArguments().getInt(ARG_PARAM2);
        }
//        initView();
//        loadNotificationList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignfeedback, container, false);
        Bundle argument = getArguments();
//        ImageView iv = view.findViewById(R.id.iv);
//        iv.setImageResource(argument.getInt(ARG_PARAM2, R.drawable.background));
        initView();
        loadPerformExperimentsList();
        //初始化控件
        spinnerCname = (Spinner) view.findViewById(R.id.spinnerCname);
        lvAssignfeedback = (ListView) view.findViewById(R.id.lvAssignfeedback);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        btnHome = (ImageView) view.findViewById(R.id.btnHome);
        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
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
                        spinnerAdapterOfCname = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.spinnerItem, classList);
                        spinnerCname.setAdapter(spinnerAdapterOfCname);
                        spinnerCname.setPrompt("班级列表");
                        spinnerCname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//                String[] cityStrs = getResources().getStringArray(R.array.cities);
//                Log.i(TAG, "onItemSelected pos:" + pos);
                                CommonUtils.showShortMsg(getActivity(), classList[pos]);
                                query_cname = classList[pos];
                                loadPerformExperimentsList();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                });
            }
        }).start();

        return view;
    }

    private void initView() {
        mainHandler = new Handler(getMainLooper());
        performExperimentsDao = new PerformExperimentsDao();
//        lvNotifications = (ListView) getView().findViewById(R.id.lvNotifications);
    }

    public void loadPerformExperimentsList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                performExperimentsList = performExperimentsDao.getPerformExperimentsList(query_cname, query_pbname);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showLvData();
                    }
                });
            }
        }).start();
    }

    private void showLvData() {
        if (lvAssignfeedbackAdapter == null) {
            lvAssignfeedbackAdapter = new ListViewAssignfeedbackAdapter(getActivity(), performExperimentsList);
            lvAssignfeedback.setAdapter(lvAssignfeedbackAdapter);
        } else {
            lvAssignfeedbackAdapter.setPerformExperimentsList(performExperimentsList);
            lvAssignfeedbackAdapter.notifyDataSetChanged();
        }
        //修改按钮
        lvAssignfeedbackAdapter.setOnEditBtnClickListener(new OnEditBtnClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                PerformExperiments item = performExperimentsList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                Intent intent = new Intent(getActivity(), AssignfeedbackEditActivity.class);//!!!!!!!
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                getActivity().finish();
                break;
            case R.id.btnHome:
                Intent intent = new Intent(getActivity(), TeacherHomeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            loadPerformExperimentsList();
        }
    }
}