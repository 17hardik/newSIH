package com.example.sih.Jobs;

public class jobPost {
    private String title;
    private String description;
    private String experience;
    private String city;
    private String email;

    public jobPost() {
    }

    public jobPost(String title, String description, String experience, String city, String email) {
        this.title = title;
        this.description = description;
        this.experience = experience;
        this.city = city;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
