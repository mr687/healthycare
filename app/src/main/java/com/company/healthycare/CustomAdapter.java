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

/**
 * Class ini digunakan untuk membuat adapter listView yang bisa kita modifikasi
 *
 */
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
            //Proses inflate list_indication_items layout
            //template yang akan di tampilkan di listView per item
            view = inflater.inflate(R.layout.list_indication_items,viewGroup,false);
            holder = new ViewHolder();

            //mendefinisikan textView yang dibuat di list_indication_items layout tadi
            holder.txtIndicationTitle = view.findViewById(R.id.txt_indication_title);
            holder.imgCheck = view.findViewById(R.id.img_check);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        ListIndicationsModel model = indications.get(i);
        holder.txtIndicationTitle.setText(model.getmIndication());
        if(model.isSelected()){
            //mengubah background gambar menjadi checklist ketika model select dipilih
            holder.imgCheck.setBackgroundResource(R.drawable.outline_check_box_black_24dp);
        }else{
            //mengubah background gambar menjadi unchecklist ketika model select tidak dipilih
            holder.imgCheck.setBackgroundResource(R.drawable.outline_check_box_outline_blank_black_24dp);
        }
        return view;
    }

    /**
     * Fungsi ini digunakan untuk update data pada listview secara realtime
     *
     * @param indications
     */
    public void updateRecords(List<ListIndicationsModel> indications){
        this.indications = indications;
        notifyDataSetChanged();
    }

    /**
     * Class ini digunakan untuk mendefinisikan ada berapa banyak view yang akan di tampilkan di item listview
     */
    static class ViewHolder{
        TextView txtIndicationTitle;
        ImageView imgCheck;
    }
}
