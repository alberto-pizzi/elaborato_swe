package tests.ORMTest;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;
import main.java.DomainModel.User;
import main.java.ORM.FacilityDAO;
import main.java.ORM.FieldDao;
import main.java.ORM.OwnerDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class FacilityDAOTest extends GeneralDAOTest {

    private static boolean shouldSkip = false;
    private FacilityDAO facilityDAO;
    private Facility facility;
    private OwnerDAO ownerDAO;


    @Override
    @BeforeEach
    public void setup() throws SQLException, Exception {

        facilityDAO = new FacilityDAO();
        ownerDAO = new OwnerDAO();
        facility = createFacility(createOwner());

        if (facilityDAO.getFacility(facility.getId(),false) == null)
            shouldSkip = true;

        Assumptions.assumeFalse(shouldSkip);

    }

    @Override
    @AfterEach
    public void teardown() throws SQLException, Exception {


        facilityDAO.deleteFacility(facility.getId());
        ownerDAO.deletePerson(facility.getOwner().getUsername());

        if (facilityDAO.getFacility(facility.getId(),false) != null)
            shouldSkip = true;

        facility = null;
        facilityDAO = null;
        ownerDAO = null;
    }


    @Test
    public void updateNameTest() throws SQLException{
        String newName = "newNameFacility";

        facilityDAO.updateName(facility.getId(), newName);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getName(), newName);
    }

    @Test
    public void updateAddressTest() throws SQLException{
        String newAddress = "newFacilityAddress";

        facilityDAO.updateAddress(facility.getId(), newAddress);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getAddress(), newAddress);
    }

    @Test
    public void updateCityTest() throws SQLException{
        String newCity = "newFacilityCity";

        facilityDAO.updateCity(facility.getId(), newCity);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getCity(), newCity);
    }

    @Test
    public void updateCountryTest() throws SQLException{
        String newCountry = "newFacilityCountry";

        facilityDAO.updateCountry(facility.getId(), newCountry);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getCountry(), newCountry);
    }

    @Test
    public void updateImageTest() throws SQLException{
        String newImageName = "newFacilityImage.png";

        facilityDAO.updateImage(facility.getId(), newImageName);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getImage(), newImageName);
    }

    @Test
    public void updateNFieldsTest() throws SQLException{

        //FIXME fix field dependencies
        /*
        Sport sport = createSport();
        facility.getFields().add(createField(facility,sport));

        int newNFields = facility.getNFields() + 10;

        facilityDAO.updateNFields(facility.getId(), newNFields);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);
        facilityUpdated.getFields().add(createField(facilityUpdated,sport));


        assertEquals(facilityUpdated.getNFields(), newNFields);

         */
    }

    @Test
    public void updateNManagersTest() throws SQLException{
        int newName = facility.getNManager() + 10;

        facilityDAO.updateNManagers(facility.getId(), newName);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getNManager(), newName);
    }

    @Test
    public void updateProvinceTest() throws SQLException{
        String newProvince = "newFacilityProvince";

        facilityDAO.updateProvince(facility.getId(), newProvince);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getProvince(), newProvince);
    }

    @Test
    public void updateTelephoneTest() throws SQLException{
        String newNumber = "12345678";

        facilityDAO.updateTelephone(facility.getId(), newNumber);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getTelephone(), newNumber);
    }

    @Test
    public void updateZipTest() throws SQLException{
        String newZip = "12345";

        facilityDAO.updateZip(facility.getId(), newZip);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getZip(), newZip);
    }

    @Test
    public void getFacilitiesByProvinceTest() throws SQLException{
        assertFalse(facilityDAO.getFacilitiesByProvince(facility.getProvince()).isEmpty());
    }

    @Test
    public void getFacilitiesByOwnerTest() throws SQLException{
        assertFalse(facilityDAO.getFacilitiesByOwner(facility.getOwner().getId()).isEmpty());

    }




}
