package main.java.ORM;

import main.java.DomainModel.Owner;
import main.java.DomainModel.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OwnerDAO extends PersonDAO {

    public void addOwner(String username, String email, String password, String city, String province, String zip, String country) throws SQLException, ClassNotFoundException {

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
    public void deleteOwner(int idOwner)throws SQLException, ClassNotFoundException {

        String querySQL = String.format("DELETE FROM \"Owner\" WHERE id = '%s'", idOwner);

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

    //TODO getOwner() input parameter?

    public Owner getOwner(int idOwner) throws SQLException, ClassNotFoundException {
        Owner owner = null;

        String querySQL = String.format("SELECT * FROM \"Owner\" WHERE id = '%d'", idOwner);

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


}
