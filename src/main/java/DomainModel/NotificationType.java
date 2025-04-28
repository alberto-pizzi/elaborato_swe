package main.java.DomainModel;

//todo aggiungere uml
public enum NotificationType {
    //UNKNOWN is default as error tag
    UNKNOWN("UNKNOWN"),
    CONFIRMATION("CONFIRMATION"),
    MODIFICATION("MODIFICATION"),
    DELETION("DELETION"),
    ANNOUNCEMENT("ANNOUNCEMENT");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getStringValue() {
        return value;
    }

    public static NotificationType fromStringToNotificationType(String value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.getStringValue().equals(value)) {
                return type;
            }
        }
        return NotificationType.UNKNOWN;
    }
}

