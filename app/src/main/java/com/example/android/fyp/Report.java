package com.example.android.fyp;

public class Report {
    private String from;
    private String to;
    private String route;
    private long datetime;

    public Report() {
    }

    public Report(String from, String to, String route, long datetime) {
        this.from = from;
        this.to = to;
        this.route = route;
        this.datetime = datetime;
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

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
