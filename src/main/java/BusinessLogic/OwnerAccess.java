package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class OwnerAccess implements AccessStrategy{

    @Override
    public Owner login(String username) throws SQLException {

        OwnerDAO dao = new OwnerDAO();
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

        OwnerDAO dao = new OwnerDAO();

        try {
            dao.addOwner(username,email,PasswordEncoder.hashPassword(password),city,province,zip,country);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean checkPassword(String username, String notEncodedPassword) throws SQLException {

        boolean verified = false;
        OwnerDAO dao = new OwnerDAO();
        try {
            verified = PasswordEncoder.verifyPassword(notEncodedPassword,dao.getEncodedPassword(username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return verified;
    }

    @Override
    public boolean checkPersonExistence(String username) throws SQLException{

        OwnerDAO ownerDAO = new OwnerDAO();
        Owner owner1;
        try {
            owner1 = ownerDAO.getOwner(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (owner1 != null);
    }

    @Override
    public boolean checkEmail(String emailEntered) throws SQLException{

        boolean verified = false;
        OwnerDAO ownerDAO = new OwnerDAO();

        try {
            verified = ownerDAO.checkEmailExistence(emailEntered);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return verified;
    }

}