package com.example.androidapp.teacher.teacherFragments.assignfeedback;
/*自定义用户适配器类*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.DB_CRUD.PerformExperiments;
import com.example.androidapp.R;
import com.example.androidapp.teacher.teacherFragments.assignfeedback.OnEditBtnClickListener;

import java.util.List;

public class ListViewAssignfeedbackAdapter extends BaseAdapter {
    private Context context;
    private List<PerformExperiments> performExperimentsList;
    private OnEditBtnClickListener onEditBtnClickListener;

    public ListViewAssignfeedbackAdapter() {
    }

    public ListViewAssignfeedbackAdapter(Context context, List<PerformExperiments> performExperimentsList) {
        this.context = context;
        this.performExperimentsList = performExperimentsList;
    }

    public void setPerformExperimentsList(List<PerformExperiments> performExperimentsList) {
        this.performExperimentsList = performExperimentsList;
    }

    public void setOnEditBtnClickListener(OnEditBtnClickListener onEditBtnClickListener) {
        this.onEditBtnClickListener = onEditBtnClickListener;
    }

    @Override
    public int getCount() {
        return performExperimentsList.size();
    }

    @Override
    public Object getItem(int i) {
        return performExperimentsList.get(i);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.assignfeedback_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtPfindex = convertView.findViewById(R.id.txtPfindex);
            viewHolder.txtSname = convertView.findViewById(R.id.txtSname);
            viewHolder.txtSid = convertView.findViewById(R.id.txtSid);
            viewHolder.txtCname = convertView.findViewById(R.id.txtCname);
            viewHolder.txtEname = convertView.findViewById(R.id.txtEname);
            viewHolder.txtPfgrade = convertView.findViewById(R.id.txtPfgrade);
            viewHolder.txtPfcomment = convertView.findViewById(R.id.txtPfcomment);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PerformExperiments item = performExperimentsList.get(i);
        viewHolder.txtPfindex.setText(i + 1 + ".");
        viewHolder.txtCname.setText(item.getCname());
        viewHolder.txtSid.setText(item.getSid());
        viewHolder.txtEname.setText(item.getEname());
        viewHolder.txtPfgrade.setText(item.getPfgrade());
        if (item.getSname().length() > 5)
            viewHolder.txtSname.setText(item.getSname().substring(0, 5) + "…");
        else
            viewHolder.txtSname.setText(item.getSname());
        if (item.getPfcomment() == null)
            viewHolder.txtPfcomment.setText("");
        else if (item.getPfcomment().length() > 12)
            viewHolder.txtPfcomment.setText(item.getPfcomment().substring(0, 12) + "…");
        else
            viewHolder.txtPfcomment.setText(item.getPfcomment());
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
        private TextView txtPfindex, txtCname, txtEname, txtSname, txtSid, txtPfgrade, txtPfcomment;
        private ImageView btnEdit;
    }
}
