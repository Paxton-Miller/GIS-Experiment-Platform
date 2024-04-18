package com.example.androidapp.teacher.teacherFragments.assignmanage;

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

import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.DB_CRUD.PublishExperiments;
import com.example.androidapp.DB_CRUD.PublishExperimentsDao;
import com.example.androidapp.R;
import com.example.androidapp.teacher.TeacherHomeActivity;

import java.util.List;

public class AssignmanageFragment extends Fragment implements View.OnClickListener {
    private ListViewAssignmanageAdapter lvAssignmanageAdapter; //用户信息数据适配器

    private ListView lvAssignmanage;
    private ImageView btnAdd, btnBack, btnHome;

    private List<PublishExperiments> publishExperimentsList;
    private PublishExperimentsDao publishExperimentsDao;
    private Handler mainHandler;

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private String title;
    private int img;

    public static AssignmanageFragment newInstance(String title, int img) {
        AssignmanageFragment fragment = new AssignmanageFragment();
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
        View view = inflater.inflate(R.layout.fragment_assignmanage, container, false);
        Bundle argument = getArguments();
//        ImageView iv = view.findViewById(R.id.iv);
//        iv.setImageResource(argument.getInt(ARG_PARAM2, R.drawable.background));
        initView();
        loadPublishExperimentsList();
        lvAssignmanage = (ListView) view.findViewById(R.id.lvAssignmanage);
        btnAdd = (ImageView) view.findViewById(R.id.btnAdd); //不能放到createView
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        btnHome = (ImageView) view.findViewById(R.id.btnHome);
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);

        return view;
    }

    private void initView() {
        mainHandler = new Handler(getMainLooper());
        publishExperimentsDao = new PublishExperimentsDao();
//        lvNotifications = (ListView) getView().findViewById(R.id.lvNotifications);
    }

    public void loadPublishExperimentsList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                publishExperimentsList = publishExperimentsDao.getPublishExperimentsList();
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
        if (lvAssignmanageAdapter == null) {
            lvAssignmanageAdapter = new ListViewAssignmanageAdapter(getActivity(), publishExperimentsList);
            lvAssignmanage.setAdapter(lvAssignmanageAdapter);
        } else {
            lvAssignmanageAdapter.setPublishExperimentsList(publishExperimentsList);
            lvAssignmanageAdapter.notifyDataSetChanged();
        }
        //修改按钮
        lvAssignmanageAdapter.setOnEditBtnClickListener(new OnEditBtnClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                PublishExperiments item = publishExperimentsList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                Intent intent = new Intent(getActivity(), AssignmanageEditActivity.class);//!!!!!!!
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        //删除按钮
        lvAssignmanageAdapter.setOnDeleteBtnClickListener(new OnDeleteBtnClickListener() {
            @Override
            public void onDeleteBtnClick(View view, int position) {
                final String pbid = publishExperimentsList.get(position).getPbid();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//?
                builder.setTitle("提示信息");
                builder.setMessage("您确定要删除此作业吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                publishExperimentsDao.delPublishExperiments(pbid);
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        CommonUtils.showShortMsg(getActivity(), "删除成功");
                                        loadPublishExperimentsList();
                                    }
                                });
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(getActivity(), AssignmanageAddActivity.class);
                startActivityForResult(intent, 1);//requestCode
                break;
            case R.id.btnBack:
                getActivity().finish();
                break;
            case R.id.btnHome:
                Intent intent2 = new Intent(getActivity(), TeacherHomeActivity.class);
                startActivity(intent2);
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            loadPublishExperimentsList();
        }
    }
}