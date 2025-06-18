package com.example.appdoctruyen;

public class Account {
    private String username;
    private String password;
    private String phone;
    private String birthYear;
    private String gender;
    private String email;

    // Constructor mặc định
    public Account() {
    }

    // Constructor với tất cả các trường
    public Account(String username, String password, String phone, String birthYear, String gender, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.birthYear = birthYear;
        this.gender = gender;
        this.email = email;
    }

    // Constructor ban đầu (chỉ username và password)
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.phone = "";
        this.birthYear = "";
        this.gender = "";
        this.email = "";
    }

    // Getter và Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}