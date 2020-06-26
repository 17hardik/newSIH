package com.example.sih;

public class data_in_cardview {
    private String company_logo;
    private String job_Post;
    private String company_name;
    private String company_location;
    private String salary;

    public data_in_cardview() {
    }

    public data_in_cardview(String company_logo, String job_Post, String company_name, String company_location, String salary) {
        this.company_logo = company_logo;
        this.job_Post = job_Post;
        this.company_name = company_name;
        this.company_location = company_location;
        this.salary = salary;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getJob_Post() {
        return job_Post;
    }

    public void setJob_Post(String job_Post) {
        this.job_Post = job_Post;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_location() {
        return company_location;
    }

    public void setCompany_location(String company_location) {
        this.company_location = company_location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
