package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserAccess implements AccessStrategy{

    private UserDAO dao;

    public UserAccess(){
        dao = new UserDAO();
    }

    public UserAccess(UserDAO userDAO){
        this.dao = userDAO;
    }

    @Override
    public User login(String username) throws SQLException {
        User user = null;
        user = dao.getUser(username);
        return user;
    }

    @Override
    public boolean register(String username, String email, String password, String city, String province, String zip, String country){

        try {
            dao.addUser(username,email,PasswordEncoder.hashPassword(password),city,province,zip,country);

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
        User user;
        user = dao.getUser(username);
        return (user != null);
    }

    @Override
    public boolean checkEmail(String emailEntered) throws SQLException{
        boolean verified;
        verified = dao.checkEmailExistence(emailEntered);
        return verified;
    }

}
