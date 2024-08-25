package main.java.ORM;

import main.java.DomainModel.Group;
import main.java.DomainModel.Invite;
import main.java.DomainModel.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class IsPartDao {

    private Connection connection;

    //methods
    public void addMembership(int groupId, int userId, int guestUsers) throws SQLException {

        String querySQL = String.format("INSERT INTO \"IsPart\" (id_group, id_user,guest_users)) " +
                "VALUES ('%d', '%d', '%d')", groupId, userId, guestUsers);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Membership added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void removeMembership(int groupId, int userId) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("DELETE FROM \"Invite\" WHERE id_group = '%d' AND id_user = '%d'", groupId, userId);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Membership removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public ArrayList<User> getGroupMembers(int id) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        String querySQL = String.format("SELECT id_user FROM \"IsPart\" WHERE id_group = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(userDAO.getUser(resultSet.getInt("id_user")));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return users;
    }

    public ArrayList<Group> getAllGroupsByUser(int id) throws SQLException {
        ArrayList<Group> groups = new ArrayList<>();
        GroupDao groupDAO = new GroupDao();

        String querySQL = String.format("SELECT id_group FROM \"IsPart\" WHERE id_user = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                groups.add(groupDAO.getGroup(resultSet.getInt("id_group")));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return groups;
    }

    public int countGroupGuests(int id) throws SQLException {

        int count = 0;
        String querySQL = String.format("SELECT SUM(guest_users) AS Guests FROM \"IsPart\" WHERE id_group = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            count = resultSet.getInt("Guests");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return count;
    }

    public int countGroupMembers(int id) throws SQLException {

        int count = 0;
        String querySQL = String.format("SELECT COUNT(id_user) AS Members FROM \"IsPart\" WHERE id_group = '%d'", id);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            count = resultSet.getInt("Members");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return count;
    }

    //TODO cascata per remove groupmembership

    public void updateGuestsUsers(int id, int guestUsers) throws SQLException {

        String querySQL = String.format("UPDATE \"IsPart\" SET guest_users = '%d' WHERE id = '%d'", guestUsers, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Guest users updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
}
