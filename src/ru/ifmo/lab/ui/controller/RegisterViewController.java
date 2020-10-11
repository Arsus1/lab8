package ru.ifmo.lab.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.ifmo.lab.database.CurrentUser;
import ru.ifmo.lab.network.Client;
import ru.ifmo.lab.ui.ClientMainLauncher;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.LoginListener;
import ru.ifmo.lab.utils.I18N;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {
    public TextField user;
    public PasswordField password;
    public PasswordField cpassword;
    public Button registerButton;
    public Hyperlink returnButton;
    public Label register_lb;
    public Label user_lb;
    public Label password_lb;
    public Label cpassword_lb;

    public void registerOnClick(ActionEvent actionEvent) {
        if(!password.getText().isEmpty() && !cpassword.getText().isEmpty() && !user.getText().isEmpty()){
            if(!password.getText().equals(cpassword.getText())) {
                showErrorDialog("The Confirm Password does not match");
                return;
            }

            NetworkManager.getInstance().register(user.getText(), password.getText(), new LoginListener() {
                @Override
                public void onLoginSuccessful(CurrentUser user) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MainViewTest.fxml"), ClientMainLauncher.resources);
                                ClientMainLauncher.getPrimaryStage().setScene(new Scene(root));
                                ClientMainLauncher.getPrimaryStage().setResizable(true);
                                ClientMainLauncher.getPrimaryStage().centerOnScreen();
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
        }else {
            showErrorDialog("Please fill all fields");
        }
    }

    public void returnLoginOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/LoginView.fxml"), ClientMainLauncher.resources);
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
        register_lb.textProperty().bind(I18N.createStringBinding("key.registerText"));
        password_lb.textProperty().bind(I18N.createStringBinding("key.passwordText"));
        cpassword_lb.textProperty().bind(I18N.createStringBinding("key.cpasswordText"));
        user_lb.textProperty().bind(I18N.createStringBinding("key.userText"));
        user_lb.textProperty().bind(I18N.createStringBinding("key.userText"));
        registerButton.textProperty().bind(I18N.createStringBinding("key.registerText"));
        returnButton.textProperty().bind(I18N.createStringBinding("key.alreadyAcc"));
    }
}
