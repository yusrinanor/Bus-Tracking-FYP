package com.example.android.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static com.example.android.fyp.Utility.convertToTimestamp;
import static com.example.android.fyp.Utility.getDateString;
import static com.example.android.fyp.Utility.getTimeString;

public class EndahReport extends Fragment {
    private View view;
    private RecyclerView reportRecyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterAllFrom;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterAllTo;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterThisWeek;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapterLastWeek;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference reportRef;
    private Button thisWeekButt, allButt, lastWeekButt;
    private FloatingActionButton switchbutt;
    private TextView filter_text;
    private long date_now;
    private long date_this_week;
    private long date_last_week;
    private String destination_switch = "from";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.endahreport, container, false);
        // get current layout activity
        layoutManager = new LinearLayoutManager(getActivity());
        // initialize recyclerview
        reportRecyclerView = (RecyclerView) view.findViewById(R.id.reportRecycler);
        //display text when switch button is clicked
        filter_text = (TextView) view.findViewById(R.id.filter_text);
        // set size of recyclerview to be fixed as the content goes on
        reportRecyclerView.setHasFixedSize(true);
        // initialize firebase database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        // get report reference
        reportRef = mDatabaseRef.child("Report");
        Calendar cal = Calendar.getInstance();
        date_now = convertToTimestamp(cal, 0);
        date_this_week = convertToTimestamp(cal, -7);
        date_last_week = convertToTimestamp(cal, -14);
        //initialize adapter containing report appending card in report holder with the endah query filter
        reportAdapterAllFrom = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.orderByChild("route").equalTo("ENDAH-APU").orderByChild("from") // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText(viewHolder.txtDate.getText().toString() + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText(viewHolder.txtTime.getText().toString() + " " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText(viewHolder.txtFrom.getText().toString() + " " + model.getFrom());
                viewHolder.txtTo.setText(viewHolder.txtTo.getText().toString() + " " + model.getTo());
                viewHolder.txtRoute.setText(viewHolder.txtRoute.getText().toString() + ": " + model.getRoute());
            }
        };
        reportAdapterAllFrom.notifyDataSetChanged();

        reportAdapterAllTo = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.orderByChild("route").equalTo("ENDAH-APU").orderByChild("to") // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText(viewHolder.txtDate.getText().toString() + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText(viewHolder.txtTime.getText().toString() + " " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText(viewHolder.txtFrom.getText().toString() + " " + model.getFrom());
                viewHolder.txtTo.setText(viewHolder.txtTo.getText().toString() + " " + model.getTo());
                viewHolder.txtRoute.setText(viewHolder.txtRoute.getText().toString() + ": " + model.getRoute());
            }
        };
        reportAdapterAllTo.notifyDataSetChanged();

        reportAdapterThisWeek = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.orderByChild("route").equalTo("ENDAH-APU").orderByChild("datetime").startAt(date_this_week).endAt(date_now) // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText(viewHolder.txtDate.getText().toString() + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText(viewHolder.txtTime.getText().toString() + " " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText(viewHolder.txtFrom.getText().toString() + " " + model.getFrom());
                viewHolder.txtTo.setText(viewHolder.txtTo.getText().toString() + " " + model.getTo());
                viewHolder.txtRoute.setText(viewHolder.txtRoute.getText().toString() + ": " + model.getRoute());
            }
        };
        reportAdapterThisWeek.notifyDataSetChanged();

        reportAdapterLastWeek = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.orderByChild("route").equalTo("ENDAH-APU").orderByChild("datetime").startAt(date_last_week).endAt(date_this_week) // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText(viewHolder.txtDate.getText().toString() + ": " + getDateString(model.getDatetime() * 1000));
                viewHolder.txtTime.setText(viewHolder.txtTime.getText().toString() + " " + getTimeString(model.getDatetime() * 1000));
                viewHolder.txtFrom.setText(viewHolder.txtFrom.getText().toString() + " " + model.getFrom());
                viewHolder.txtTo.setText(viewHolder.txtTo.getText().toString() + " " + model.getTo());
                viewHolder.txtRoute.setText(viewHolder.txtRoute.getText().toString() + ": " + model.getRoute());
            }
        };
        reportAdapterLastWeek.notifyDataSetChanged();


        reportRecyclerView.setLayoutManager(layoutManager);
        reportRecyclerView.setAdapter(reportAdapterAllFrom);

        //create filter

        thisWeekButt = (Button) view.findViewById(R.id.thisWeek);
        allButt = (Button) view.findViewById(R.id.all);
        lastWeekButt = (Button) view.findViewById(R.id.lastWeek);
        switchbutt = (FloatingActionButton) view.findViewById(R.id.switchToFrom);

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
                filter_text.setText("Last week");
            }
        });

        allButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportRecyclerView.swapAdapter(reportAdapterAllFrom, false);
                filter_text.setText("All from");
            }
        });

        switchbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination_switch == "from") {
                    destination_switch = "to";
                    reportRecyclerView.swapAdapter(reportAdapterAllTo, false);
                    filter_text.setText("All To");
                } else {
                    destination_switch = "from";
                    reportRecyclerView.swapAdapter(reportAdapterAllFrom, false);
                    filter_text.setText("All From");
                }
            }
        });

        return view;
    }
}