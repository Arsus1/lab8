package ru.ifmo.lab.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.ifmo.lab.database.CurrentUser;
import ru.ifmo.lab.ui.ClientMainLauncher;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.LoginListener;
import ru.ifmo.lab.utils.I18N;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {
    public Button loginButton;
    public TextField user;
    public PasswordField password;
    public Hyperlink registerButton;
    public Label welcome_lb;
    public Label user_text_lb;
    public Label password_text_lb;

    public void onLoginClick(ActionEvent actionEvent) {

        NetworkManager.getInstance().login(user.getText(), password.getText(), new LoginListener() {
            @Override
            public void onLoginSuccessful(CurrentUser user) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MainViewTest.fxml"), ClientMainLauncher.resources);
                                Scene scene = new Scene(root);
                                scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/light.css").toExternalForm());
                                ClientMainLauncher.getPrimaryStage().setScene(scene);
                                ClientMainLauncher.getPrimaryStage().setResizable(false);
                                ClientMainLauncher.getPrimaryStage().setWidth(1400);
                                ClientMainLauncher.getPrimaryStage().setHeight(700);
                                ClientMainLauncher.getPrimaryStage().centerOnScreen();
                                ClientMainLauncher.getPrimaryStage().titleProperty().bind(I18N.createStringBinding("key.login_as", user.getCredentials().username));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }

            @Override
            public void onLoginError(String message) {
                showErrorDialog(message);
            }
        });
    }

    public void onRegisterClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/RegisterView.fxml"), ClientMainLauncher.resources);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/light.css").toExternalForm());
        ClientMainLauncher.getPrimaryStage().setScene(scene);
    }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcome_lb.textProperty().bind(I18N.createStringBinding("key.welcome"));
        user_text_lb.textProperty().bind(I18N.createStringBinding("key.userText"));
        password_text_lb.textProperty().bind(I18N.createStringBinding("key.passwordText"));
        loginButton.textProperty().bind(I18N.createStringBinding("key.loginText"));
        registerButton.textProperty().bind(I18N.createStringBinding("key.newUserText"));
    }
}
