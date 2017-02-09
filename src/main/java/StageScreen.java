import com.anmi.volumiofx.scene.ShareMenuSceneFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class StageScreen extends Application {

    private TextArea message = new TextArea();
    private ShareMenuSceneFactory shareMenuScene;

    private Parent createSocketContent() throws URISyntaxException, IOException, DeploymentException {
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

    private Scene createShareMenuContent() throws MalformedURLException, SmbException {
        SmbFile smbFile = new SmbFile("smb://192.168.1.1");
        shareMenuScene = new ShareMenuSceneFactory(smbFile);

        return new Scene(shareMenuScene.getHbox(),300,120);
    }


    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        primaryStage.setScene(createShareMenuContent());
       // primaryStage.setScene(new Scene(createSocketContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
