package main.java.BusinessLogic;

import main.java.DomainModel.Person;

public class SessionController {

    private Person person = null;

    private static final SessionController instance = new SessionController();

    private SessionController() {}

    public static SessionController getInstance() {
        return instance;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
