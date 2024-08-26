package main.java.ORM;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Time;

public class WorkingHoursDAO {

    private Connection connection;

    //methods

    public void addWHToFacility(int idFacility, int dayOfWeek, Time openingHours, Time closingHours) throws SQLException {

        String querySQL = String.format("INSERT INTO \"WH\" (day_of_week, opening, closing, id_facility)) " +
                "VALUES ('%d', '%tT', '%tT', '%d')", dayOfWeek, openingHours, closingHours, idFacility);

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

    //TODO add removeByDay and removeAll(ByFacility)


}
