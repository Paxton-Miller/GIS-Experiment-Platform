package com.example.androidapp.teacher.teacherFragments.studentmanage;
/*自定义用户适配器类*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.DB_CRUD.StudentInfo;
import com.example.androidapp.R;

import java.util.List;

public class ListViewStudentmanageAdapter extends BaseAdapter {
    private Context context;
    private List<StudentInfo> studentInfoList;
    private OnEditBtnClickListener onEditBtnClickListener;
    private OnDeleteBtnClickListener onDeleteBtnClickListener;

    public ListViewStudentmanageAdapter() {
    }

    public ListViewStudentmanageAdapter(Context context, List<StudentInfo> studentInfoList) {
        this.context = context;
        this.studentInfoList = studentInfoList;
    }

    public void setStudentInfoList(List<StudentInfo> studentInfoList) {
        this.studentInfoList = studentInfoList;
    }

    public void setOnEditBtnClickListener(OnEditBtnClickListener onEditBtnClickListener) {
        this.onEditBtnClickListener = onEditBtnClickListener;
    }

    public void setOnDeleteBtnClickListener(OnDeleteBtnClickListener onDeleteBtnClickListener) {
        this.onDeleteBtnClickListener = onDeleteBtnClickListener;
    }

    @Override
    public int getCount() {
        return studentInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return studentInfoList.get(i);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.studentmanage_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtSindex = convertView.findViewById(R.id.txtSindex);
            viewHolder.txtCname = convertView.findViewById(R.id.txtCname);
            viewHolder.txtSname = convertView.findViewById(R.id.txtSname);
            viewHolder.txtSid = convertView.findViewById(R.id.txtSid);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StudentInfo item = studentInfoList.get(i);
        viewHolder.txtSindex.setText(i + 1 + ".");
        viewHolder.txtCname.setText(item.getCname());
        if (item.getSname().length() > 5)
            viewHolder.txtSname.setText(item.getSname().substring(0, 5) + "…");
        else
            viewHolder.txtSname.setText(item.getSname());
        viewHolder.txtSid.setText(item.getSid());
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
        private TextView txtSindex, txtCname, txtSname, txtSid;
        private ImageView btnEdit, btnDelete;
    }
}
