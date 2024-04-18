package com.example.androidapp.teacher.teacherFragments.exptmanage;
/*自定义用户适配器类*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.DB_CRUD.Experiment;
import com.example.androidapp.DB_CRUD.Notification;
import com.example.androidapp.R;
import com.example.androidapp.teacher.teacherFragments.notifications.OnEditBtnClickListener;

import java.util.List;

public class ListViewExptmanageAdapter extends BaseAdapter {
    private Context context;
    private List<Experiment> experimentList;
    private OnEditBtnClickListener onEditBtnClickListener;

    public ListViewExptmanageAdapter() {
    }

    public ListViewExptmanageAdapter(Context context, List<Experiment> experimentList) {
        this.context = context;
        this.experimentList = experimentList;
    }

    public void setExperimentList(List<Experiment> experimentList) {
        this.experimentList = experimentList;
    }

    public void setOnEditBtnClickListener(OnEditBtnClickListener onEditBtnClickListener) {
        this.onEditBtnClickListener = onEditBtnClickListener;
    }

    @Override
    public int getCount() {
        return experimentList.size();
    }

    @Override
    public Object getItem(int i) {
        return experimentList.get(i);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.exptmanage_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtEindex = convertView.findViewById(R.id.txtEindex);
            viewHolder.txtEname = convertView.findViewById(R.id.txtEname);
            viewHolder.txtEdesc = convertView.findViewById(R.id.txtEdesc);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Experiment item = experimentList.get(i);
        viewHolder.txtEindex.setText(i + 1 + ".");
        viewHolder.txtEname.setText(item.getEname());
        viewHolder.txtEdesc.setText(item.getEdesc());
//        viewHolder.txtEsteps.setText(item.getEsteps().replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "").replace(":", ""));
        if (item.getEdesc().length() > 12)
            viewHolder.txtEdesc.setText(item.getEdesc().substring(0, 12) + "…");
        else
            viewHolder.txtEdesc.setText(item.getEdesc());
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
        private TextView txtEindex, txtEname, txtEdesc;
        private ImageView btnEdit;
    }
}
