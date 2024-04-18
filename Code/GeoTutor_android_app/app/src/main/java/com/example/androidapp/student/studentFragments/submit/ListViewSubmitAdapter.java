package com.example.androidapp.student.studentFragments.submit;
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

public class ListViewSubmitAdapter extends BaseAdapter {
    private Context context;
    private List<PublishExperiments> publishExperimentsList;
    private OnEditBtnClickListener onEditBtnClickListener;

    public ListViewSubmitAdapter() {
    }

    public ListViewSubmitAdapter(Context context, List<PublishExperiments> publishExperimentsList) {
        this.context = context;
        this.publishExperimentsList = publishExperimentsList;
    }

    public void setPublishExperimentsList(List<PublishExperiments> publishExperimentsList) {
        this.publishExperimentsList = publishExperimentsList;
    }

    public void setOnEditBtnClickListener(OnEditBtnClickListener onEditBtnClickListener) {
        this.onEditBtnClickListener = onEditBtnClickListener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.submit_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtPfindex = convertView.findViewById(R.id.txtPfindex);
            viewHolder.txtCname = convertView.findViewById(R.id.txtCname);
            viewHolder.txtPbname = convertView.findViewById(R.id.txtPbname);
            viewHolder.txtPbendtime = convertView.findViewById(R.id.txtPbendtime);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PublishExperiments item = publishExperimentsList.get(i);
        viewHolder.txtPfindex.setText(i + 1 + ".");
        viewHolder.txtCname.setText(item.getCname());
        if (item.getPbname().length() > 20)
            viewHolder.txtPbname.setText(item.getPbname().substring(0, 20) + "…");
        else
            viewHolder.txtPbname.setText(item.getPbname());
        viewHolder.txtPbendtime.setText(item.getPbendtime().substring(0, 19));
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditBtnClickListener.onEditBtnClick(view, i);
            }
        });
        return convertView;
    }

    //自定义内部类
    private class ViewHolder {
        private TextView txtPfindex, txtCname, txtPbname, txtPbendtime;
        private ImageView btnEdit, btnDelete;
    }
}
