package com.example.android.fyp;

public class Feedback {
    private String message, date, category;

    public Feedback() {
    }

    public Feedback(String message, String date, String category) {
        this.message = message;
        this.date = date;
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
