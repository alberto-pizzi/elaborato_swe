package main.java.ORM;

import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReservationDao {

    private Connection connection;

    //methods
    public Reservation getReservation(int id) {
        return null;
    }

    public void deleteReservation(int id) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("DELETE FROM \"Reservation\" WHERE id = '%d'", id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Reservation removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }
    //TODO da finire
    public ArrayList<Reservation> getReservationsByField(int id) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Reservation\" WHERE id_field = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservations.add(new Reservation());
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return reservations;
    }

    public void updateIdUser(int id, int newId) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET id_user = '%d' WHERE id = '%d'", newId, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("User updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    //Todo controllare ortografia
    public void updateNParticipants(int id, int newNumber) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET n_participants = '%d' WHERE id = '%d'", newNumber, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Participants updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    //Todo controllare e sistemare da bool a sql
    public void updateIsConfirmed(int id, boolean isConfirmed) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET is_confirmed = '%d' WHERE id = '%d'", isConfirmed, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Confirmation updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

}
