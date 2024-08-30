package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.ORM.OwnerDAO;

import java.sql.SQLException;

public class OwnerAccess implements AccessStrategy{
    @Override
    public Owner login(String username, String password) throws SQLException {

        OwnerDAO dao = new OwnerDAO();
        Owner owner = null;
        try {
            boolean verified = dao.checkPassword(username,password);

            if (verified) {
                owner = dao.getOwner(username);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return owner;
    }

    @Override
    public void register(String username, String email, String password, String city, String province, String zip, String country) throws SQLException{

        OwnerDAO dao = new OwnerDAO();

        try {
            dao.addOwner(username,email,password,city,province,zip,country);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}