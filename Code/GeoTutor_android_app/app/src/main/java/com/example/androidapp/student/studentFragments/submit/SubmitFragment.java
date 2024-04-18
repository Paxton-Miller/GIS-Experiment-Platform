package com.example.androidapp.student.studentFragments.submit;

import static android.os.Looper.getMainLooper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.DB_CRUD.PerformExperiments;
import com.example.androidapp.DB_CRUD.PerformExperimentsDao;
import com.example.androidapp.DB_CRUD.PublishExperiments;
import com.example.androidapp.DB_CRUD.PublishExperimentsDao;
import com.example.androidapp.R;
import com.example.androidapp.teacher.teacherFragments.notifications.NotificationEditActivity;

import java.util.ArrayList;
import java.util.List;

public class SubmitFragment extends Fragment implements View.OnClickListener {
    private ListViewSubmitAdapter lvSubmitAdapter; //用户信息数据适配器

    private ListView lvSubmit;
    private ImageView btnBack;

    private List<PublishExperiments> publishExperimentsList;
    private PublishExperimentsDao publishExperimentsDao;
    private Handler mainHandler;

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private String title;
    private int img;

    public static SubmitFragment newInstance(String title, int img) {
        SubmitFragment fragment = new SubmitFragment();
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
        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        Bundle argument = getArguments();
//        ImageView iv = view.findViewById(R.id.iv);
//        iv.setImageResource(argument.getInt(ARG_PARAM2, R.drawable.background));
        initView();
        loadPublishExperimentsList();
        lvSubmit = (ListView) view.findViewById(R.id.lvSubmit);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

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
                List<PublishExperiments> tmp = publishExperimentsDao.getPublishExperimentsList();
                List<PerformExperiments> performExperimentsList = new PerformExperimentsDao().getPerformExperimentsList(((ApplicationTrans) getActivity().getApplication()).getCname(), "");
                publishExperimentsList = new ArrayList<>();
                for (int i = 0; i < tmp.size(); i++) {
                    if (tmp.get(i).getCname().equals(((ApplicationTrans) getActivity().getApplication()).getCname()))
                        publishExperimentsList.add(tmp.get(i));
//                    (tmp.get(i).getPbname().equals(performExperimentsList.get(j).getPbname()) && performExperimentsList.get(i).getSname().equals(((ApplicationTrans) getActivity().getApplication()).getSname()))
//                    if (tmp.get(i).getCname() == ((ApplicationTrans) getActivity().getApplication()).getCname())
//                        publishExperimentsList.add(tmp.get(i));
                }
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
        if (lvSubmitAdapter == null) {
            lvSubmitAdapter = new ListViewSubmitAdapter(getActivity(), publishExperimentsList);
            lvSubmit.setAdapter(lvSubmitAdapter);
        } else {
            lvSubmitAdapter.setPublishExperimentsList(publishExperimentsList);
            lvSubmitAdapter.notifyDataSetChanged();
        }
        //修改按钮
        lvSubmitAdapter.setOnEditBtnClickListener(new OnEditBtnClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//?
                builder.setTitle("提示信息");
                builder.setMessage("即将打开网页端提交作业，是否继续？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("http://47.115.226.172/");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
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
            case R.id.btnBack:
                getActivity().finish();
                break;
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