package com.example.workhourtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//ArrrayAdapter class

public class ListArrayAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public ListArrayAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(HourListingsClass object) {
        super.add(object);
        list.add(object);
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
    public View getView(int position, View convertView, ViewGroup parent){
        View row;
        row = convertView;
        HourHolder hourHolder;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            hourHolder = new HourHolder();
            hourHolder.tx_startTimeText = row.findViewById(R.id.tx_startTimeText);
            hourHolder.tx_endTimeText = row.findViewById(R.id.tx_endTimeText);
            hourHolder.tx_descriptionText =  row.findViewById(R.id.tx_descriptionText);
            row.setTag(hourHolder);
        }
        else
        {
            hourHolder = (HourHolder)row.getTag();
        }

        HourListingsClass hourListingsClass= (HourListingsClass) this.getItem(position);
        hourHolder.tx_startTimeText.setText(hourListingsClass.getStartTime());
        hourHolder.tx_endTimeText.setText(hourListingsClass.getEndTime());
        hourHolder.tx_descriptionText.setText(hourListingsClass.getDescription());
        return row;
    }

    static class HourHolder
    {
        TextView tx_startTimeText, tx_endTimeText, tx_descriptionText;

    }
}
