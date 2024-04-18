package com.example.androidapp.teacher.teacherFragments.exptmanage;

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
import android.widget.ImageView;
import android.widget.ListView;

import com.example.androidapp.DB_CRUD.Experiment;
import com.example.androidapp.DB_CRUD.ExperimentDao;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.R;
import com.example.androidapp.teacher.TeacherHomeActivity;
import com.example.androidapp.teacher.teacherFragments.notifications.NotificationEditActivity;
import com.example.androidapp.teacher.teacherFragments.notifications.OnEditBtnClickListener;

import java.util.List;

public class ExptmanageFragment extends Fragment implements View.OnClickListener {
    private ListViewExptmanageAdapter lvExptmanageAdapter; //用户信息数据适配器

    private ListView lvExptmanage;
    private ImageView btnBack, btnHome;

    private List<Experiment> experimentList;
    private ExperimentDao experimentDao;
    private Handler mainHandler;

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private String title;
    private int img;

    public static ExptmanageFragment newInstance(String title, int img) {
        ExptmanageFragment fragment = new ExptmanageFragment();
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
//        loadExperimentList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exptmanage, container, false);
        Bundle argument = getArguments();
//        ImageView iv = view.findViewById(R.id.iv);
//        iv.setImageResource(argument.getInt(ARG_PARAM2, R.drawable.background));
        initView();
        loadExperimentList();
        lvExptmanage = (ListView) view.findViewById(R.id.lvExptmanage);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnHome = (ImageView) view.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

        return view;
    }

    private void initView() {
        mainHandler = new Handler(getMainLooper());
        experimentDao = new ExperimentDao();
//        lvExptmanage = (ListView) getView().findViewById(R.id.lvExptmanage);
    }

    public void loadExperimentList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                experimentList = experimentDao.getExperimentList();
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
        if (lvExptmanageAdapter == null) {
            lvExptmanageAdapter = new ListViewExptmanageAdapter(getActivity(), experimentList);
            lvExptmanage.setAdapter(lvExptmanageAdapter);
        } else {
            lvExptmanageAdapter.setExperimentList(experimentList);
            lvExptmanageAdapter.notifyDataSetChanged();
        }
        //修改按钮
        //修改按钮
        lvExptmanageAdapter.setOnEditBtnClickListener(new OnEditBtnClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                Experiment item = experimentList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                Intent intent = new Intent(getActivity(), ExptmanageEditActivity.class);//!!!!!!!
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
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            loadExperimentList();
        }
    }
}