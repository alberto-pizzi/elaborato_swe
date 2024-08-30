package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.WorkingHours;
import main.java.DomainModel.WorkingHours.Day;

import java.sql.*;
import java.util.ArrayList;

public class WorkingHoursDAO {

    private Connection connection;

    //constructor
    public WorkingHoursDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //methods

    public void addWHToFacility(int idFacility, Day dayOfWeek, Time openingHours, Time closingHours) throws SQLException {

        String querySQL = String.format("INSERT INTO \"WH\" (day_of_week, opening, closing, id_facility)) " +
                "VALUES ('%s', '%tT', '%tT', '%d')", dayOfWeek, openingHours, closingHours, idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility's WH added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void removeWHFromFacility(int idWH) throws SQLException {

        String querySQL = String.format("DELETE FROM \"WH\" WHERE id = '%d'", idWH);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("selected WH removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void removeWHFromFacilityByDay(int idFacility, Day dayOfWeek) throws SQLException {

        String querySQL = String.format("DELETE FROM \"WH\" WHERE id_facility = '%d' AND day_of_week = '%s'", idFacility,dayOfWeek);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("selected WH removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void removeAllWHsByFacility(int idFacility) throws SQLException {

        String querySQL = String.format("DELETE FROM \"WH\" WHERE id_facility = '%d'", idFacility);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("selected WH removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }


    public void updateWH(int idWH, Time openingHours, Time closingHours) throws SQLException {


        String querySQL = String.format("UPDATE \"WH\" SET opening = '%tT', closing = '%tT'  WHERE id = '%d'", openingHours, closingHours,idWH);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Facility working hours updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public WorkingHours getWH(int idWH) throws SQLException {
        //default id (id not found)
        WorkingHours WH = null;

        String querySQL = String.format("SELECT * FROM \"WH\" WHERE id = '%d'", idWH);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();


            int id = resultSet.getInt("id");
            Day dayOfWeek = Day.valueOf(resultSet.getString("day_of_week"));
            Time openingHours = resultSet.getTime("opening");
            Time closingHours = resultSet.getTime("closing");
            int idFacility = resultSet.getInt("id_facility"); //FIXME is it useful?

            WH = new WorkingHours(id,dayOfWeek,openingHours,closingHours);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return WH;
    }

    public ArrayList<WorkingHours> getWHsByFacility(int idFacility) throws SQLException {
        //default id (id not found)
        ArrayList<WorkingHours> WHs = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"WH\" WHERE id_facility = '%d'", idFacility);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WHs.add(this.getWH(resultSet.getInt("id")));
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return WHs;
    }

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, Day dayOfWeek) throws SQLException {
        //default id (id not found)
        ArrayList<WorkingHours> WHs = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"WH\" WHERE id_facility = '%d' AND day_of_week = '%s'", idFacility, dayOfWeek);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WHs.add(this.getWH(resultSet.getInt("id")));
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return WHs;
    }



}
