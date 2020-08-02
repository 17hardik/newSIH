package com.example.sih.Atmanirbhar;


public class serviceCardView {
    private String JobName;
    private String Description;
    private String Days;
    private String Phone;

    public serviceCardView() {
    }

    public serviceCardView(String jobname, String phone, String description, String days) {
        JobName = jobname;
        Description = description;
        Phone = phone;
        Days = days;
    }

    public String getJobName() {
        return JobName;
    }

    public String getDays() {
        return Days;
    }

    public String getDescription() {
        return Description;
    }

    public String getPhone() {
        return Phone;
    }

}