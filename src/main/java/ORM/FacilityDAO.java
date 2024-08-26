package main.java.ORM;

import main.java.DomainModel.Facility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;



public class FacilityDAO {
    private Connection connection;

    //methods

    public void addFacility(String name, String address, String city, String province, String zip, String country, String telephone, String image, int idOwner) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("INSERT INTO \"Facility\" (name, address, city, province, zip, country, n_managers, n_fields, telephone, image, WH_Mon, WH_Tue, WH_Wed, WH_Thu, WH_Fri, WH_Sat, WH_Sun, id_owner)) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%d', '%d', '%s', '%s', '%d')", name, address, city, province, zip, country, 0,0,telephone,image,idOwner);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO cascade delete?
    public void deleteFacility(int idFacility) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("DELETE FROM \"Facility\" WHERE id = '%s'", idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public Facility getFacility(int idFacility) throws SQLException, ClassNotFoundException {
        //default id (id not found)
        Facility facility = null;

        String querySQL = String.format("SELECT * FROM \"Facility\" WHERE id = '%d'", idFacility);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            //FIXME check attributes
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            String city = resultSet.getString("city");
            String province = resultSet.getString("province");
            String zip = resultSet.getString("zip");
            String country = resultSet.getString("country");
            int nManagers = resultSet.getInt("n_managers");
            int nFields = resultSet.getInt("n_fields"); //TODO is useful?
            String telephone = resultSet.getString("telephone");
            String image = resultSet.getString("image");
            int idOwner = resultSet.getInt("id_owner");

            //TODO fill WH array and pass it to Facility constructor

            facility = new Facility(id, name, address, city, province, zip, country, nManagers, telephone,image,idOwner);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return facility;
    }

    public ArrayList<Facility> getFacilities() throws SQLException, ClassNotFoundException {
        ArrayList<Facility> facilities = new ArrayList<>();

        String querySQL = "SELECT * FROM \"Facility\" ORDER BY id ASC";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                facilities.add(this.getFacility(resultSet.getInt("id")));
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return facilities;
    }

    public ArrayList<Facility> getFacilitiesByOwner(int idOwnerTarget) throws SQLException, ClassNotFoundException {
        ArrayList<Facility> facilities = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Facility\" WHERE id_owner = '%d'", idOwnerTarget);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                facilities.add(this.getFacility(resultSet.getInt("id")));
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return facilities;
    }

    public int countFacilitiesByOwner(int idOwner) throws SQLException, ClassNotFoundException {
        //default value (results not found)
        int nFacilities = -1;

        String querySQL = String.format("SELECT count(*) AS total_facilities FROM \"Facility\" WHERE id_owner = '%d'", idOwner);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            nFacilities = resultSet.getInt("total_facilities");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return nFacilities;
    }

    public void updateName(int idFacility, String newName) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET name = '%s' WHERE id = '%d'", newName,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility name updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO check type
    public void updateDescription(int idFacility, String newDescription) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET description = '%s' WHERE id = '%d'", newDescription,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility description updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateAddress(int idFacility, String newAddress) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET address = '%s' WHERE id = '%d'", newAddress,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility address updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateCity(int idFacility, String newCity) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET city = '%s' WHERE id = '%d'", newCity,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility city updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateProvince(int idFacility, String newProvince) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET province = '%s' WHERE id = '%d'", newProvince,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility province updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateZip(int idFacility, String newZip) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET zip = '%s' WHERE id = '%d'", newZip,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility zip updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateCountry(int idFacility, String newCountry) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET country = '%s' WHERE id = '%d'", newCountry,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility country updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateTelephone(int idFacility, String newTelephone) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET telephone = '%s' WHERE id = '%d'", newTelephone,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility telephone updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO check type
    public void updateImage(int idFacility, String newImage) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("UPDATE \"Facility\" SET image = '%s' WHERE id = '%d'", newImage,idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility image updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }


    public int getNMangers(int idFacility) throws SQLException, ClassNotFoundException {
        //default value (results not found)
        int nManagers = -1;

        String querySQL = String.format("SELECT n_managers FROM \"Facility\" WHERE id = '%d'", idFacility);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            nManagers = resultSet.getInt("n_managers");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return nManagers;
    }

    public int getNFields(int idFacility) throws SQLException, ClassNotFoundException {
        //default value (results not found)
        int nFields = -1;

        String querySQL = String.format("SELECT n_fields FROM \"Facility\" WHERE id = '%d'", idFacility);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            nFields = resultSet.getInt("n_fields");

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return nFields;
    }

    public ArrayList<Facility> getFacilitiesByProvince(String provinceTarget) throws SQLException, ClassNotFoundException {
        ArrayList<Facility> facilities = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Facility\" WHERE province = '%s'", provinceTarget);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                facilities.add(this.getFacility(resultSet.getInt("id")));
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
