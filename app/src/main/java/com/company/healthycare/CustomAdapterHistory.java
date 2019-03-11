package com.company.healthycare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.company.healthycare.model.DiseasesModel;
import com.company.healthycare.model.HeaderDiagnosisModel;

import java.util.List;

public class CustomAdapterHistory extends BaseAdapter {
    Activity activity;
    List<HeaderDiagnosisModel> datas;
    List<DiseasesModel> diseases;
    List<String> dates;
    LayoutInflater layoutInflater;

    public CustomAdapterHistory(Activity activity,List<HeaderDiagnosisModel> datas,List<String> dates,List<DiseasesModel> diseases){
        this.activity = activity;
        this.datas = datas;
        this.dates = dates;
        this.diseases = diseases;
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
        HeaderDiagnosisModel headerData = datas.get(i);
        String date = dates.get(i);
        holder.txtDate.setText(date);
        for(DiseasesModel dm : diseases){
            if(dm.ID.equals(headerData.getIdDisease())){
                holder.txtDisease.setText(headerData.getIdDisease() + ". " + dm.Name);
            }
        }
        return view;
    }

    class ViewHolder{
        TextView txtDate,txtDisease;
    }
}
