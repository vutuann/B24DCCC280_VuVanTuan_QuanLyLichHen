package com.example.th3;

public class Appointment {
    private int id;
    private String title;
    private String location;
    private String date; // dạng yyyy-MM-dd
    private String time; // dạng HH:mm
    private boolean reminder;

    public Appointment() {
    }

    public Appointment(String title, String location, String date, String time, boolean reminder) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.reminder = reminder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    // Đổi ngày yyyy-MM-dd thành dd/MM/yyyy · HH:mm để hiển thị
    public String getDisplayDateTime() {
        String[] parts = date.split("-"); // parts[0] = năm, parts[1] = tháng, parts[2] = ngày
        return parts[2] + "/" + parts[1] + "/" + parts[0] + " · " + time;
    }
}
