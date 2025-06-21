package main.java.DomainModel;

import java.io.Serializable;

public abstract class Person implements Serializable {
    private int id;
    private String email;
    private String username;
    private String password;;
    private String city;
    private String province;
    private String zip;
    private String country;
    private String target;

    //constructor


    public Person(int id, String email, String username, String password, String city, String province, String zip, String country, String personType) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.city = city;
        this.province = province;
        this.zip = zip;
        this.country = country;
        this.target = personType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
