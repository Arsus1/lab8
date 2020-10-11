package ru.ifmo.lab.ui.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.ui.ClientMainLauncher;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.ui.model.TableCityModel;
import ru.ifmo.lab.utils.I18N;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;


public class MainViewController implements Initializable {

    public MenuItem logout;
    public Menu menu_user;
    public Menu menu_settings;
    public MenuItem menu_lang_eng;
    public MenuItem menu_lang_ru;
    public MenuItem menu_lang_slv;
    public MenuItem menu_lang_hr;
    public MenuItem menu_lang_es;
    public MenuButton menubtn_filter;
    public ToggleGroup menuToggleGroup;
    public HBox mainPane;
    public ToggleButton toggleTable;
    public ToggleButton toggleVisual;

    private ResourceBundle resources;

    private VBox tablePane;
    private TableViewController tableViewController;
    private boolean tableViewInit = false;

    private VBox visualPane;
    private VisualViewController visualViewController;
    private boolean visualViewInit = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        try {
            FXMLLoader tableLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/TableView.fxml"), ClientMainLauncher.resources);
            tablePane = tableLoader.load();
            tableViewController = tableLoader.getController();

            FXMLLoader visualLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/VisualView.fxml"), ClientMainLauncher.resources);
            visualPane = visualLoader.load();
            visualViewController = visualLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }


        menu_user.textProperty().bind(I18N.createStringBinding("key.user_Text"));
        menu_settings.textProperty().bind(I18N.createStringBinding("key.settingsText"));
        logout.textProperty().bind(I18N.createStringBinding("key.logoutText"));
        toggleTable.textProperty().bind(I18N.createStringBinding("key.table"));
        toggleVisual.textProperty().bind(I18N.createStringBinding("key.visual"));

        switchPanes();
        toggleTable.setSelected(true);
        menuToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
            else if(oldVal != null){
                switchPanes();
            }
        });
    }


    private void switchPanes(){
        if(!tableViewInit) {
            HBox.setHgrow(tablePane, Priority.ALWAYS);
            tablePane.setFillWidth(true);
            if(visualViewInit) mainPane.getChildren().remove(visualPane);
            NetworkManager.getInstance().showEvent = tableViewController.showEvent;
            mainPane.getChildren().add(tablePane);
            tableViewController.show();

            tableViewInit = true;
            visualViewInit = false;
        }else {
            HBox.setHgrow(visualPane, Priority.ALWAYS);
            visualPane.setFillWidth(true);
            if(tableViewInit) mainPane.getChildren().remove(tablePane);
            visualViewController.map_img.fitWidthProperty().bind(mainPane.widthProperty());
            NetworkManager.getInstance().showEvent = visualViewController.showEvent;
            visualViewController.draw(mainPane.getWidth(), mainPane.getHeight());
            mainPane.getChildren().add(visualPane);

            tableViewInit = false;
            visualViewInit = true;
        }
    }

    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/LoginView.fxml"), ClientMainLauncher.resources);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/light.css").toExternalForm());
        ClientMainLauncher.getPrimaryStage().setScene(scene);
        ClientMainLauncher.getPrimaryStage().setHeight(330);
        ClientMainLauncher.getPrimaryStage().setWidth(600);
        ClientMainLauncher.getPrimaryStage().setResizable(false);
        ClientMainLauncher.getPrimaryStage().titleProperty().unbind();
        ClientMainLauncher.getPrimaryStage().setTitle("");
        ClientMainLauncher.getPrimaryStage().centerOnScreen();

        NetworkManager.getInstance().logout();
    }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }



    public void onEnLang(ActionEvent actionEvent) { I18N.setLocale(Locale.ENGLISH); tableViewController.refreshTableLocale(); }

    public void onRuLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("ru")); tableViewController.refreshTableLocale(); }

    public void onRoLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("slv")); tableViewController.refreshTableLocale(); }

    public void onHrLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("hr")); tableViewController.refreshTableLocale(); }

    public void onEsLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("es_MX")); tableViewController.refreshTableLocale(); }
}
