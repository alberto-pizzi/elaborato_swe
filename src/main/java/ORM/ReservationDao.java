package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

import java.sql.*;
import java.util.ArrayList;

public class ReservationDao {

    private Connection connection;

    //methods
    public Reservation getReservation(int id) throws SQLException {

        Reservation reservation = null;

        String querySQL = String.format("SELECT * FROM \"Reservation\" WHERE id = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //FIXME type of dates
        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int reservationId = resultSet.getInt("id");
            Date reservationDate = resultSet.getDate("res_date");
            Time reservationTime = resultSet.getTime("res_time");
            Date eventDateStart = resultSet.getDate("event_date");
            Time eventTimeEnd = resultSet.getTime("event_time_end");
            int idField = resultSet.getInt("id_field");
            int nParticipants = resultSet.getInt("n_participants");
            boolean isConfirmed = resultSet.getBoolean("is_confirmed");
            int idUser = resultSet.getInt("id_user");
            int participantsRequired = resultSet.getInt("n_participants"); //TODO is doable?
            boolean isMatched = resultSet.getBoolean("is_matched");

            reservation = new Reservation(reservationId, reservationDate, reservationTime, eventDateStart, eventTimeEnd, idField, nParticipants, isConfirmed, idUser,participantsRequired,isMatched);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return reservation;

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

    public ArrayList<Reservation> getReservationsByField(int id) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Reservation\" WHERE id_field = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservations.add(this.getReservation(resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return reservations;
    }

    public ArrayList<Reservation> getReservationsByUser(int id) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Reservation\" WHERE id_user = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservations.add(this.getReservation(resultSet.getInt("id")));
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

    //FIXME riferimento
    public void updateEventDate(int id, Date date) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET event_date = '%s' WHERE id = '%d'", date, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Event date updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    //FIXME riferimento
    public void updateEventTimeStart(int id, Time time) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET event_time_start = '%s' WHERE id = '%d'", time, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Event start time updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    //FIXME riferimento
    public void updateEventTimeEnd(int id, Time time) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET event_time_end = '%s' WHERE id = '%d'", time, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Event end time updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    //fixme
}
