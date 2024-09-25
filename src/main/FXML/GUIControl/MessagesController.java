package main.FXML.GUIControl;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MessagesController {
    private Label messageLabel;
    private PauseTransition hideMessageTransition;

    private static final String ERROR_STYLE = "-fx-text-fill: red;";
    private static final String WARNING_STYLE = "-fx-text-fill: orange;";
    private static final String SUCCESS_STYLE = "-fx-text-fill: green;";

    public enum MessageType {
        ERROR, WARNING, SUCCESS
    }

    public MessagesController(Label messageLabel) {
        this.messageLabel = messageLabel;
        this.hideMessageTransition = new PauseTransition();
        this.messageLabel.setVisible(false);
    }

    public void showMessage(String message, MessageType type, int durationInSeconds) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);

        switch (type) {
            case ERROR:
                messageLabel.setStyle(ERROR_STYLE);
                break;
            case WARNING:
                messageLabel.setStyle(WARNING_STYLE);
                break;
            case SUCCESS:
                messageLabel.setStyle(SUCCESS_STYLE);
                break;
        }

        hideMessageTransition.setDuration(Duration.seconds(durationInSeconds));
        hideMessageTransition.setOnFinished(event -> messageLabel.setVisible(false));

        hideMessageTransition.playFromStart();
    }


    public void hideMessage() {
        hideMessageTransition.stop();
        messageLabel.setVisible(false);
    }
}
