package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

import java.sql.SQLException;

public class UserAccess implements AccessStrategy{
    @Override
    public User login(String username) throws SQLException {

        UserDAO dao = new UserDAO();
        User user = null;
        try {
            user = dao.getUser(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public boolean register(String username, String email, String password, String city, String province, String zip, String country) throws SQLException{

        UserDAO dao = new UserDAO();

        try {
            dao.addUser(username,email,password,city,province,zip,country);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean checkPassword(java.lang.String username, java.lang.String password) throws SQLException {

        boolean verified = false;
        UserDAO dao = new UserDAO();
        try {
            verified = dao.checkPassword(username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return verified;
    }

    @Override
    public boolean checkPersonExistence(String username) throws SQLException{

        UserDAO userDAO = new UserDAO();
        User user;
        try {
            user = userDAO.getUser(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (user != null);
    }

    @Override
    public boolean checkEmail(String emailEntered) throws SQLException{

        UserDAO userDAO = new UserDAO();
        boolean verified = false;

        try {
            verified = userDAO.checkEmailExistence(emailEntered);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return verified;
    }

}
