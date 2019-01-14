package com.example.android.fyp;

import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Locale;

public class Utility {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String getDateString(long datetime) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(datetime);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static String getTimeString(long datetime) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(datetime);
        String date = DateFormat.format("HH:mm", cal).toString();
        return date;
    }

    public static Long convertToTimestamp(Calendar datetime, int addition) {
        datetime.add(Calendar.DAY_OF_YEAR, addition);
        return datetime.getTimeInMillis() / 1000;
    }
}
