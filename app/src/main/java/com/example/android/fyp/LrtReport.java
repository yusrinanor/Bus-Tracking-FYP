package com.example.android.fyp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.android.fyp.Utility.convertToTimestamp;
import static com.example.android.fyp.Utility.getDateString;
import static com.example.android.fyp.Utility.getTimeString;

import java.util.Calendar;

public class LrtReport extends Fragment {

    private View view;
    private RecyclerView reportRecyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterAllFrom;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterAllTo;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterThisWeek;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterLastWeek;
    private Button thisWeekButt, lastWeekButt, allButt;
    private FloatingActionButton switchbutt;
    private TextView filter_text;
    private long date_now;
    private long date_this_week;
    private long date_last_week;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference reportRef;
    private String destination_switch = "from";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lrtreport, container, false);

        layoutManager = new LinearLayoutManager(getActivity());
        reportRecyclerView = (RecyclerView) view.findViewById(R.id.reportRecycler);
        filter_text = (TextView)view.findViewById(R.id.filter_text);
        reportRecyclerView.setHasFixedSize(true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        reportRef = mDatabaseRef.child("Report");
        Calendar cal = Calendar.getInstance();
        date_now = convertToTimestamp(cal, 0);
        date_last_week = convertToTimestamp(cal, -14);
        date_this_week = convertToTimestamp(cal, -7);


        reportAdapterAllFrom = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.child("LRT-APU").orderByChild("from").equalTo("LRT") // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText("");
                viewHolder.txtTime.setText("");
                viewHolder.txtFrom.setText("");
                viewHolder.txtTo.setText("");
                viewHolder.txtRoute.setText("");
                viewHolder.txtDate.setText("Date"   + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText("Time"   + ": " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText("From"   + ": " + model.getFrom());
                viewHolder.txtTo.setText("To"       + ": " + model.getTo());
                viewHolder.txtRoute.setText("Route" + ": " + model.getRoute());
            }
        };


        reportAdapterThisWeek = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.child("LRT-APU").orderByChild("datetime").startAt(date_this_week).endAt(date_now) // select * from Report where route = scp-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText("");
                viewHolder.txtTime.setText("");
                viewHolder.txtFrom.setText("");
                viewHolder.txtTo.setText("");
                viewHolder.txtRoute.setText("");
                viewHolder.txtDate.setText("Date"   + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText("Time"   + ": " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText("From"   + ": " + model.getFrom());
                viewHolder.txtTo.setText("To"       + ": " + model.getTo());
                viewHolder.txtRoute.setText("Route" + ": " + model.getRoute());
            }
        };


        reportAdapterLastWeek = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.child("LRT-APU").orderByChild("datetime").startAt(date_last_week).endAt(date_this_week) // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText("");
                viewHolder.txtTime.setText("");
                viewHolder.txtFrom.setText("");
                viewHolder.txtTo.setText("");
                viewHolder.txtRoute.setText("");
                viewHolder.txtDate.setText("Date"   + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText("Time"   + ": " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText("From"   + ": " + model.getFrom());
                viewHolder.txtTo.setText("To"       + ": " + model.getTo());
                viewHolder.txtRoute.setText("Route" + ": " + model.getRoute());
            }
        };

        reportAdapterAllTo = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.child("LRT-APU").orderByChild("to").equalTo("LRT") // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText("");
                viewHolder.txtTime.setText("");
                viewHolder.txtFrom.setText("");
                viewHolder.txtTo.setText("");
                viewHolder.txtRoute.setText("");
                viewHolder.txtDate.setText("Date"   + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText("Time"   + ": " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText("From"   + ": " + model.getFrom());
                viewHolder.txtTo.setText("To"       + ": " + model.getTo());
                viewHolder.txtRoute.setText("Route" + ": " + model.getRoute());
            }
        };

        reportRecyclerView.setLayoutManager(layoutManager);
        reportRecyclerView.setAdapter(reportAdapterAllFrom);

        //create filter
        thisWeekButt = (Button) view.findViewById(R.id.thisWeek);
        lastWeekButt = (Button) view.findViewById(R.id.lastWeek);
        allButt = (Button) view.findViewById(R.id.ViewAll);
        switchbutt =  (FloatingActionButton) view.findViewById(R.id.switchToFrom);

        thisWeekButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportRecyclerView.swapAdapter(reportAdapterThisWeek, false);
                filter_text.setText("This week");
            }
        });

        lastWeekButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportRecyclerView.swapAdapter(reportAdapterLastWeek, false);
            }
        });

        allButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportRecyclerView.swapAdapter(reportAdapterAllFrom, false);
            }
        });

        switchbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(destination_switch == "from"){
                    destination_switch = "to";
                    reportRecyclerView.swapAdapter(reportAdapterAllTo, false);
                    reportAdapterAllTo.notifyDataSetChanged();
                    filter_text.setText("All To");
                } else {
                    destination_switch = "from";
                    reportRecyclerView.swapAdapter(reportAdapterAllFrom, false);
                    reportAdapterAllFrom.notifyDataSetChanged();
                    filter_text.setText("All From");
                }
            }
        });

        return view;
    }
}