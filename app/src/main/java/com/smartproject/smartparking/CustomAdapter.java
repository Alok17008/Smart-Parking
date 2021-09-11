package com.smartproject.smartparking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Bookinguser> {

    private Activity context;
    List<Bookinguser> storeDataList;

    public CustomAdapter(Activity context, List<Bookinguser> storeDataList) {
        super(context, R.layout.sample_layout, storeDataList);
        this.context = context;
        this.storeDataList = storeDataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sample_layout, null, true);

        Bookinguser storeData = storeDataList.get(position);

        TextView textView1 = view.findViewById(R.id.usernamepark );
        TextView textView2 = view.findViewById(R.id.parkemail );
        TextView  vtype = view.findViewById(R.id.parkvehicaltype);
        TextView  vno = view.findViewById(R.id.parkvechicalno);
        TextView  pdate = view.findViewById(R.id.parkdate);
        TextView  ptime = view.findViewById(R.id.Parkingtime);
        textView1.setText(storeData.getName());
        textView2.setText(storeData.getParking_name());
        vtype.setText( storeData.getVechical_Type() );
        vno.setText( storeData.getVehical_no());
        pdate.setText( storeData.getSaveCurrentDate());
        ptime.setText( storeData.getSaveCurrentTime());


        return view;
    }
}
