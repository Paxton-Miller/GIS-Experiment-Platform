package com.example.androidapp.student.studentFragments.notifications;
/*自定义用户适配器类*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.R;

import java.util.List;

public class ListViewNotificationsAdapter extends BaseAdapter {
    private Context context;
    private List<Notification> notificationList;

    public ListViewNotificationsAdapter() {
    }

    public ListViewNotificationsAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int i) {
        return notificationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        return null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notifications_list_item_student, null);
            viewHolder = new ViewHolder();
            viewHolder.txtRindex = convertView.findViewById(R.id.txtRindex);
            viewHolder.txtCname = convertView.findViewById(R.id.txtCname);
            viewHolder.txtRname = convertView.findViewById(R.id.txtRname);
            viewHolder.txtRtime = convertView.findViewById(R.id.txtRtime);
            viewHolder.txtTname = convertView.findViewById(R.id.txtTname);
            viewHolder.txtRcontent = convertView.findViewById(R.id.txtRcontent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Notification item = notificationList.get(i);
        viewHolder.txtRindex.setText(i + 1 + ".");
        viewHolder.txtCname.setText(item.getCname());
        if (item.getRname().length() > 20)
            viewHolder.txtRname.setText(item.getRname().substring(0, 20) + "…");
        else
            viewHolder.txtRname.setText(item.getRname());
        viewHolder.txtTname.setText(item.getTname());
        viewHolder.txtRcontent.setText(item.getRcontent());

        viewHolder.txtRtime.setText(item.getRtime().substring(0, 19)); //省去seconds后面小数点
        return convertView;
    }

    //自定义内部类
    private class ViewHolder {
        private TextView txtRindex, txtCname, txtRname, txtRtime, txtTname, txtRcontent;
    }
}
