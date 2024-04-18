package com.example.androidapp.student.studentFragments.notifications;

import static android.os.Looper.getMainLooper;

import android.content.Intent;
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
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.NotificationDao;
import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements View.OnClickListener {
    private ListViewNotificationsAdapter lvNotificationsAdapter; //用户信息数据适配器

    private ListView lvNotifications;
    private ImageView btnBack;

    private List<Notification> notificationList;
    private NotificationDao notificationDao;
    private Handler mainHandler;

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private String title;
    private int img;

    public static NotificationsFragment newInstance(String title, int img) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_notifications_student, container, false);
        Bundle argument = getArguments();
//        ImageView iv = view.findViewById(R.id.iv);
//        iv.setImageResource(argument.getInt(ARG_PARAM2, R.drawable.background));
        initView();
        loadNotificationList();
        lvNotifications = (ListView) view.findViewById(R.id.lvNotifications);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        return view;
    }

    private void initView() {
        mainHandler = new Handler(getMainLooper());
        notificationDao = new NotificationDao();
//        lvNotifications = (ListView) getView().findViewById(R.id.lvNotifications);
    }

    public void loadNotificationList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("error", ((ApplicationTrans) (getActivity().getApplication())).getCname());
                List<Notification> tmp = notificationDao.getNotificationList();
                notificationList = new ArrayList<>();
                for (int i = 0; i < tmp.size(); i++) {
                    if (tmp.get(i).getCname().equals(((ApplicationTrans) (getActivity().getApplication())).getCname()))
                        notificationList.add(tmp.get(i));
                }
//                notificationList = notificationDao.getNotificationList();
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
        if (lvNotificationsAdapter == null) {
            lvNotificationsAdapter = new ListViewNotificationsAdapter(getActivity(), notificationList);
            lvNotifications.setAdapter(lvNotificationsAdapter);
        } else {
            lvNotificationsAdapter.setNotificationList(notificationList);
            lvNotificationsAdapter.notifyDataSetChanged();
        }
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
            loadNotificationList();
        }
    }
}