package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.User;

import java.sql.*;
import java.util.ArrayList;

public class ManagesDAO extends ConnectionHolder{
    //methods

    public void attachManager(int idManager, int idFacility) throws SQLException {

        String insertQuerySQL = String.format("INSERT INTO \"Manages\" (id_facility, id_user) " +
                "VALUES ('%d', '%d')", idFacility,idManager);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insertQuerySQL);
            preparedStatement.executeUpdate();

            System.out.println("Manager added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void detachManager(int idManager, int idFacility) throws SQLException {

        String deleteQuerySQL = String.format("DELETE FROM \"Manages\" WHERE id_facility = '%d' AND id_user = '%d'", idFacility,idManager);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(deleteQuerySQL);
            preparedStatement.executeUpdate();
            System.out.println("Membership removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public ArrayList<User> getAllManagersByFacility(int idFacility) throws SQLException {
        ArrayList<User> managers = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Manages\" INNER JOIN \"User\" ON \"Manages\".id_user = \"User\".id WHERE id_facility = '%d'", idFacility);

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

                managers.add(new User(id, email, username, password, city, province, zip, country));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return managers;
    }

    public ArrayList<Facility> getAllFacilitiesByManager(int idManager) throws SQLException {
        ArrayList<Facility> facilities = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Manages\" INNER JOIN \"Facility\" ON \"Manages\".id_facility = \"Facility\".id WHERE id_user = '%d'", idManager);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            FacilityDAO facilityDAO = new FacilityDAO();
            while (resultSet.next()) {

                facilities.add(facilityDAO.getFacility(resultSet.getInt("id"), false));

            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return facilities;
    }
}
