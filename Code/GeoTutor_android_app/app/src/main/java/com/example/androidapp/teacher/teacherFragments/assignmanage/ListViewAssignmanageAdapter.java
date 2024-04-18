package com.example.androidapp.teacher.teacherFragments.assignmanage;
/*自定义用户适配器类*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.DB_CRUD.PublishExperiments;
import com.example.androidapp.R;

import java.util.List;

public class ListViewAssignmanageAdapter extends BaseAdapter {
    private Context context;
    private List<PublishExperiments> publishExperimentsList;
    private OnEditBtnClickListener onEditBtnClickListener;
    private OnDeleteBtnClickListener onDeleteBtnClickListener;

    public ListViewAssignmanageAdapter() {
    }

    public ListViewAssignmanageAdapter(Context context, List<PublishExperiments> publishExperimentsList) {
        this.context = context;
        this.publishExperimentsList = publishExperimentsList;
    }

    public void setPublishExperimentsList(List<PublishExperiments> publishExperimentsList) {
        this.publishExperimentsList = publishExperimentsList;
    }

    public void setOnEditBtnClickListener(OnEditBtnClickListener onEditBtnClickListener) {
        this.onEditBtnClickListener = onEditBtnClickListener;
    }

    public void setOnDeleteBtnClickListener(OnDeleteBtnClickListener onDeleteBtnClickListener) {
        this.onDeleteBtnClickListener = onDeleteBtnClickListener;
    }

    @Override
    public int getCount() {
        return publishExperimentsList.size();
    }

    @Override
    public Object getItem(int i) {
        return publishExperimentsList.get(i);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.assignmanage_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtPbindex = convertView.findViewById(R.id.txtPbindex);
            viewHolder.txtCname = convertView.findViewById(R.id.txtCname);
            viewHolder.txtPbname = convertView.findViewById(R.id.txtPbname);
            viewHolder.txtPbstarttime = convertView.findViewById(R.id.txtPbstarttime);
            viewHolder.txtPbendtime = convertView.findViewById(R.id.txtPbendtime);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PublishExperiments item = publishExperimentsList.get(i);
        viewHolder.txtPbindex.setText(i + 1 + ".");
        viewHolder.txtCname.setText(item.getCname());
        if (item.getPbname().length() > 12)
            viewHolder.txtPbname.setText(item.getPbname().substring(0, 12) + "…");
        else
            viewHolder.txtPbname.setText(item.getPbname());
        viewHolder.txtPbstarttime.setText(item.getPbstarttime().substring(0, 19));
        viewHolder.txtPbendtime.setText(item.getPbendtime().substring(0, 19));
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
        private TextView txtPbindex, txtCname, txtPbname, txtPbstarttime, txtPbendtime;
        private ImageView btnEdit, btnDelete;
    }
}
