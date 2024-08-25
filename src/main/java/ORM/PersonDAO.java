package main.java.ORM;

import main.java.DomainModel.User;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class PersonDAO {
    protected Connection connection;
    protected String target;

    //getter
    public String getTarget() {
        return target;
    }

    //setter
    public void setTarget(String target) {
        this.target = target;
    }

    //constructor
    public PersonDAO() {
        //TODO fill attributes

        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }


    //methods
    public void addUser(String username, String email, String password, String city, String province, String zip, String country) throws SQLException, ClassNotFoundException {

        //TODO check not mandatory parameters

        String querySQL = String.format("INSERT INTO \"User\" (email, username, city, province, zip, country, password)) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s',)", email, username, city, province, zip, country, password);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO cascade delete?
    public void deleteUser(String username) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("DELETE FROM \"User\" WHERE username = '%s'", username);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("User removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateUsername(String username, String newUsername) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"" + this.target + "\" SET username = '%s' WHERE id = '%d'", newUsername, username);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Username updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateEmail(int idUser, String newEmail) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"" + this.target + "\" SET email = '%s' WHERE id = '%d'", newEmail, idUser);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Email updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"" + this.target + "\" SET password = '%s' WHERE username = '%s'", newPassword, username);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Password updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateAddress(String username, String city, String province, String zip, String country) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"" + this.target + "\" SET city = '%s', province = '%s', zip = '%s', country = '%s' WHERE username = '%s'", city, province, zip, country, username);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Address updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO  implement it (BOOL)
    public boolean checkPassword(String username, String passwordEntered){
        return false;
    }

    public ArrayList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();

        String querySQL = "SELECT * FROM \"User\" ORDER BY id ASC";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String city = resultSet.getString("city");
                String province = resultSet.getString("province");
                String zip = resultSet.getString("zip");
                String country = resultSet.getString("country");

                users.add(new User(id, email, username, city, province, zip, country, password));

            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return users;
    }

    public int getUserID(String username) throws SQLException, ClassNotFoundException {
        //default id (id not found)
        int id = -1;

        String querySQL = String.format("SELECT id FROM \"User\" WHERE username = '%s'", username);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            id = resultSet.getInt("id");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return id;
    }


    public User getUser(String username) throws SQLException, ClassNotFoundException {
        //default id (id not found)
        User user = null;

        String querySQL = String.format("SELECT * FROM \"User\" WHERE username = '%s'", username);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            String usernameSelected = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String city = resultSet.getString("city");
            String province = resultSet.getString("province");
            String zip = resultSet.getString("zip");
            String country = resultSet.getString("country");
            user = new User(id, email, usernameSelected, city, province, zip, country, password);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return user;
    }


}
