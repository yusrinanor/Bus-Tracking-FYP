package com.example.android.fyp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class FeedbackViewHolder extends RecyclerView.ViewHolder {
    public TextView txtFeedbackID, txtFeedbackDate, txtCategory, txtMessage;

    public FeedbackViewHolder(View itemView) {
        super(itemView);
        txtFeedbackID = (TextView)itemView.findViewById(R.id.ID_Feedback);
        txtFeedbackDate = (TextView)itemView.findViewById(R.id.Date);
        txtCategory = (TextView)itemView.findViewById(R.id.Category_Feedback);
        txtMessage = (TextView)itemView.findViewById(R.id.Message_Feedback);
    }
}