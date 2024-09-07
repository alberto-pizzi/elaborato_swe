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

    public GroupDao() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

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

    public void deleteGroup(int idGroup) throws SQLException {

        String querySQL = String.format("DELETE FROM \"Group\" WHERE id = '%d'", idGroup);

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

    public void updateGroupHead(int idGroup, int newGroupHead) throws SQLException {

        String querySQL = String.format("UPDATE \"Group\" SET group_head = '%d' WHERE id = '%d'", newGroupHead, idGroup);

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

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int requiredParticipants = resultSet.getInt("participants_required");
                int groupHead = resultSet.getInt("group_head");
                int idReservation = resultSet.getInt("id_reservation");

                Reservation reservation = reservationDao.getReservation(idReservation);


                UserDAO userDAO = new UserDAO(); //TODO check correctness

                group = new Group(id, userDAO.getUserByID(groupHead), reservation, requiredParticipants);

                group.setParticipants(reservationDao.getCountAllParticipants(idReservation)); //TODO check correctness
            }
            else{
                System.err.println("No group found with id: " + idGroup);
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return group;
    }

}
