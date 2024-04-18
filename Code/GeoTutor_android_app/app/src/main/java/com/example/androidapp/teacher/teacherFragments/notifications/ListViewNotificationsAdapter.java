package com.example.androidapp.teacher.teacherFragments.notifications;
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
    private OnEditBtnClickListener onEditBtnClickListener;
    private OnDeleteBtnClickListener onDeleteBtnClickListener;

    public ListViewNotificationsAdapter() {
    }

    public ListViewNotificationsAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public void setOnEditBtnClickListener(OnEditBtnClickListener onEditBtnClickListener) {
        this.onEditBtnClickListener = onEditBtnClickListener;
    }

    public void setOnDeleteBtnClickListener(OnDeleteBtnClickListener onDeleteBtnClickListener) {
        this.onDeleteBtnClickListener = onDeleteBtnClickListener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.notifications_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtRindex = convertView.findViewById(R.id.txtRindex);
            viewHolder.txtCname = convertView.findViewById(R.id.txtCname);
            viewHolder.txtRname = convertView.findViewById(R.id.txtRname);
            viewHolder.txtRtime = convertView.findViewById(R.id.txtRtime);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
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

        viewHolder.txtRtime.setText(item.getRtime().substring(0, 19)); //省去seconds后面小数点
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditBtnClickListener.onEditBtnClick(view, i);
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteBtnClickListener.onDeleteBtnClick(view, i);
            }
        });
        return convertView;
    }

    //自定义内部类
    private class ViewHolder {
        private TextView txtRindex, txtCname, txtRname, txtRtime;
        private ImageView btnEdit, btnDelete;
    }
}
