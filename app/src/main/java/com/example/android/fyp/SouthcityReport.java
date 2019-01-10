package com.example.android.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SouthcityReport extends Fragment {

    private View view;
    private RecyclerView reportRecyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Report, ReportHolder> reportAdapter;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference reportRef;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.scpreport, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        reportRecyclerView = (RecyclerView) view.findViewById(R.id.reportRecycler);
        reportRecyclerView.setHasFixedSize(true);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        reportRef = mDatabaseReference.child("Report");
        reportAdapter = new FirebaseRecyclerAdapter<Report, ReportHolder>(Report.class, R.layout.cardviewreport, ReportHolder.class,  reportRef.orderByChild("route").equalTo("SCP-APU")) {
            @Override
            protected void populateViewHolder(ReportHolder viewHolder, Report model, int position) {
                viewHolder.txtDate.setText(viewHolder.txtDate.getText().toString() + ":" + model.getDate());
                viewHolder.txtFrom.setText(viewHolder.txtFrom.getText().toString() + ":" + model.getFrom());
                viewHolder.txtTo.setText(viewHolder.txtTo.getText().toString() + ":" + model.getTo());
                viewHolder.txtRoute.setText(viewHolder.txtRoute.getText().toString() + ":" + model.getRoute());
                viewHolder.txtTime.setText(viewHolder.txtTime.getText().toString() + ":" + model.getTime());


            }
        };
        reportAdapter.notifyDataSetChanged();
        reportRecyclerView.setLayoutManager(layoutManager);
        reportRecyclerView.setAdapter(reportAdapter);
        return view;
    }
}