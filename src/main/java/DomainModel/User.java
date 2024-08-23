package main.java.DomainModel;

public class User extends Person{
    public User(int id, String email, String username, String password, String city, String province, String zip, String country) {
        super(id, email, username, password, city, province, zip, country);
    }
}
