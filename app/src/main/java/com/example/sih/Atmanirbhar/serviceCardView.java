package com.example.sih.Atmanirbhar;


public class serviceCardView {
    private String JobName;
    private String Description;
    private String Phone;
    private String Days;

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

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }
}