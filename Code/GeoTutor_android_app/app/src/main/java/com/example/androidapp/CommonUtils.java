package com.example.androidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

public class CommonUtils {
    public static void showShortMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showDialogMsg(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .create().show();
    }


    public static String getBcryptHash(String spassword) {
        String encodePwd = BCrypt.hashpw(spassword, BCrypt.gensalt()); // 加密，核心代码
        return encodePwd;
    }

    public static boolean checkPw(String spassword, String hashedPw) {
        if (hashedPw == null) return false;
        boolean flag = BCrypt.checkpw(spassword, hashedPw); // 验证加密是否正确
        return flag;
    }

}
