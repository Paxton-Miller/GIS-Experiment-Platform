package com.example.androidapp.student.studentFragments.studenthome;

import static android.os.Looper.getMainLooper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;
import com.example.androidapp.LoginActivity;
import com.example.androidapp.R;

import java.util.List;

public class StudentHomeFragment extends Fragment implements View.OnClickListener {
    private Handler mainHandler;
    private TextView txtSname, txtSgender;
    private ImageView btnWeb, btnExit;

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private String title;
    private int img;

    public static StudentHomeFragment newInstance(String title, int img) {
        StudentHomeFragment fragment = new StudentHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);
        Bundle argument = getArguments();
//        ImageView iv = view.findViewById(R.id.iv);
//        iv.setImageResource(argument.getInt(ARG_PARAM2, R.drawable.background));
        initView();
        txtSgender = (TextView) view.findViewById(R.id.txtSgender);
        txtSname = (TextView) view.findViewById(R.id.txtSname);
        txtSname.setText(((ApplicationTrans) getActivity().getApplication()).getSname());
        txtSgender.setText(((ApplicationTrans) getActivity().getApplication()).getSgender());
        btnWeb = (ImageView) view.findViewById(R.id.btnWeb);
        btnWeb.setOnClickListener(this);
        btnExit = (ImageView) view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
        return view;
    }

    private void initView() {
        mainHandler = new Handler(getMainLooper());
//        notificationDao = new NotificationDao();
//        lvNotifications = (ListView) getView().findViewById(R.id.lvNotifications);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWeb:
                Uri uri = Uri.parse("http://47.115.226.172/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.btnExit:
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
//            loadNotificationList();
        }
    }
}