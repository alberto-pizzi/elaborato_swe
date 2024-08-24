package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FacilityDAO {
    private Connection connection;

    //methods


    //TODO enter input parameter
    public void addFacility(String name, String address, String city, String province, String zip, String country, String telephone, String[] WH,String image, int idOwner) throws SQLException, ClassNotFoundException {

        //TODO optimize
        //check number of days correctness
        int daysInAWeek = 7;
        if (WH.length != daysInAWeek){
            for (int i = 0; i < daysInAWeek; i++) {
                WH[i] = "Error";
            }
        }

        String querySQL = String.format("INSERT INTO \"Facility\" (name, address, city, province, zip, country, n_managers, n_fields, telephone, image, WH_Mon, WH_Tue, WH_Wed, WH_Thu, WH_Fri, WH_Sat, WH_Sun, id_owner)) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%d', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%d')", name, address, city, province, zip, country, 0,0,telephone,image,WH[0],WH[1],WH[2],WH[3],WH[4],WH[5],WH[6],idOwner);

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
            String[] WH = new String[7];
            WH[0] = resultSet.getString("WH_Mon");
            WH[1] = resultSet.getString("WH_Tue");
            WH[2] = resultSet.getString("WH_Wed");
            WH[3] = resultSet.getString("WH_Thu");
            WH[4] = resultSet.getString("WH_Fri");
            WH[5] = resultSet.getString("WH_Sat");
            WH[6] = resultSet.getString("WH_Sun");
            int idOwner = resultSet.getInt("id_owner");

            facility = new Facility(id, name, address, city, province, zip, country, nManagers, telephone,image,WH,idOwner);

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

    public ArrayList<Facility> getFacilitiesByOwner(int idOwnerTarget) throws SQLException, ClassNotFoundException {
        ArrayList<Facility> facilities = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Facility\" WHERE id_owner = '%d'", idOwnerTarget);

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

    public void updateName(int idFacility, String newName) {
    }

    //TODO check type
    public void updateDescription(int idFacility, String newDescription) {
    }

    public void updateAddress(int idFacility, String newAddress) {
    }

    public void updateCity(int idFacility, String newCity) {
    }

    public void updateProvince(int idFacility, String newProvince) {
    }

    public void updateZip(int idFacility, String newZip) {
    }

    public void updateCountry(int idFacility, String newCountry) {
    }

    public void updateTelephone(int idFacility, String newTelephone) {
    }

    //TODO check type
    public void updateImage(int idFacility, String newImage) {
    }

    //TODO check type
    public void updateWH(int idFacility, String newWH) {}

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


}
