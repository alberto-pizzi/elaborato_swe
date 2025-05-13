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
    public Owner login(String username, String notEncodedPassword) throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        Owner owner = null;
        if(checkPassword(username, notEncodedPassword)){
            owner = dao.getOwner(username);
        }
        return owner;
    }

    @Override
    public boolean register(String username, String email, String password, String city, String province, String zip, String country){
        try {
            dao.addOwner(username,email,PasswordEncoder.hashPassword(password),city,province,zip,country);
        } catch (SQLException | NoSuchAlgorithmException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPassword(String username, String notEncodedPassword) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        boolean verified;
        verified = PasswordEncoder.verifyPassword(notEncodedPassword,dao.getEncodedPassword(username));
        return verified;
    }

    @Override
    public boolean checkPersonExistence(String username) throws SQLException{
        Owner owner1;
        owner1 = dao.getOwner(username);
        return (owner1 != null);
    }

    @Override
    public boolean checkEmail(String emailEntered) throws SQLException{
        boolean verified;
        verified = dao.checkEmailExistence(emailEntered);
        return verified;
    }

}