package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Invite;

import java.sql.*;
import java.util.ArrayList;

public class InviteDao {

    private Connection connection;

    public InviteDao() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //methods
    public int addInvite(Invite invite) throws SQLException {

        String querySQL = String.format("INSERT INTO \"Invite\" (id_group, id_user) " +
                "VALUES ('%d', '%d')", invite.getGroup().getId(), invite.getUser().getId());

        int idAdded = 0;

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                idAdded = resultSet.getInt(1);
            }

            System.out.println("Invite added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
        return idAdded;
    }

    public void deleteInvite(int idInvite) throws SQLException {

        String querySQL = String.format("DELETE FROM \"Invite\" WHERE id = '%d'", idInvite);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Invite removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public ArrayList<Invite>  getInvitesByUser(int idUser) throws SQLException, ClassNotFoundException {
        ArrayList<Invite> invites = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \"Invite\" WHERE id_user = '%d'", idUser);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int inviteId = resultSet.getInt("id");
                int groupId = resultSet.getInt("id_group");

                GroupDao groupDao = new GroupDao();

                invites.add(new Invite(inviteId, groupDao.getGroup(groupId)));

            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return invites;
    }

    public Boolean  checkInvite(int idUser,int idGroup) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("SELECT count(*) AS results FROM \"Invite\" WHERE id_group = '%d' AND id_user = '%d'", idGroup, idUser);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                int invites = resultSet.getInt("results");

                if (invites > 0)
                    return true;
            }else{
                System.err.println("No invite found ");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return false;
    }
}
