package com.example.sih;


public class data_in_cardview {
    private String company_logo;
    private String Job_Post;
    private String Company_Name;
    private String Location;
    private String Job_Type;
    private String ID;
    private String domain_type;

    public data_in_cardview() {
    }

    public data_in_cardview(String company_logo, String job_Post, String company_Name, String location, String job_Type, String ID, String domain_type) {
        this.company_logo = company_logo;
        Job_Post = job_Post;
        Company_Name = company_Name;
        Location = location;
        Job_Type = job_Type;
        this.ID = ID;
        this.domain_type = domain_type;
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

    public String getJob_Type() {
        return Job_Type;
    }

    public void setJob_Type(String job_Type) {
        Job_Type = job_Type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDomain_type() {
        return domain_type;
    }

    public void setDomain_type(String domain_type) {
        this.domain_type = domain_type;
    }
}


