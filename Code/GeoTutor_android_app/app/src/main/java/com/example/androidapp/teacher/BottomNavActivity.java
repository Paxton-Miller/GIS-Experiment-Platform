package com.example.androidapp.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.androidapp.R;
import com.example.androidapp.teacher.teacherFragments.assignfeedback.AssignfeedbackFragment;
import com.example.androidapp.teacher.teacherFragments.assignmanage.AssignmanageFragment;
import com.example.androidapp.teacher.teacherFragments.exptmanage.ExptmanageFragment;
import com.example.androidapp.teacher.teacherFragments.studentmanage.StudentmanageFragment;
import com.example.androidapp.teacher.teacherFragments.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BottomNavActivity extends FragmentActivity {
    private BottomNavigationView btmNav;
    private ViewPager vpContainer;
    private List<Fragment> fragmentList;
    private ViewPageAdapter vpAdapter;
    private NotificationsFragment notificationsFrag;
    private ExptmanageFragment exptmanageFrag;
    private AssignmanageFragment assignmanageFrag;
    private AssignfeedbackFragment assignfeedbackFrag;
    private StudentmanageFragment studentmanageFrag;
//    private notificationsFrag notificationsFragment;
//    private exptmanageFrag exptmanageFragment;
//    private Fragment[] fragments;    //Fragment数组
//    private int lastShowFragment = 0;   //表示最后一个显示的Fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        initData();
        initView();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        notificationsFrag = NotificationsFragment.newInstance("首页", R.drawable.background);
        exptmanageFrag = ExptmanageFragment.newInstance("首页", R.drawable.background);
        assignmanageFrag = AssignmanageFragment.newInstance("首页", R.drawable.background);
        assignfeedbackFrag = AssignfeedbackFragment.newInstance("首页", R.drawable.background);
        studentmanageFrag = StudentmanageFragment.newInstance("首页", R.drawable.background);
        fragmentList.add(notificationsFrag);
        fragmentList.add(exptmanageFrag);
        fragmentList.add(assignmanageFrag);
        fragmentList.add(assignfeedbackFrag);
        fragmentList.add(studentmanageFrag);
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
//                        mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navExptmanage:
                        vpContainer.setCurrentItem(1);
//                        mTextMessage.setText(R.string.title_dashboard);
                        return true;
                    case R.id.navAssignmanage:
                        vpContainer.setCurrentItem(2);
//                        mTextMessage.setText(R.string.title_notifications);
                        return true;
                    case R.id.navAssignfeedback:
                        vpContainer.setCurrentItem(3);
//                        mTextMessage.setText(R.string.title_notifications);
                        return true;
                    case R.id.navStudentmanage:
                        btmNav.removeBadge(R.id.navStudentmanage);
                        vpContainer.setCurrentItem(4);
//                        mTextMessage.setText(R.string.title_home);
                        return true;
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
                btmNav.setSelectedItemId(R.id.navExptmanage);
                exptmanageFrag.loadExperimentList();
                break;
            case 2:
                btmNav.setSelectedItemId(R.id.navAssignmanage);
                assignmanageFrag.loadPublishExperimentsList();
                break;
            case 3:
                btmNav.setSelectedItemId(R.id.navAssignfeedback);
                //waitng to be coded
                break;
            case 4:
                btmNav.setSelectedItemId(R.id.navStudentmanage);
                studentmanageFrag.loadStudentInfoList();
                break;
            default:
                break;
        }
    }

//    public void switchFragment(int lastIndex, int index) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.hide(fragments[lastIndex]);
//        if (!fragments[index].isAdded()) {
//            transaction.add(R.id.container, fragments[index]);
//        }
//        transaction.show(fragments[index]).commitAllowingStateLoss();
//    }
//
//    private void initFragments() {
//        notificationsFragment = new notificationsFrag();
//        exptmanageFragment = new exptmanageFrag();
//        fragments = new Fragment[]{fragment1, fragment2};
//        lastShowFragment = 0;
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.container, fragment1)
//                .show(fragment1)
//                .commit();
//    }
}