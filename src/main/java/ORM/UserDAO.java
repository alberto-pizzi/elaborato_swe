package main.java.ORM;

import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;
import main.java.DomainModel.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends PersonDAO {
    public UserDAO() {
        super("User");
    }

    public int addUser(String username, String email, String password, String city, String province, String zip, String country) throws SQLException {


        String querySQL = String.format("INSERT INTO \"User\" (email, username, city, province, zip, country, password) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", email, username, city, province, zip, country, password);

        int idAdded = 0;

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();


            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                idAdded = resultSet.getInt(1);
            }

            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

        return idAdded;

    }

    public ArrayList<User> getUsersByProvinceSearch(String provinceUser) throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"User\" WHERE UPPER(province) LIKE UPPER('%s')", "%" + provinceUser + "%");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String usernameSelected = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String city = resultSet.getString("city");
                String province = resultSet.getString("province");
                String zip = resultSet.getString("zip");
                String country = resultSet.getString("country");
                users.add(new User(id, email, usernameSelected, password, city, province, zip, country));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return users;
    }

    public ArrayList<User> getUsersByUsernameSearch(String searchUsername) throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"User\" WHERE username LIKE '%s'", "%" + searchUsername + "%");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String usernameSelected = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String city = resultSet.getString("city");
                String province = resultSet.getString("province");
                String zip = resultSet.getString("zip");
                String country = resultSet.getString("country");
                users.add(new User(id, email, usernameSelected, password, city, province, zip, country));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return users;
    }

}
