package tests.DomainModelTest;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Group;
import main.java.DomainModel.Reservation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class FacilityTest extends GeneralTest {

    @Test
    public void initFacilityTest(){
        Facility facility = createFacility();

        assertNotNull(facility);
        assertEquals(0,facility.getFields().size());
        assertEquals(0,facility.getWorkingHours().size());
        assertNotNull(facility.getOwner());
        assertFalse(facility.getName().isEmpty());
        assertFalse(facility.getAddress().isEmpty());
        assertFalse(facility.getCity().isEmpty());
        assertFalse(facility.getProvince().isEmpty());
        assertFalse(facility.getCountry().isEmpty());

    }
}
