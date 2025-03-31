package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InviteSenderTest extends GeneralTest {

    @Test
    public void factoryMethod() {
        Group group = createGroup(createReservation(false), 1);
        InviteSender inviteSender = new InviteSender(group);
        Invite invite = inviteSender.factoryMethod();
        assertNotNull(invite);
        assertEquals(group.getId(), invite.getGroup().getId());
        assertNotEquals("random user", invite.getGroup().getGroupHead().getUsername());
    }
}