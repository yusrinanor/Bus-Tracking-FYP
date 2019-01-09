package com.example.android.fyp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SouthcityAdmin extends Fragment {

    public ListView listView;
    public ListView listView2;
    public RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase mDatabase;
    private DatabaseReference toAPU;
    private DatabaseReference fromAPU;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vista, container, false);
        mDatabase = FirebaseDatabase.getInstance();
        fromAPU = mDatabase.getReference("Schedules").child("Apu-to-southcity");
        toAPU = mDatabase.getReference("Schedules").child("Southcity-to-apu");
        listView = (ListView) view.findViewById(R.id.toAPURecycler);
        listView2 = (ListView) view.findViewById(R.id.fromAPURecycler);

        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(getActivity(), String.class,R.layout.card_viewadmin, toAPU.orderByKey()
        ){
            TextView text_holder;


            @Override
            protected void populateView(View v, String model, final int position) {
                Button dBut;
                dBut = (Button) v.findViewById(R.id.del_button);
                dBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "Removed", Toast.LENGTH_SHORT).show();
                        getRef(position).removeValue();
                    }
                });

                text_holder = (TextView) v.findViewById(R.id.text_holder);
                text_holder.setText(getRef(position).getKey());
            }
        };

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        toAPU.keepSynced(true);
        FirebaseListAdapter<String> adapter2 = new FirebaseListAdapter<String>(getActivity(), String.class,R.layout.card_viewadmin, fromAPU.orderByKey()
        ){
            TextView text_holder;

            @Override
            protected void populateView(View v, String model, final int position) {

                Button dBut;
                dBut = (Button) v.findViewById(R.id.del_button);
                dBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "Removed", Toast.LENGTH_SHORT).show();
                        getRef(position).removeValue();
                    }
                });

                text_holder = (TextView) v.findViewById(R.id.text_holder);
                text_holder.setText(getRef(position).getKey());
            }
        };

        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        fromAPU.keepSynced(true);
        Utility.setListViewHeightBasedOnChildren(listView);
        Utility.setListViewHeightBasedOnChildren(listView2);
        return view;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mTime = (TextView) itemView.findViewById(R.id.text_holder);

        }
    }
}
