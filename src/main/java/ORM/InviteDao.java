package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Invite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class InviteDao {

    private Connection connection;

    //methods
    public void addInvite(Invite invite,int idUser) throws SQLException {

        String querySQL = String.format("INSERT INTO \"Invite\" (id_group, id_user)) " +
                "VALUES ('%d', '%d')", invite.getIdGroup(), idUser);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Invite added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

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

    public ArrayList<Invite>  getInvitesByUser(int idUser) throws SQLException {
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

                invites.add(new Invite(inviteId, groupId));

            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return invites;
    }
}
