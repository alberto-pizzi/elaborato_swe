package main.java.ORM;

import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;
import main.java.DomainModel.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FieldDao {

    private Connection connection;

    //constructor
    public FieldDao() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //methods
    public void addField(Field field) throws SQLException {

        String querySQL = String.format("INSERT INTO \"Field\" (name, id_sport, description, price, image, id_facility)) " +
                "VALUES ('%s', '%d', '%s', '%f', '%s', '%d')", field.getName(), field.getSport().getId(), field.getDescription(),
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

    public Field getField(int idField) throws SQLException, ClassNotFoundException {

        Field field = null;
        Sport sport = null;
        SportDao sportDao = new SportDao();

        String querySQL = String.format("SELECT * FROM \"Field\" WHERE id = '%d'", idField);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();

            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            sport = sportDao.getSport(resultSet.getInt("id_sport"));
            String description = resultSet.getString("description");
            float price = resultSet.getInt("price");
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

    public void deleteField(int idField) throws SQLException {

        String querySQL = String.format("DELETE FROM \"Field\" WHERE id = '%d'", idField);

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

    public void updateName(int idField, String newName) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET name = '%s' WHERE id = '%d'", newName, idField);

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

    public void updateDescription(int idField, String newDescription) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET description = '%s' WHERE id = '%d'", newDescription, idField);

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

    public void updatePrice(int idField, float newPrice) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET price = '%f' WHERE id = '%d'", newPrice, idField);

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

    public void updateImage(int idField, String newImage) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET image = '%s' WHERE id = '%d'", newImage, idField);

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

    public void updateSport(int idField, int sportId) throws SQLException {

        String querySQL = String.format("UPDATE \"Field\" SET id_sport = '%d' WHERE id = '%d'", sportId, idField);

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

    public ArrayList<Field> getFieldsByFacility(int idFacility) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();

        String querySQL = String.format("SELECT id FROM \"Field\" WHERE id_facility = '%d'", idFacility);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                fields.add(this.getField(resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return fields;
    }

    //todo check
    public ArrayList<Field> getFieldsByProvince(String province) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();
        Sport sport = null;
        SportDao sportDao = new SportDao();

        String querySQL = String.format("SELECT * FROM \"Field\" INNER JOIN \"Facility\" ON Field.id_facility = Facility.id WHERE province = '%s'", province);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                sport = sportDao.getSport(resultSet.getInt("id_sport"));
                String description = resultSet.getString("description");
                int price = resultSet.getInt("price");
                String image = resultSet.getString("image");
                int idFacility = resultSet.getInt("id_facility");

                fields.add(new Field(id, name, sport, description, price, image, idFacility));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return fields;
    }

}
