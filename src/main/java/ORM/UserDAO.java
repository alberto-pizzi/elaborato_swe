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

    //todo check
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

    //todo check
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
