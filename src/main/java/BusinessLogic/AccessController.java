package main.java.BusinessLogic;
//fixme mi torna poco l'utilizzo di un'intera classe solo per questo
public class AccessController {
    // attributes
    private AccessStrategy accessStrategy;

    //constructor

    public AccessController(AccessStrategy accessStrategy) {
        this.accessStrategy = accessStrategy;
    }

    // methods

    public AccessStrategy getAccessStrategy() {
        return accessStrategy;
    }

    public void setAccessStrategy(AccessStrategy accessStrategy) {
        this.accessStrategy = accessStrategy;
    }

    public void login(String username, String password) {
        accessStrategy.login(username, password);
    }

    public void register() {
        accessStrategy.register();
    }
}
