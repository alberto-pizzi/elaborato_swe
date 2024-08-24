package main.java.ORM;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SportDao {

    private Connection connection;

    //methods
    //Todo cambiato nome
    public void addSport(String name, int playersRequired) throws SQLException {

        String querySQL = String.format("INSERT INTO \"Sport\" (name, players_required)) " +
                "VALUES ('%s', '%d')", name, playersRequired);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Sport added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
    }

    public void deleteSport(int id) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("DELETE FROM \"Sport\" WHERE id = '%d'", id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Sport removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO controllare cosa di sport
    /*
    public void getSport(int idGroup) throws SQLException, ClassNotFoundException {

        Group group = null;
        ReservationDao reservationDao = new ReservationDao();

        String querySQL = String.format("SELECT * FROM \"Group\" WHERE id = '%d'", idGroup);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            int groupHead = resultSet.getInt("id");

            Reservation reservation = reservationDao.getReservation(resultSet.getInt("id"));

            group = new Group(id, groupHead, reservation);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return group;
    }

    public void getAllSport(int idGroup) throws SQLException, ClassNotFoundException {

        Group group = null;
        ReservationDao reservationDao = new ReservationDao();

        String querySQL = String.format("SELECT * FROM \"Group\" WHERE id = '%d'", idGroup);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            int groupHead = resultSet.getInt("id");

            Reservation reservation = reservationDao.getReservation(resultSet.getInt("id"));

            group = new Group(id, groupHead, reservation);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return group;
    }*/

    public int getSportPlayers(int id) throws SQLException, ClassNotFoundException {

        int count = 0;
        String querySQL = String.format("SELECT players_required FROM \"Sport\" WHERE id_group = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            count = resultSet.getInt("players_required");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return count;
    }

    public void updateSportPlayers(int id, int players) throws SQLException {

        String querySQL = String.format("UPDATE \"Sport\" SET players_required = '%d' WHERE id = '%d'", players, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Players required updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public void updateSportName(int id, String name) throws SQLException {

        String querySQL = String.format("UPDATE \"Sport\" SET name = '%s' WHERE id = '%d'", name, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Name updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

}
