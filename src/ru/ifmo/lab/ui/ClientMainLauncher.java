package ru.ifmo.lab.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.lab.exceptions.InvalidValueException;
import ru.ifmo.lab.utils.AppConstant;
import ru.ifmo.lab.utils.I18N;

import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class ClientMainLauncher extends Application {

    private static InetSocketAddress address = null;

    private static Stage primaryStageObj;
    public static ResourceBundle resources = ResourceBundle.getBundle("locale.loc");

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStageObj = primaryStage;
        I18N.setLocale(new Locale("en"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/LoginView.fxml"), resources);
        Scene mainScene = new Scene(root);
        mainScene.setRoot(root);
        mainScene.getStylesheets().add(getClass().getClassLoader().getResource("styles/light.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        NetworkManager networkManager = new NetworkManager(address);
        Thread x = new Thread(networkManager);
        x.start();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            networkManager.finishClient();
        });
    }

    public static void main(String[] args) {
        try {
            if (args.length >= 2) {
                address = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
            } else if (args.length == 1) {
                String[] hostAndPort = args[0].split(":");
                if (hostAndPort.length != 2) {
                    throw new InvalidValueException("");
                }
                address = new InetSocketAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            } else {
                address = new InetSocketAddress("localhost", AppConstant.DEFAULT_PORT);
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            System.exit(-1);
        }

        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }
}
