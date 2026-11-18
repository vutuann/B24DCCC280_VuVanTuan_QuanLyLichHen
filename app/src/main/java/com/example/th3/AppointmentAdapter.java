package com.example.th3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AppointmentAdapter extends BaseAdapter {

    Context context;
    ArrayList<Appointment> list;

    public AppointmentAdapter(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvItemTitle);
        TextView tvLocation = convertView.findViewById(R.id.tvItemLocation);
        TextView tvDateTime = convertView.findViewById(R.id.tvItemDateTime);
        TextView tvReminder = convertView.findViewById(R.id.tvItemReminder);

        Appointment a = list.get(position);
        tvTitle.setText(a.getTitle());
        tvLocation.setText("Địa điểm: " + a.getLocation());
        tvDateTime.setText("Thời gian: " + a.getDisplayDateTime());

        if (a.isReminder()) {
            tvReminder.setText("Có nhắc");
            tvReminder.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            tvReminder.setText("Không nhắc");
            tvReminder.setBackgroundColor(Color.parseColor("#9E9E9E"));
        }

        return convertView;
    }
}
