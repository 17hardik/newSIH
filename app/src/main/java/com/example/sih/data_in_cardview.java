package com.example.sih;

public class data_in_cardview {
    private String company_logo;
    private String Job_Post;
    private String Company_Name;
    private String Location;
    private String Salary_PA_in_Rs;
    private String Jobs_Types;
    private String Jobs_Description;
    private String Key_Skills;
    private String Qualification;
    private String Rating;
    private String Sector;

    public data_in_cardview() {
    }

    public void data_in_cardview(String company_logo, String job_Post, String company_Name, String location, String salary_PA_in_Rs, String jobs_Types, String jobs_Description, String key_Skills, String qualification, String rating, String sector) {
        this.company_logo = company_logo;
        Job_Post = job_Post;
        Company_Name = company_Name;
        Location = location;
        Salary_PA_in_Rs = salary_PA_in_Rs;
        Jobs_Types = jobs_Types;
        Jobs_Description = jobs_Description;
        Key_Skills = key_Skills;
        Qualification = qualification;
        Rating = rating;
        Sector = sector;
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

    public String getJobs_Types() {
        return Jobs_Types;
    }

    public void setJobs_Types(String jobs_Types) {
        Jobs_Types = jobs_Types;
    }

    public String getJobs_Description() {
        return Jobs_Description;
    }

    public void setJobs_Description(String jobs_Description) {
        Jobs_Description = jobs_Description;
    }

    public String getKey_Skills() {
        return Key_Skills;
    }

    public void setKey_Skills(String key_Skills) {
        Key_Skills = key_Skills;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getSector() {
        return Sector;
    }

    public void setSector(String sector) {
        Sector = sector;
    }
}
