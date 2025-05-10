package tests.ORMTest;

import main.java.DomainModel.Facility;
import main.java.ORM.FacilityDAO;
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

        if (facility.getId() == 0)
            shouldSkip = true;


    }

    @Override
    @AfterEach
    public void teardown() throws SQLException, Exception {

        facilityDAO.deleteFacility(facility.getId());

        if (facility.getOwner() != null && facility.getOwner().getId() != 0)
            ownerDAO.deletePerson(facility.getOwner().getUsername());


        facility = null;
        facilityDAO = null;
        ownerDAO = null;

        //it is important to reset each test
        shouldSkip = false;
    }


    @Test
    public void updateNameTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newName = "newNameFacility";

        facilityDAO.updateName(facility.getId(), newName);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getName(), newName);
    }

    @Test
    public void updateAddressTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newAddress = "newFacilityAddress";

        facilityDAO.updateAddress(facility.getId(), newAddress);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getAddress(), newAddress);
    }

    @Test
    public void updateCityTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newCity = "newFacilityCity";

        facilityDAO.updateCity(facility.getId(), newCity);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getCity(), newCity);
    }

    @Test
    public void updateCountryTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newCountry = "newFacilityCountry";

        facilityDAO.updateCountry(facility.getId(), newCountry);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getCountry(), newCountry);
    }

    @Test
    public void updateImageTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newImageName = "newFacilityImage.png";

        facilityDAO.updateImage(facility.getId(), newImageName);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getImage(), newImageName);
    }

    @Test
    public void updateProvinceTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newProvince = "newFacilityProvince";

        facilityDAO.updateProvince(facility.getId(), newProvince);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getProvince(), newProvince);
    }

    @Test
    public void updateTelephoneTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newNumber = "12345678";

        facilityDAO.updateTelephone(facility.getId(), newNumber);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getTelephone(), newNumber);
    }

    @Test
    public void updateZipTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        String newZip = "12345";

        facilityDAO.updateZip(facility.getId(), newZip);
        Facility facilityUpdated = facilityDAO.getFacility(facility.getId(),false);

        assertEquals(facilityUpdated.getZip(), newZip);
    }

    @Test
    public void getFacilitiesByProvinceTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(facilityDAO.getFacilitiesByProvince(facility.getProvince()).isEmpty());
    }

    @Test
    public void getFacilitiesByOwnerTest() throws SQLException{
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(facilityDAO.getFacilitiesByOwner(facility.getOwner().getId()).isEmpty());

    }




}
