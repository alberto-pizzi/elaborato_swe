package main.java.ORM;

import main.java.DomainModel.*;

import java.sql.*;
import java.util.ArrayList;

public class NotificationDAO extends ConnectionHolder{

    //methods

    private String notificationTableName(Person person){
        return (person.getTarget() == "Owner") ? "NotificationOwner" : "NotificationUser";
    }

    private String notificationIdName(Person person){
        return (person.getTarget() == "Owner") ? "id_owner" : "id_user";
    }


    public Notification getNotification(Person person, int idNotification) throws SQLException {

        Notification notification = null;

        String querySQL = String.format("SELECT * FROM \""+ notificationTableName(person)+ "\" P1 LEFT JOIN \"Message\" M1 ON P1.id_message=M1.id WHERE P1.id = '%d'", idNotification);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idPerson = resultSet.getInt(notificationIdName(person));
                int idReservation = resultSet.getInt("id_reservation");
                int idMessage = resultSet.getInt("id_message");
                String notificationType = resultSet.getString("notification_type");

                Reservation reservation = null;
                ReservationDao reservationDao = new ReservationDao();

                //I do two distinct search
                if (idReservation != 0) {
                    reservation = reservationDao.getReservation(idReservation, true);
                    if (reservation == null) {
                        reservation = reservationDao.getReservation(idReservation, false);

                    }
                }


                if (idMessage != 0) {
                    String title = resultSet.getString("title");
                    String message = resultSet.getString("message");
                    notification = new Notification(id,person, reservation, NotificationType.fromStringToNotificationType(notificationType),title,message);
                }
                else
                    notification = new Notification(id,person,reservation,NotificationType.fromStringToNotificationType(notificationType));

            }
            else{
                System.err.println("No notification found with id: " + idNotification);
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


    public ArrayList<Notification> getNotifications(Person person) throws SQLException {
        ArrayList<Notification> notifications = new ArrayList<>();

        String querySQL = String.format("SELECT * FROM \""+ notificationTableName(person)+ "\" WHERE "+ notificationIdName(person) +" = '%d'", person.getId());

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                notifications.add(this.getNotification(person, resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return notifications;

    }

    public void deleteNotification(Person person, int idNotification) throws SQLException {

        String querySQL = String.format("DELETE FROM \""+ notificationTableName(person)+ "\" WHERE id = '%d'", idNotification);

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


    public int addNotification(Notification notification) throws SQLException {
        int idAdded = 0;

        boolean oldConnectionAutoCommit = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            int messageId = -1;  // -1 for message typing error

            if (notification.getNotificationType() == NotificationType.ANNOUNCEMENT) {
                messageId = createMessage(notification);
                if (messageId == -1) {
                    throw new SQLException("Failed to create message.");
                }
            }

            // 0 if not announcement
            idAdded = createNotification(notification, messageId);

            //commit transaction
            connection.commit();
            System.out.println("Notification and message added successfully.");
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Error while adding notification: " + e.getMessage());
        } finally {
            // restore auto commit
            connection.setAutoCommit(oldConnectionAutoCommit);
        }

        return idAdded;
    }

    private int createMessage(Notification notification) throws SQLException {
        String messageQuery = String.format("INSERT INTO \"Message\" (title, message) VALUES ('%s', '%s')",notification.getTitle(),notification.getMessage());

        try (PreparedStatement pstmtMessage = connection.prepareStatement(messageQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmtMessage.executeUpdate();

            try (ResultSet generatedKeys = pstmtMessage.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while adding message: " + e.getMessage());
            throw e;
        }
    }

    private int createNotification(Notification notification, int messageId) throws SQLException {
        String notificationQuery = "INSERT INTO \"" + notificationTableName(notification.getRecipient()) +
                "\" (" + notificationIdName(notification.getRecipient()) + ", notification_type, id_message, id_reservation) " +
                "VALUES (?, ?, ?, ?)";

        int idAdded = 0;

        try (PreparedStatement pstmtNotification = connection.prepareStatement(notificationQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmtNotification.setInt(1, notification.getRecipient().getId());
            pstmtNotification.setString(2, notification.getNotificationType().getStringValue());

            if (messageId != -1) {
                pstmtNotification.setInt(3, messageId);
            } else {
                pstmtNotification.setNull(3, java.sql.Types.INTEGER);
            }

            pstmtNotification.setInt(4, notification.getReservation().getId());
            pstmtNotification.executeUpdate();

            ResultSet resultSet = pstmtNotification.getGeneratedKeys();
            if (resultSet.next()) {
                idAdded = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error while adding notification: " + e.getMessage());
            throw e;
        }

        return idAdded;
    }





}
