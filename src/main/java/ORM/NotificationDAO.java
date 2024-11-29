package main.java.ORM;

import main.java.DomainModel.Invite;
import main.java.DomainModel.Notification;
import main.java.DomainModel.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//TODO to finish to implement
public class NotificationDAO {

    private Connection connection;

    private String notificationTableName(Person person){
        return (person.getTarget() == "Owner") ? "NotifyOwner" : "NotifyUser";
    }

    private String notificationIdName(Person person){
        return (person.getTarget() == "Owner") ? "id_owner" : "id_user";
    }

    public ArrayList<Notification> getNotifications(Person person) throws SQLException {
        ArrayList<Notification> notifications = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \""+ notificationTableName(person)+ "\" WHERE "+ notificationIdName(person) +" = '%d'", person.getId());

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idPerson = resultSet.getInt(notificationIdName(person));
                int idReservation = resultSet.getInt("id_reservation");

                ReservationDao reservationDao = new ReservationDao();
                reservationDao.getReservation(idReservation);

                //TODO implement
                //notifications.add(new Notification());


            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return notifications;

    }

    public Notification getNotification(){

        //TODO implement
        return null;
    }

}
