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

    //constructor
    public IsPartDao() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //methods
    public void addMembership(int idGroup, int idUser, int guestUsers) throws SQLException {

        String querySQL = String.format("INSERT INTO \"IsPart\" (id_group, id_user,guest_users)) " +
                "VALUES ('%d', '%d', '%d')", idGroup, idUser, guestUsers);

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

    public void removeMembership(int idGroup, int idUser) throws SQLException {

        String querySQL = String.format("DELETE FROM \"IsPart\" WHERE id_group = '%d' AND id_user = '%d'", idGroup, idUser);

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

    public ArrayList<User> getGroupMembers(int idGroup) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        String querySQL = String.format("SELECT id_user FROM \"IsPart\" WHERE id_group = '%d'", idGroup);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(userDAO.getUser(resultSet.getString("username")));
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

    public ArrayList<Group> getAllGroupsByUser(int idUser) throws SQLException {
        ArrayList<Group> groups = new ArrayList<>();
        GroupDao groupDAO = new GroupDao();

        String querySQL = String.format("SELECT id_group FROM \"IsPart\" WHERE id_user = '%d'", idUser);

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

    public int countGroupGuests(int idGroup) throws SQLException {

        int count = 0;
        String querySQL = String.format("SELECT SUM(guest_users) AS Guests FROM \"IsPart\" WHERE id_group = '%d'", idGroup);

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

    public int countGroupMembers(int idGroup) throws SQLException {

        int count = 0;
        String querySQL = String.format("SELECT COUNT(id_user) AS Members FROM \"IsPart\" WHERE id_group = '%d'", idGroup);

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

    public void updateGuestsUsers(int idGroup, int idUser, int guestUsers) throws SQLException {

        String querySQL = String.format("UPDATE \"IsPart\" SET guest_users = '%d' WHERE id_group = '%d' AND id_user = '%d'", guestUsers, idGroup, idUser);

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
