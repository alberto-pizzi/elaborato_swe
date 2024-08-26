package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Group;
import main.java.DomainModel.Reservation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GroupDao {

    private Connection connection;

    //methods
    public void addGroup(Group group) throws SQLException {


        String querySQL = String.format("INSERT INTO \"Group\" (group_head, participants_required, id_reservation)) " +
                "VALUES ('%d', '%d', '%d')", group.getGroupHead(), group.getRequiredParticipants(), group.getReservation().getId());

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Group added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

    }

    public void deleteGroup(int id) throws SQLException {

        String querySQL = String.format("DELETE FROM \"Group\" WHERE id = '%d'", id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Group removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

    }

    public void updateGroupHead(int groupId, int newId) throws SQLException {

        String querySQL = String.format("UPDATE \"Group\" SET group_head = '%d' WHERE id = '%d'", newId, groupId);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Group head updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public Group getGroup(int idGroup) throws SQLException, ClassNotFoundException {

        Group group = null;
        ReservationDao reservationDao = new ReservationDao();

        String querySQL = String.format("SELECT * FROM \"Group\" WHERE id = '%d'", idGroup);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            int requiredParticipants = resultSet.getInt("participants_required");
            int groupHead = resultSet.getInt("group_head");

            Reservation reservation = reservationDao.getReservation(resultSet.getInt("id_reservation"));

            group = new Group(id, groupHead, reservation);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return group;
    }

}
