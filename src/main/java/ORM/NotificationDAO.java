package main.java.ORM;

import main.java.DomainModel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//TODO to finish to implement
public class NotificationDAO {

    private Connection connection;


    //constructor
    public NotificationDAO(){
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //methods

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

                //TODO check correctness
                notifications.add(new Notification(person,reservationDao.getReservation(idReservation)));

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

    //TODO id or Notification as parameter? attribute name "id" is correct?
    public void deleteNotification(Person person, int idReservation) throws SQLException {

        String querySQL = String.format("DELETE FROM \""+ notificationTableName(person)+ "\" WHERE "+ notificationIdName(person) +" = '%d' AND id_reservation = '%d'", person.getId(),idReservation);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Notification removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO attribute name is correct? (maybe yes)
    public Notification getNotification(Person person, int idReservation) throws SQLException {

        Notification notification = null;
        ReservationDao reservationDao = new ReservationDao();

        //TODO is better like this or with "exists" query?
        String querySQL = String.format("SELECT * FROM \""+ notificationTableName(person)+ "\" WHERE "+ notificationIdName(person) +" = '%d' AND id_reservation = '%d'", person.getId(),idReservation);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Reservation reservation = reservationDao.getReservation(idReservation);

                notification = new Notification(person,reservation);

            }
            else{
                System.err.println("No group found with id: " + idReservation);
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return notification;
    }

    //TODO Notification as parameter? (Maybe yes, like GroupDAO). Notification ID exists when I create a notification by DomainModel?
    public void addNotification(Notification notification) throws SQLException {


        String querySQL = String.format("INSERT INTO "+ notificationTableName(notification.getPerson())+ " ("+ notificationIdName(notification.getPerson()) +", id_reservation)) " +
                "VALUES ('%d', '%d')", notification.getPerson().getId(),notification.getReservation().getId());

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Notification added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

    }


}
