package main.java.ORM;

import main.java.DomainModel.Owner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OwnerDAO extends PersonDAO {


    public OwnerDAO() {
        super("Owner");
    }

    public void addOwner(String username, String email, String password, String city, String province, String zip, String country) throws SQLException {

        //TODO check not mandatory parameters

        String querySQL = String.format("INSERT INTO \"Owner\" (email, username, city, province, zip, country, password)) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s',)", email, username, city, province, zip, country, password);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Owner added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO cascade delete?
    public void deleteOwner(String username)throws SQLException {

        String querySQL = String.format("DELETE FROM \"Owner\" WHERE id = '%s'", username);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Owner removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }


    public Owner getOwner(String ownerUsername) throws SQLException {
        Owner owner = null;

        String querySQL = String.format("SELECT * FROM \"Owner\" WHERE username = '%s'", ownerUsername);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String city = resultSet.getString("city");
            String province = resultSet.getString("province");
            String zip = resultSet.getString("zip");
            String country = resultSet.getString("country");
            owner = new Owner(id, email, username, city, province, zip, country, password);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return owner;
    }

    public int getOwnerID(String username) throws SQLException {
        //default id (id not found)
        int id = -1;

        String querySQL = String.format("SELECT id FROM \"Owner\" WHERE username = '%s'", username);

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



}
