import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

public class StatusScreen extends Application {

    private TextArea message = new TextArea();


    private Parent createContent() throws URISyntaxException, IOException, DeploymentException {
        ClientSocket client = new ClientSocket(data ->
                Platform.runLater(() -> message.appendText(data)));
        client.socketIoConnect();

        message.setPrefHeight(500.);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String inputText = input.getText();
            try {
                client.sendMessage(inputText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox(20, message, input);
        root.setPrefSize(800, 600);
        return root;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
       // primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
