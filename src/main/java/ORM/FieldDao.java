package main.java.ORM;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Owner;
import main.java.DomainModel.Sport;

import java.sql.*;
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
                field.getPrice(), field.getImage(), field.getFacility().getId());

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

            if (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                sport = sportDao.getSport(resultSet.getInt("id_sport"));
                String description = resultSet.getString("description");
                float price = resultSet.getInt("price");
                String image = resultSet.getString("image");
                int idFacility = resultSet.getInt("id_facility");

                //TODO check correctness
                FacilityDAO facilityDao = new FacilityDAO();
                Facility facility = facilityDao.getFacility(idFacility, false);


                field = new Field(id, name, sport, description, price, image, facility);
            }
            else{
                System.err.println("No field found with id: " + idField);
            }

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

    public ArrayList<Field> getFieldsByFacility(int idFacility, boolean loadFacility) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();

        String querySQL = String.format("SELECT id FROM \"Field\" WHERE id_facility = '%d'", idFacility);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //FIXME possible infinite loop
                Field field = this.getField(resultSet.getInt("id"));
                if (loadFacility) {
                    Facility facility = new FacilityDAO().getFacility(idFacility, false);
                    field.setFacility(facility);
                }
                fields.add(field);
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
    //todo aggiungere a uml
    public ArrayList<Field> getAllFields(boolean loadFacility) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();

        String querySQL = String.format("SELECT id FROM \"Field\" ");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //FIXME possible infinite loop
                Field field = this.getField(resultSet.getInt("id"));
                if (loadFacility) {
                    Facility facility = new FacilityDAO().getFacility(field.getFacility().getId(), false);
                    field.setFacility(facility);
                }
                fields.add(field);
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

        String querySQL = String.format("SELECT * FROM \"Field\" INNER JOIN \"Facility\" ON \"Field\".id_facility = \"Facility\".id WHERE UPPER(province) LIKE UPPER('%s')", "%" + province + "%");

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

                FacilityDAO facilityDAO = new FacilityDAO(); //TODO check correctness

                fields.add(new Field(id, name, sport, description, price, image, facilityDAO.getFacility(idFacility, false)));
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
    //todo aggiungere uml
    public ArrayList<Field> getFieldsByName(String searchName) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();
        Sport sport = null;
        SportDao sportDao = new SportDao();

        String querySQL = String.format("SELECT * FROM \"Field\" WHERE name LIKE '%s'", "%" + searchName + "%");

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

                FacilityDAO facilityDAO = new FacilityDAO(); //TODO check correctness

                fields.add(new Field(id, name, sport, description, price, image, facilityDAO.getFacility(idFacility, false)));
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

    public ArrayList<Field> getFieldsBySport(String sportName) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();
        Sport sport = null;
        SportDao sportDao = new SportDao();

        String querySQL =  String.format("SELECT * FROM \"Field\" INNER JOIN \"Sport\" ON \"Field\".id_sport = \"Sport\".id WHERE UPPER(\"Sport\".name) LIKE UPPER('%s')", "%" + sportName + "%");

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

                FacilityDAO facilityDAO = new FacilityDAO(); //TODO check correctness

                fields.add(new Field(id, name, sport, description, price, image, facilityDAO.getFacility(idFacility, false)));
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

    public void fieldComparator (ArrayList<Field> fields, ArrayList<Field> newFields) throws SQLException {
        ArrayList<Field> tempFields = new ArrayList<>();
        boolean found = false;
        if(fields.isEmpty()) {
            fields.addAll(newFields);
        }else if(!newFields.isEmpty()){
            for (Field field : newFields) {
                for (Field oldField : fields) {
                    if (oldField.getId() == field.getId()) {
                        found = true;
                        break;
                    }
                }
                if(!found){
                    tempFields.add(field);
                }
                found = false;
            }
            fields.addAll(tempFields);
        }
    }
    public ArrayList<Field> search(String searchText) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<Field> tempFields;
        String[] words = searchText.split("\\s+");
        for (String word : words) {

            tempFields = this.getFieldsByProvince(word);
            fieldComparator(fields, tempFields);

            tempFields = this.getFieldsBySport(word);
            fieldComparator(fields, tempFields);

            tempFields = this.getFieldsByName(word);
            fieldComparator(fields, tempFields);

        }


        return fields;
    }

    //todo aggiungere a uml
    public ArrayList<Field> getFieldsByOwner(Owner owner) throws SQLException {
        ArrayList<Field> fields = new ArrayList<>();
        Sport sport = null;
        SportDao sportDao = new SportDao();

        String querySQL =  String.format("SELECT * FROM \"Field\" INNER JOIN \"Facility\" ON \"Field\".id_facility = \"Facility\".id WHERE \"Facility\".id_owner = '%d'", owner.getId());

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

                FacilityDAO facilityDAO = new FacilityDAO(); //TODO check correctness

                fields.add(new Field(id, name, sport, description, price, image, facilityDAO.getFacility(idFacility, false)));
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

    //todo aggiungere a uml
    public int reservedFields(Date date, Owner owner) throws SQLException {

        int number = 0;
        String querySQL =  String.format("SELECT count(Distinct id_field) AS number FROM \"Reservation\" INNER JOIN \"Field\" ON \"Reservation\".id_field = \"Field\".id INNER JOIN \"Facility\" ON \"Field\".id_facility = \"Facility\".id WHERE \"Reservation\".event_date = '%tF' AND \"Facility\".id_owner = '%d'", date, owner.getId());

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                number = resultSet.getInt("number");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return number;
    }

    //todo aggiungere uml
    public String getFieldAddress(int fieldId) throws SQLException {

        String address = null;
        String querySQL =  String.format("SELECT address FROM \"Field\"  INNER JOIN \"Facility\" ON \"Field\".id_facility = \"Facility\".id WHERE \"Field\".id = '%d'", fieldId);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(querySQL);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                address = resultSet.getString("address");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return address;
    }
}
