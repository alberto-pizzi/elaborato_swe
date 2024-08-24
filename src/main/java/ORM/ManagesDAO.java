package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ManagesDAO {

    private Connection connection;

    //methods

    public void attachManager(int idManager, int idFacility) throws SQLException, ClassNotFoundException {


        String insertQuerySQL = String.format("INSERT INTO \"Manages\" (id_facility, id_user)) " +
                "VALUES ('%d', '%d')", idFacility,idManager);

        String updateSQL = String.format("UPDATE \"Facility\" SET n_managers = n_managers + 1 WHERE id = '%d'", idFacility);


        PreparedStatement preparedStatementForInsert = null;
        PreparedStatement preparedStatementForUpdate = null;

        try {
            //first query
            preparedStatementForInsert = connection.prepareStatement(insertQuerySQL);
            int facilityRowsAffectedForInsert = preparedStatementForInsert.executeUpdate();

            if (facilityRowsAffectedForInsert == 0){
                throw new SQLException("Insert on Facility failed, no rows affected.");
            }

            //second query
            preparedStatementForUpdate = connection.prepareStatement(updateSQL);
            int facilityRowsAffectedForUpdate = preparedStatementForUpdate.executeUpdate();

            if (facilityRowsAffectedForUpdate == 0){
                throw new SQLException("Update on Facility failed, no rows affected.");
            }

            connection.commit();
            System.out.println("New manager attached successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Transaction failed, rolled back.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();

        } finally {
            try {
                if (preparedStatementForInsert != null) {
                    preparedStatementForInsert.close();
                }
                if (preparedStatementForUpdate != null) {
                    preparedStatementForUpdate.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void detachManager(int idManager, int idFacility) throws SQLException, ClassNotFoundException {


        String deleteQuerySQL = String.format("DELETE FROM \"Manages\" WHERE id_facility = '%d' AND id_user = '%d'", idFacility,idManager);

        String updateSQL = String.format("UPDATE \"Facility\" SET n_managers = n_managers - 1 WHERE id = '%d'", idFacility);


        PreparedStatement preparedStatementForDelete = null;
        PreparedStatement preparedStatementForUpdate = null;

        try {
            //first query
            preparedStatementForDelete = connection.prepareStatement(deleteQuerySQL);
            int facilityRowsAffectedForInsert = preparedStatementForDelete.executeUpdate();

            if (facilityRowsAffectedForInsert == 0){
                throw new SQLException("Delete from Facility failed, no rows affected.");
            }

            //second query
            preparedStatementForUpdate = connection.prepareStatement(updateSQL);
            int facilityRowsAffectedForUpdate = preparedStatementForUpdate.executeUpdate();

            if (facilityRowsAffectedForUpdate == 0){
                throw new SQLException("Update on Facility failed, no rows affected.");
            }

            connection.commit();
            System.out.println("Manager detached successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Transaction failed, rolled back.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();

        } finally {
            try {
                if (preparedStatementForDelete != null) {
                    preparedStatementForDelete.close();
                }
                if (preparedStatementForUpdate != null) {
                    preparedStatementForUpdate.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public ArrayList<User> getAllManagersByFacility(int idFacility) throws SQLException, ClassNotFoundException {
        ArrayList<User> managers = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Manages\" INNER JOIN \"User\" ON Manages.id_user = User.id WHERE id_facility = '%d'", idFacility);

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

                managers.add(new User(id, email, username, city, province, zip, country, password));

            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return managers;
    }

    public ArrayList<Facility> getAllFacilitiesByManager(int idManager) throws SQLException, ClassNotFoundException {
        ArrayList<Facility> facilities = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Manages\" INNER JOIN \"Facility\" ON Manages.id_facility = Facility.id WHERE id_user = '%d'", idManager);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String province = resultSet.getString("province");
                String zip = resultSet.getString("zip");
                String country = resultSet.getString("country");
                int nManagers = resultSet.getInt("n_managers");
                int nFields = resultSet.getInt("n_fields");
                String telephone = resultSet.getString("telephone");
                String image = resultSet.getString("image");
                String[] WH = new String[7];
                WH[0] = resultSet.getString("WH_Mon");
                WH[1] = resultSet.getString("WH_Tue");
                WH[2] = resultSet.getString("WH_Wed");
                WH[3] = resultSet.getString("WH_Thu");
                WH[4] = resultSet.getString("WH_Fri");
                WH[5] = resultSet.getString("WH_Sat");
                WH[6] = resultSet.getString("WH_Sun");
                int idOwner = resultSet.getInt("id_owner");

                facilities.add(new Facility(id, name, address, city, province, zip, country, nManagers, telephone,image,WH,idOwner));

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
