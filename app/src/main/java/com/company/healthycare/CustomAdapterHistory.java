package com.company.healthycare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.healthycare.model.HeaderDiagnosisModel;

import java.util.List;

public class CustomAdapterHistory extends BaseAdapter {
    Activity activity;
    List<HeaderDiagnosisModel> datas;
    LayoutInflater layoutInflater;

    public CustomAdapterHistory(Activity activity,List<HeaderDiagnosisModel> datas){
        this.activity = activity;
        this.datas = datas;
        layoutInflater = activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = layoutInflater.inflate(R.layout.list_history_item,viewGroup,false);
            holder = new CustomAdapterHistory.ViewHolder();
            holder.txtDate = view.findViewById(R.id.txt_date);
            holder.txtDisease = view.findViewById(R.id.txt_disease);

            view.setTag(holder);
        }else{
            holder = (CustomAdapterHistory.ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder{
        TextView txtDate,txtDisease;
    }
}
