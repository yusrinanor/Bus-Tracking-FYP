package com.example.android.fyp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ReportHolder extends RecyclerView.ViewHolder{
    public TextView txtDate, txtTime, txtFrom, txtTo, txtRoute;

    public ReportHolder(View itemView) {
        super(itemView);

        txtDate     = (TextView)itemView.findViewById(R.id.Date);
        txtTime     = (TextView)itemView.findViewById(R.id.Time);
        txtFrom     = (TextView)itemView.findViewById(R.id.From);
        txtTo       = (TextView)itemView.findViewById(R.id.To);
        txtRoute    = (TextView)itemView.findViewById(R.id.Route);
    }
}