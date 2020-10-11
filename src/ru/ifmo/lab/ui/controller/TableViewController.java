package ru.ifmo.lab.ui.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TableViewController implements Initializable {
    public TableView table_city;
    public TextField search_tb;
    public Button info_btn;
    public Button clear_btn;
    public Button execute_btn;
    public Button add_btn;

    public TableColumn tv_id;
    public TableColumn tv_name;
    public TableColumn tv_x;
    public TableColumn tv_y;
    public TableColumn tv_date;
    public TableColumn tv_area;
    public TableColumn tv_population;
    public TableColumn tv_mas;
    public TableColumn tv_timezone;
    public TableColumn tv_capital;
    public TableColumn tv_goverment;
    public TableColumn tv_governor;
    public TableColumn tv_username;

    public MenuButton menubtn_filter;
    public ToggleGroup menuToggleGroup;

    private TableColumn sortcolumn = null;
    private TableColumn.SortType st = null;

    private FilteredList<TableCityModel> filteredData;

    private boolean filterIdIsSelect = false;
    private boolean filterNameIsSelect = false;
    private boolean filterXIsSelect = false;
    private boolean filterYIsSelect = false;
    private boolean filterDateIsSelect = false;
    private boolean filterAreaIsSelect = false;
    private boolean filterPopulationIsSelect = false;
    private boolean filterMasIsSelect = false;
    private boolean filterTimezoneIsSelect = false;
    private boolean filterCapitalIsSelect = false;
    private boolean filterGovermentIsSelect = false;
    private boolean filterGovernorIsSelect = false;
    private boolean filterUsernameIsSelect = false;

    public EventListener showEvent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        search_tb.promptTextProperty().bind(I18N.createStringBinding("key.search"));
        info_btn.textProperty().bind(I18N.createStringBinding("key.info"));
        clear_btn.textProperty().bind(I18N.createStringBinding("key.clear_all"));
        execute_btn.textProperty().bind(I18N.createStringBinding("key.execute_script"));
        add_btn.textProperty().bind(I18N.createStringBinding("key.add"));

        tv_name.textProperty().bind(I18N.createStringBinding("key.table_name"));
        tv_date.textProperty().bind(I18N.createStringBinding("key.table_date"));
        tv_area.textProperty().bind(I18N.createStringBinding("key.table_area"));
        tv_population.textProperty().bind(I18N.createStringBinding("key.table_population"));
        tv_mas.textProperty().bind(I18N.createStringBinding("key.table_mas"));
        tv_timezone.textProperty().bind(I18N.createStringBinding("key.table_timezone"));
        tv_capital.textProperty().bind(I18N.createStringBinding("key.table_capital"));
        tv_goverment.textProperty().bind(I18N.createStringBinding("key.table_goverment"));
        tv_governor.textProperty().bind(I18N.createStringBinding("key.table_governor"));
        tv_username.textProperty().bind(I18N.createStringBinding("key.table_username"));

        menubtn_filter.textProperty().bind(I18N.createStringBinding("key.filter_by"));

        tv_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tv_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tv_x.setCellValueFactory(new PropertyValueFactory<>("X"));
        tv_y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        tv_date.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        tv_area.setCellValueFactory(new PropertyValueFactory<>("Area"));
        tv_population.setCellValueFactory(new PropertyValueFactory<>("Population"));
        tv_mas.setCellValueFactory(new PropertyValueFactory<>("MetersAboveSeaLevel"));
        tv_timezone.setCellValueFactory(new PropertyValueFactory<>("Timezone"));
        tv_capital.setCellValueFactory(new PropertyValueFactory<>("Capital"));
        tv_goverment.setCellValueFactory(new PropertyValueFactory<>("Government"));
        tv_governor.setCellValueFactory(new PropertyValueFactory<>("Governor"));
        tv_username.setCellValueFactory(new PropertyValueFactory<>("Username"));

        refreshTableLocale();
        makeContextMenuFilter();

        search_tb.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(city -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (Long.valueOf(city.getId()).toString().toLowerCase().contains(lowerCaseFilter) && filterIdIsSelect) {
                    return true;
                }

                if (city.getName().toLowerCase().contains(lowerCaseFilter) && filterNameIsSelect) {
                    return true;
                }

                if (Float.valueOf(city.getX()).toString().toLowerCase().contains(lowerCaseFilter) && filterXIsSelect) {
                    return true;
                }

                if (Double.valueOf(city.getY()).toString().toLowerCase().contains(lowerCaseFilter) && filterYIsSelect) {
                    return true;
                }

                if (city.getCreationDate().toString().toLowerCase().contains(lowerCaseFilter) && filterDateIsSelect) {
                    return true;
                }

                if (Double.valueOf(city.getArea()).toString().toLowerCase().contains(lowerCaseFilter) && filterAreaIsSelect) {
                    return true;
                }

                if (Long.valueOf(city.getPopulation()).toString().toLowerCase().contains(lowerCaseFilter) && filterPopulationIsSelect) {
                    return true;
                }

                if (Long.valueOf(city.getMetersAboveSeaLevel()).toString().toLowerCase().contains(lowerCaseFilter) && filterMasIsSelect) {
                    return true;
                }

                if (Double.valueOf(city.getTimezone()).toString().toLowerCase().contains(lowerCaseFilter) && filterTimezoneIsSelect) {
                    return true;
                }

                if (Boolean.valueOf(city.isCapital()).toString().toLowerCase().contains(lowerCaseFilter) && filterCapitalIsSelect) {
                    return true;
                }

                if (city.getGovernment().toLowerCase().contains(lowerCaseFilter) && filterGovermentIsSelect) {
                    return true;
                }

                if (Integer.valueOf(city.getGovernor()).toString().toLowerCase().contains(lowerCaseFilter) && filterGovernorIsSelect) {
                    return true;
                }

                if (city.getUsername().toLowerCase().contains(lowerCaseFilter) && filterUsernameIsSelect) {
                    return true;
                }

                return false;
            });
        });

        table_city.setRowFactory(param -> {
            TableRow<TableCityModel> row = new TableRow<>();
            MenuItem edit = new MenuItem();
            edit.textProperty().bind(I18N.createStringBinding("key.edit"));
            edit.setOnAction(event1 -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), resources);
                    Parent root = loader.load();
                    AddUpdateView addUpdateView = loader.getController();
                    addUpdateView.transferData(row.getItem(), true);

                    Stage stage = new Stage();
                    stage.setResizable(false);

                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/light.css").toExternalForm());
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.titleProperty().bind(I18N.createStringBinding("key.update"));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            MenuItem remove = new MenuItem();
            remove.textProperty().bind(I18N.createStringBinding("key.remove"));
            remove.setOnAction(event2 -> {
                NetworkManager.getInstance().remove(row.getItem().getId(), new EventListener() {
                    @Override
                    public void onResponse(Object event) {
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            });

            ContextMenu rowMenu = new ContextMenu(edit, remove);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu).otherwise((ContextMenu) null));

            return row;
        });

        showEvent = new EventListener() {
            @Override
            public void onResponse(Object event) {
                Platform.runLater(() -> {
                    if (table_city.getSortOrder().size() > 0) {
                        sortcolumn = (TableColumn) table_city.getSortOrder().get(0);
                        st = sortcolumn.getSortType();
                    }

                    List<City> cityList = (List) event;
                    ObservableList<TableCityModel> cityModels = FXCollections.observableArrayList();

                    cityList.forEach(x -> {
                        TableCityModel cityModel = new TableCityModel();
                        cityModel.setId(x.getId());
                        cityModel.setName(x.getName());
                        cityModel.setX(x.getCoordinates().getX());
                        cityModel.setY(x.getCoordinates().getY());
                        cityModel.setCreationDate(x.getCreationDate());
                        cityModel.setArea(x.getArea());
                        cityModel.setPopulation(x.getPopulation());
                        cityModel.setMetersAboveSeaLevel(x.getMetersAboveSeaLevel());
                        cityModel.setTimezone(x.getTimezone());
                        cityModel.setGovernment(x.getGovernment().name());
                        cityModel.setGovernor(x.getGovernor().getAge());
                        cityModel.setUserID(x.getUserID());
                        cityModel.setCapital(x.getCapital());
                        cityModel.setUsername(x.getUsername());

                        cityModels.add(cityModel);
                    });

                    filteredData = new FilteredList<>(cityModels, p -> true);
                    SortedList<TableCityModel> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(table_city.comparatorProperty());

                    table_city.setItems(sortedData);

                    notifySearchTextField();

                    if (sortcolumn != null) {
                        table_city.getSortOrder().add(sortcolumn);
                        sortcolumn.setSortType(st);
                        sortcolumn.setSortable(true);
                    }
                });
            }

            @Override
            public void onError(Object message) {

            }
        };

        show();
    }

    public void show(){
        NetworkManager.getInstance().show(showEvent);
    }

    public void onAddClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), ClientMainLauncher.resources);
            Parent root = loader.load();
            AddUpdateView addUpdateView = loader.getController();
            addUpdateView.transferData(null, false);

            Stage stage = new Stage();
            stage.setResizable(false);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/light.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.titleProperty().bind(I18N.createStringBinding("key.add"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExecuteClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/ExecuteView.fxml"), ClientMainLauncher.resources);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setResizable(false);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/light.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.titleProperty().bind(I18N.createStringBinding("key.execute_script"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClearAllClick(ActionEvent actionEvent) {
        NetworkManager.getInstance().clear(new EventListener() {
            @Override
            public void onResponse(Object event) {
                showErrorDialog((String)event);
            }

            @Override
            public void onError(Object message) {
                showErrorDialog((String)message);
            }
        });
    }

    public void onInfoClick(ActionEvent actionEvent) {
        NetworkManager.getInstance().info(new EventListener() {
            @Override
            public void onResponse(Object event) {
                showErrorDialog((String)event);
            }

            @Override
            public void onError(Object message) {

            }
        });
    }


    public void refreshTableLocale() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(I18N.getLocale());
        tv_date.setCellFactory(column -> new TableCell<TableCityModel, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(formatter.format(date));
                }
            }
        });
    }

    private void makeContextMenuFilter(){
        RadioMenuItem id_ci = new RadioMenuItem("Id");
        id_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterIdIsSelect = newValue; notifySearchTextField(); });
        id_ci.setSelected(true);

        RadioMenuItem name_ci = new RadioMenuItem();
        name_ci.textProperty().bind(I18N.createStringBinding("key.table_name"));
        name_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterNameIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem x_ci = new RadioMenuItem("X");
        x_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterXIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem y_ci = new RadioMenuItem("Y");
        y_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterYIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem date_ci = new RadioMenuItem();
        date_ci.textProperty().bind(I18N.createStringBinding("key.table_date"));
        date_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterDateIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem area_ci = new RadioMenuItem();
        area_ci.textProperty().bind(I18N.createStringBinding("key.table_area"));
        area_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterAreaIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem population_ci = new RadioMenuItem();
        population_ci.textProperty().bind(I18N.createStringBinding("key.table_population"));
        population_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterPopulationIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem mas_ci = new RadioMenuItem();
        mas_ci.textProperty().bind(I18N.createStringBinding("key.table_mas"));
        mas_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterMasIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem timezone_ci = new RadioMenuItem();
        timezone_ci.textProperty().bind(I18N.createStringBinding("key.table_timezone"));
        timezone_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterTimezoneIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem capital_ci = new RadioMenuItem();
        capital_ci.textProperty().bind(I18N.createStringBinding("key.table_capital"));
        capital_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterCapitalIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem goverment_ci = new RadioMenuItem();
        goverment_ci.textProperty().bind(I18N.createStringBinding("key.table_goverment"));
        goverment_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterGovermentIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem governor_ci = new RadioMenuItem();
        governor_ci.textProperty().bind(I18N.createStringBinding("key.table_governor"));
        governor_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterGovernorIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem username_ci = new RadioMenuItem();
        username_ci.textProperty().bind(I18N.createStringBinding("key.table_username"));
        username_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterUsernameIsSelect = newValue; notifySearchTextField(); });

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(id_ci);
        toggleGroup.getToggles().add(name_ci);
        toggleGroup.getToggles().add(x_ci);
        toggleGroup.getToggles().add(y_ci);
        toggleGroup.getToggles().add(date_ci);
        toggleGroup.getToggles().add(area_ci);
        toggleGroup.getToggles().add(population_ci);
        toggleGroup.getToggles().add(mas_ci);
        toggleGroup.getToggles().add(timezone_ci);
        toggleGroup.getToggles().add(capital_ci);
        toggleGroup.getToggles().add(goverment_ci);
        toggleGroup.getToggles().add(governor_ci);
        toggleGroup.getToggles().add(username_ci);

        menubtn_filter.getItems().add(id_ci);
        menubtn_filter.getItems().add(name_ci);
        menubtn_filter.getItems().add(x_ci);
        menubtn_filter.getItems().add(y_ci);
        menubtn_filter.getItems().add(date_ci);
        menubtn_filter.getItems().add(area_ci);
        menubtn_filter.getItems().add(population_ci);
        menubtn_filter.getItems().add(mas_ci);
        menubtn_filter.getItems().add(timezone_ci);
        menubtn_filter.getItems().add(capital_ci);
        menubtn_filter.getItems().add(goverment_ci);
        menubtn_filter.getItems().add(governor_ci);
        menubtn_filter.getItems().add(username_ci);


        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    private void notifySearchTextField(){
        String oldValue = search_tb.getText();
        search_tb.setText("");
        search_tb.setText(oldValue);
    }

    public void onEnLang(ActionEvent actionEvent) { I18N.setLocale(Locale.ENGLISH); refreshTableLocale(); }

    public void onRuLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("ru")); refreshTableLocale(); }

    public void onRoLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("ro")); refreshTableLocale(); }

    public void onHrLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("hr")); refreshTableLocale(); }

    public void onEsLang(ActionEvent actionEvent) { I18N.setLocale(new Locale("es_EC")); refreshTableLocale(); }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
