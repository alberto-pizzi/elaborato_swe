package main.java.ORM;

import main.java.DomainModel.Invite;
import main.java.DomainModel.Sport;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SportDao {

    private Connection connection;

    //methods
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

    public void deleteSport(int id) throws SQLException {

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


    public Sport getSport(int idSport) throws SQLException, ClassNotFoundException {

        Sport sport = null;

        String querySQL = String.format("SELECT * FROM \"Sport\" WHERE id = '%d'", idSport);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int playersRequired = resultSet.getInt("players_required");

            sport = new Sport(id, name, playersRequired);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return sport;
    }

    public ArrayList<Sport> getAllSport() throws SQLException {

        ArrayList<Sport> sports = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Sport\"");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int playersRequired = resultSet.getInt("players_required");

                sports.add(new Sport(id, name, playersRequired));

            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return sports;
    }

    public int getSportPlayers(int id) throws SQLException {

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
