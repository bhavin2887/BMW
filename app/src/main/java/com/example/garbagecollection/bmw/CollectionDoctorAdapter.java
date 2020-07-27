package com.example.garbagecollection.bmw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

public class CollectionDoctorAdapter extends BaseAdapter {
    ViewHolder holder;
    private Context context;
    private List<WasteScanningAtHospitals> mArrayList;

    public CollectionDoctorAdapter(Context context, List<WasteScanningAtHospitals> mArrayList) {
        this.context = context;
        this.mArrayList = mArrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_collection, null);
            holder.tv_coll_id = (TextView) convertView.findViewById(R.id.tv_coll_id);
            holder.tv_coll_name = (TextView) convertView.findViewById(R.id.tv_coll_name);
            //holder.tv_coll_weight = (TextView) convertView.findViewById(R.id.tv_coll_weight);
            holder.tv_coll_qr = (TextView) convertView.findViewById(R.id.tv_coll_qr);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_coll_name.setText("BagWeight : "+mArrayList.get(position).getWeight());
        String [] val = new String[1];
        if(mArrayList.get(position).getQRCodeContent()!=null) {
            holder.tv_coll_id.setText("BagQRCode : "+mArrayList.get(position).getBagQRCodeDetalID());
            val = mArrayList.get(position).getQRCodeContent().split("-");
            String result = mArrayList.get(position).getQRCodeContent();
            final String[] tokens = result.split(Pattern.quote("|"));//YELLOW-01071900093-SHUBH390020GJBH100|77108
            final String[] colorToken = tokens[0].split(Pattern.quote("-"));
            holder.tv_coll_qr.setText(colorToken[0] + " - " + colorToken[1]);
        }else{
            holder.tv_coll_qr.setText(mArrayList.get(position).getImageTitle());
        }
        return convertView;

    }

    class ViewHolder {
        TextView tv_coll_id, tv_coll_name, tv_coll_weight, tv_coll_qr;
    }
}
