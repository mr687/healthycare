package com.company.healthycare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.healthycare.model.ListIndicationsModel;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Activity activity;
    List<ListIndicationsModel> indications;
    LayoutInflater inflater;

    public CustomAdapter(Activity activity, List<ListIndicationsModel> indication) {
        this.activity = activity;
        this.indications = indication;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return indications.size();
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
        ViewHolder holder= null;
        if(view == null){
            view = inflater.inflate(R.layout.list_indication_items,viewGroup,false);
            holder = new ViewHolder();
            holder.txtIndicationTitle = (TextView) view.findViewById(R.id.txt_indication_title);
            holder.imgCheck = (ImageView) view.findViewById(R.id.img_check);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        ListIndicationsModel model = indications.get(i);
        holder.txtIndicationTitle.setText(model.getmIndication());
        if(model.isSelected()){
            holder.imgCheck.setBackgroundResource(R.drawable.outline_check_box_black_24dp);
        }else{
            holder.imgCheck.setBackgroundResource(R.drawable.outline_check_box_outline_blank_black_24dp);
        }
        return view;
    }
    public void updateRecords(List<ListIndicationsModel> indications){
        this.indications = indications;
        notifyDataSetChanged();
    }
    static class ViewHolder{
        TextView txtIndicationTitle;
        ImageView imgCheck;
    }
}
