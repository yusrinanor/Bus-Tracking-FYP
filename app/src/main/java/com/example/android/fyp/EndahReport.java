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

public class EndahReport extends Fragment {

    private View view;
    private RecyclerView reportRecyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapter;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference reportRef;
    private Button thisWeekButt, allButt, lastWeekButt;
    private FloatingActionButton switchbutt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.endahreport, container, false);
        // get current layout activity
        layoutManager = new LinearLayoutManager(getActivity());
        // initialize recyclerview
        reportRecyclerView = (RecyclerView) view.findViewById(R.id.reportRecycler);
        // set size of recyclerview to be fixed as the content goes on
        reportRecyclerView.setHasFixedSize(true);
        // initialize firebase database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        // get report reference
        reportRef = mDatabaseRef.child("Report");
        //initialize adapter containing report appending card in report holder with the endah query filter
        reportAdapter = new FirebaseRecyclerAdapter<Report, ReportHolder>(
                Report.class, R.layout.cardviewreport, ReportHolder.class, reportRef.orderByChild("route").equalTo("ENDAH-APU") // select * from Report where route = endah-apu
        ) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText(viewHolder.txtDate.getText().toString() + ": " + model.getDate());
                viewHolder.txtTime.setText(viewHolder.txtTime.getText().toString() + " " + model.getTime());
                viewHolder.txtFrom.setText(viewHolder.txtFrom.getText().toString() + " " + model.getFrom());
                viewHolder.txtTo.setText(viewHolder.txtTo.getText().toString() + " " + model.getTo());
                viewHolder.txtRoute.setText(viewHolder.txtRoute.getText().toString() + ": " + model.getRoute());
            }
        };
        reportAdapter.notifyDataSetChanged();
        reportRecyclerView.setLayoutManager(layoutManager);
        reportRecyclerView.setAdapter(reportAdapter);

        //create filter

        thisWeekButt = (Button) view.findViewById(R.id.thisWeek);
        allButt = (Button) view.findViewById(R.id.all);
        lastWeekButt = (Button) view.findViewById(R.id.lastWeek);
        switchbutt = (FloatingActionButton) view.findViewById(R.id.switchToFrom);

        thisWeekButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return view;
    }
}