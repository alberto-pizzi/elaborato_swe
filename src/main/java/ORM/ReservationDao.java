package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

import java.sql.*;
import java.util.ArrayList;

public class ReservationDao {

    private Connection connection;

    //constructor
    public ReservationDao() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //methods
    public void addReservation(Reservation reservation) throws SQLException {
        String querySQL = String.format("INSERT INTO \"Reservation\" (res_date, event_date,res_time, event_time_start, " +
                "event_time_end, id_field, n_participants, is_confirmed, is_matched, id_user)) " +
                "VALUES ('%tF', '%tF', '%tT', '%tT', '%tT', '%d', '%d', '%b', '%b', '%d')", reservation.getReservationDate(), reservation.getEventDate(),
                reservation.getReservationTime(), reservation.getEventTimeStart(),reservation.getEventTimeEnd(), reservation.getIdField(),
                reservation.getNParticipants(), reservation.isConfirmed(), reservation.isMatched(), reservation.getIdUser());

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Reservation added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
    }

    public int getCountAllParticipants(int idReservation) throws SQLException {
        int count = 0;
        int group = 0;
        IsPartDao isPartDao = new IsPartDao();
        String querySQL = String.format("SELECT * FROM \"Group\" WHERE id_reservation = '%d'", idReservation);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            group = resultSet.getInt("id");
            count = isPartDao.countGroupGuests(group) + isPartDao.countGroupMembers(group);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return count;
    }
    public Reservation getReservation(int idReservation) throws SQLException {

        Reservation reservation = null;

        String querySQL = String.format("SELECT * FROM \"Reservation\" WHERE id = '%d'", idReservation);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            Date reservationDate = resultSet.getDate("res_date");
            Time reservationTime = resultSet.getTime("res_time");
            Date eventDate = resultSet.getDate("event_date");
            Time eventTimeStart = resultSet.getTime("event_time_start");
            Time eventTimeEnd = resultSet.getTime("event_time_end");
            int idField = resultSet.getInt("id_field");
            int nParticipants = resultSet.getInt("n_participants");
            boolean isConfirmed = resultSet.getBoolean("is_confirmed");
            int idUser = resultSet.getInt("id_user");
            boolean isMatched = resultSet.getBoolean("is_matched");

            reservation = new Reservation(id, reservationDate, reservationTime, eventDate, eventTimeStart, eventTimeEnd, idField, nParticipants, isConfirmed, idUser,isMatched);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return reservation;

    }

    public void deleteReservation(int idReservation) throws SQLException {

        String querySQL = String.format("DELETE FROM \"Reservation\" WHERE id = '%d'", idReservation);

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

    public ArrayList<Reservation> getReservationsByField(int idField) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String querySQL = String.format("SELECT id FROM \"Reservation\" WHERE id_field = '%d'", idField);

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

    public ArrayList<Reservation> getReservationsByUser(int idUser) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String querySQL = String.format("SELECT id FROM \"Reservation\" WHERE id_user = '%d'", idUser);

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

    public void updateIdUser(int idReservation, int newUser) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET id_user = '%d' WHERE id = '%d'", newUser, idReservation);

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

    public void updateNParticipants(int idReservation, int newNumber) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET n_participants = '%d' WHERE id = '%d'", newNumber, idReservation);

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

    public void updateIsConfirmed(int idReservation, boolean isConfirmed) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET is_confirmed = '%b' WHERE id = '%d'", isConfirmed, idReservation);

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

    public void updateEventDate(int idReservation, Date date) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET event_date = '%tF' WHERE id = '%d'", date, idReservation);

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

    public void updateEventTimeStart(int idReservation, Time time) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET event_time_start = '%tT' WHERE id = '%d'", time, idReservation);

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

    public void updateEventTimeEnd(int idReservation, Time time) throws SQLException {

        String querySQL = String.format("UPDATE \"Reservation\" SET event_time_end = '%tT' WHERE id = '%d'", time, idReservation);

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

}
