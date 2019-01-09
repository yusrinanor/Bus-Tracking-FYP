package com.example.android.fyp;

public class Report {
    private String from;
    private String to;
    private String route;
    private String date;
    private String time;

    public Report() {
    }

    public Report(String from, String to, String route, String date, String time) {
        this.from = from;
        this.to = to;
        this.route = route;
        this.date = date;
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
