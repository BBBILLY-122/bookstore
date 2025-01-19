package org.zr.shudian.model;

public class User {
    private String id;
    private String name;
    private String gender;
    private int age;
    private String identity;
    private String idNumber;
    private String phone;
    private String email;
    private String password;

    public User() {}

    public User(String id, String name, String gender, int age, String identity, 
               String idNumber, String phone, String email, String password) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.identity = identity;
        this.idNumber = idNumber;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 