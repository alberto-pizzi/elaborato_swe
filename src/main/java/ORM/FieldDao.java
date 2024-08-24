package main.java.ORM;

import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FieldDao {

    private Connection connection;

    //methods
    public void addField(Field field) throws SQLException {

        String querySQL = String.format("INSERT INTO \"Field\" (name, id_sport, description, price, image, id_facility)) " +
                "VALUES ('%s', '%d', '%s', '%d', '%s', '%d')", field.getName(), field.getSport().getId(), field.getDescription(),
                field.getPrice(), field.getImage(), field.getIdFacility());

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Field added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    //TODO aggiunto metodo
    public Field getField(int fieldId) throws SQLException, ClassNotFoundException {

        Field field = null;
        Sport sport = null;
        SportDao sportDao = new SportDao();

        String querySQL = String.format("SELECT * FROM \"Field\" WHERE id = '%d'", fieldId);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            sport = sportDao.getSport(id);
            String description = resultSet.getString("description");
            int price = resultSet.getInt("price");
            String image = resultSet.getString("image");
            int idFacility = resultSet.getInt("id_facility");

            field = new Field(id, name, sport, description, price, image, idFacility);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return field;
    }

    public void deleteField(int id) throws SQLException, ClassNotFoundException {

        String querySQL = String.format("DELETE FROM \"Field\" WHERE id = '%d'", id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Field removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }

    }

    public void updateName(int id, String newName) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET name = '%s' WHERE id = '%d'", newName, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Name updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public void updateDescription(int id, String newDescription) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET description = '%s' WHERE id = '%d'", newDescription, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Description updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public void updatePrice(int id, int newPrice) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET price = '%d' WHERE id = '%d'", newPrice, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Price updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public void updateImage(int id, String newImage) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET image = '%s' WHERE id = '%d'", newImage, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Image updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public void updateSport(int id, int sportId) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET id_sport = '%d' WHERE id = '%d'", sportId, id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.executeUpdate();
            System.out.println("Sport updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

}
