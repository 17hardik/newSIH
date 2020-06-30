package com.example.sih;

public class data_in_cardview {
    private String company_logo;
    private String Job_Post;
    private String Company_Name;
    private String Location;
    private String Salary_PA_in_Rs;

    public data_in_cardview() {
    }

    public data_in_cardview(String company_logo, String job_Post, String company_Name, String location, String salary_PA_in_Rs) {
        this.company_logo = company_logo;
        Job_Post = job_Post;
        Company_Name = company_Name;
        Location = location;
        Salary_PA_in_Rs = salary_PA_in_Rs;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getJob_Post() {
        return Job_Post;
    }

    public void setJob_Post(String job_Post) {
        Job_Post = job_Post;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSalary_PA_in_Rs() {
        return Salary_PA_in_Rs;
    }

    public void setSalary_PA_in_Rs(String salary_PA_in_Rs) {
        Salary_PA_in_Rs = salary_PA_in_Rs;
    }
}

