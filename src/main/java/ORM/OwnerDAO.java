package main.java.ORM;

import main.java.DomainModel.Owner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class OwnerDAO extends PersonDAO {


    public OwnerDAO() {
        super("Owner");
    }

    public int addOwner(String username, String email, String password, String city, String province, String zip, String country) throws SQLException {

        String querySQL = String.format("INSERT INTO \"Owner\" (email, username, city, province, zip, country, password) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", email, username, city, province, zip, country, password);

        int idAdded = 0;

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                idAdded = resultSet.getInt(1);
            }

            System.out.println("Owner added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
        return idAdded;
    }


    public Owner getOwner(String ownerUsername) throws SQLException {
        Owner owner = null;

        String querySQL = String.format("SELECT * FROM \"Owner\" WHERE username = '%s'", ownerUsername);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String city = resultSet.getString("city");
                String province = resultSet.getString("province");
                String zip = resultSet.getString("zip");
                String country = resultSet.getString("country");
                owner = new Owner(id, email, username, password, city, province, zip, country);
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return owner;
    }

    public Owner getOwnerByID(int idOwner) throws SQLException {
        Owner owner = null;

        String querySQL = String.format("SELECT * FROM \"Owner\" WHERE id = '%s'", idOwner);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //TODO optimize redundancy
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String city = resultSet.getString("city");
                String province = resultSet.getString("province");
                String zip = resultSet.getString("zip");
                String country = resultSet.getString("country");
                owner = new Owner(id, email, username, city, province, zip, country, password);
            }
            else{
                System.err.println("No owner found with id: " + idOwner);
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return owner;
    }

}
