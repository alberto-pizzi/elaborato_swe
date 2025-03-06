package main.java.ORM;

import main.java.DomainModel.Group;
import main.java.DomainModel.Reservation;

import java.sql.*;

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
    public int addGroup(Group group) throws SQLException {


        String querySQL = String.format("INSERT INTO \"Group\" (group_head, participants_required, id_reservation) " +
                "VALUES ('%d', '%d', '%d')", group.getGroupHead().getId(), group.getRequiredParticipants(), group.getReservation().getId());

        int idAdded = 0;

        PreparedStatement preparedStatement = null;


        try {
            preparedStatement = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                idAdded = resultSet.getInt(1);
            }

            System.out.println("Group added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        return idAdded;
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

                Reservation reservation = reservationDao.getReservation(idReservation, false);


                UserDAO userDAO = new UserDAO();
                IsPartDao isPartDao = new IsPartDao();

                group = new Group(id, userDAO.getUserByID(groupHead), reservation, requiredParticipants);
                group.setParticipants(reservationDao.getCountAllParticipants(idReservation));
                group.setUsers(isPartDao.getGroupMembers(idGroup));
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

    public Group getGroupByReservation(int idReservation) throws SQLException, ClassNotFoundException {

        Group group = null;
        ReservationDao reservationDao = new ReservationDao();

        String querySQL = String.format("SELECT * FROM \"Group\" WHERE id_reservation = '%d'", idReservation);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int requiredParticipants = resultSet.getInt("participants_required");
                int groupHead = resultSet.getInt("group_head");

                Reservation reservation = reservationDao.getReservation(idReservation, false);


                UserDAO userDAO = new UserDAO();
                IsPartDao isPartDao = new IsPartDao();

                group = new Group(id, userDAO.getUserByID(groupHead), reservation, requiredParticipants);
                group.setParticipants(reservationDao.getCountAllParticipants(idReservation));
                group.setUsers(isPartDao.getGroupMembers(id));

            }
            else{
                System.err.println("No group found reservation id: " + idReservation);
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
