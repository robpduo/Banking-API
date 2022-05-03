package com.revature.models;

/*User Model that includes the user's personal information*/
public class User {
    private String email;
    private String firstName;
    private String lastName;

    private String address;
    private long socialNum;
    private long phoneNum;

    /*no arg constructor*/
    public User() {
    }

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String firstName, String lastName, long socialNum) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialNum = socialNum;
    }

    public User(String email, String firstName, String lastName, String address, long socialNum, long phoneNum) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.socialNum = socialNum;
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public long getSocialNum() {
        return socialNum;
    }

    public void setSocialNum(long socialNum) {
        this.socialNum = socialNum;
    }

    public long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", socialNum=" + socialNum +
                ", phoneNum=" + phoneNum +
                '}';
    }
}
