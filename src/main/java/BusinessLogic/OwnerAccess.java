package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class OwnerAccess implements AccessStrategy{

    private OwnerDAO dao;

    public OwnerAccess(){
        dao = new OwnerDAO();
    }

    public OwnerAccess(OwnerDAO ownerDAO) {
        this.dao = ownerDAO;
    }

    @Override
    public Owner login(String username) throws SQLException {

        Owner owner = null;
        try {
            owner = dao.getOwner(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return owner;
    }

    @Override
    public boolean register(String username, String email, String password, String city, String province, String zip, String country) throws SQLException{

        try {
            dao.addOwner(username,email,PasswordEncoder.hashPassword(password),city,province,zip,country);

        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean checkPassword(String username, String notEncodedPassword) throws SQLException {

        boolean verified = false;
        try {
            verified = PasswordEncoder.verifyPassword(notEncodedPassword,dao.getEncodedPassword(username));
        } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return verified;
    }

    @Override
    public boolean checkPersonExistence(String username) throws SQLException{

        Owner owner1;
        try {
            owner1 = dao.getOwner(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (owner1 != null);
    }

    @Override
    public boolean checkEmail(String emailEntered) throws SQLException{

        boolean verified = false;

        try {
            verified = dao.checkEmailExistence(emailEntered);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return verified;
    }

}