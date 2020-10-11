package ru.ifmo.lab.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ru.ifmo.lab.commands.ExecuteScriptCommand;
import ru.ifmo.lab.network.CommandReader;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.utils.I18N;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ExecuteViewController implements Initializable {
    public TextField file_path_tb;
    public Button file_choose_btn;
    public TextArea output_area;
    private FileChooser fileChooser = new FileChooser();

    public void onFIleChoose(ActionEvent actionEvent) {
        output_area.clear();
        File selectedFile = fileChooser.showOpenDialog(file_choose_btn.getScene().getWindow());
        if(selectedFile != null) {
            file_path_tb.setText(selectedFile.getAbsolutePath());
            ExecuteScriptCommand esc = new ExecuteScriptCommand();
            esc.setArgs(new String[]{selectedFile.getAbsolutePath()});
            NetworkManager.getInstance().send(esc);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        file_path_tb.promptTextProperty().bind(I18N.createStringBinding("key.file_path"));
        CommandReader.getInstance().executeEvent =
        NetworkManager.getInstance().addEvent =
                NetworkManager.getInstance().updateEvent =
                        NetworkManager.getInstance().infoEvent =
                                NetworkManager.getInstance().removeEvent =
                                        NetworkManager.getInstance().clearEvent =
                                            new EventListener() {
                                                @Override
                                                public void onResponse(Object event) {
                                                    Platform.runLater(() -> {
                                                        output_area.appendText((String) event + "\n");
                                                    });
                                                }

                                                @Override
                                                public void onError(Object message) {
                                                    Platform.runLater(() -> {
                                                        output_area.appendText((String) message + "\n");
                                                    });
                                                }
                                            };
    }
}
