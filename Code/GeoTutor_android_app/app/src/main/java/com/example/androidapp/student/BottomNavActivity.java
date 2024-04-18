package com.example.androidapp.student;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.student.studentFragments.notifications.NotificationsFragment;
import com.example.androidapp.student.studentFragments.studenthome.StudentHomeFragment;
import com.example.androidapp.student.studentFragments.submit.SubmitFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BottomNavActivity extends FragmentActivity {
    private BottomNavigationView btmNav;
    private ViewPager vpContainer;
    private List<Fragment> fragmentList;
    private ViewPageAdapter vpAdapter;
    private NotificationsFragment notificationsFrag;
    private SubmitFragment submitFrag;
    private StudentHomeFragment studentHomeFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_student);
        initData();
        initView();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        notificationsFrag = NotificationsFragment.newInstance("首页", R.drawable.background);
        submitFrag = SubmitFragment.newInstance("首页", R.drawable.background);
        studentHomeFrag = StudentHomeFragment.newInstance("首页", R.drawable.background);
        fragmentList.add(notificationsFrag);
        fragmentList.add(submitFrag);
        fragmentList.add(studentHomeFrag);
    }


    private void initView() {
        btmNav = findViewById(R.id.btmNav);
        vpContainer = findViewById(R.id.vpContainer);
        vpContainer.setOffscreenPageLimit(5);
        vpAdapter = new ViewPageAdapter(getSupportFragmentManager(), fragmentList);
//        BadgeDrawable badge = btmNav.getOrCreateBadge(R.id.navHome);
//        badge.setNumber(10);
//        badge.setMaxCharacterCount(2);
        vpContainer.setAdapter(vpAdapter);
        // 滑动时联动
        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(BottomNavActivity.this, "页面" + position, Toast.LENGTH_SHORT).show();
                viewPageSelected(position); //联动效果
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 点击时联动
        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navNotifications:
                        vpContainer.setCurrentItem(0);
                        return true;
                    case R.id.navSubmit:
                        vpContainer.setCurrentItem(1);
                        return true;
                    case R.id.navStudentHome:
                        vpContainer.setCurrentItem(2);
                        return true;
//                        mTextMessage.setText(R.string.title_home);
                }
                return false;
            }
        });
    }

    private void viewPageSelected(int position) {
        switch (position) {
            case 0:
                btmNav.setSelectedItemId(R.id.navNotifications);
                notificationsFrag.loadNotificationList();
                break;
            case 1:
                btmNav.setSelectedItemId(R.id.navSubmit);
//                submitFrag.loadExperimentList();
                break;
            case 2:
                btmNav.setSelectedItemId(R.id.navStudentHome);
//                studentHomeFrag.loadPublishExperimentsList();
                break;
            default:
                break;
        }
    }
}