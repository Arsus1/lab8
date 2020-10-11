package ru.ifmo.lab.ui.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.ui.ClientMainLauncher;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.ui.model.TableCityModel;
import ru.ifmo.lab.utils.I18N;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class VisualViewController implements Initializable {
    public ImageView map_img;
    public EventListener showEvent;
    public Pane mainPane;

    private ResourceBundle resources;

    private double mainW;
    private double mainH;
    private Dimension panelSize;

    private HashMap<Long, Circle> circleHashMap = new HashMap<>();
    private List<City> bufCityList = null;

    private Tooltip tooltip;

    //TODO add context menu, localize all labels, color circles by name
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        tooltip = new Tooltip();

        javafx.scene.control.MenuItem add = new MenuItem();
        add.textProperty().bind(I18N.createStringBinding("key.add"));
        add.setOnAction(event1 -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), resources);
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
        });

        ContextMenu contextMenu = new ContextMenu(add);
        map_img.setOnContextMenuRequested(e -> contextMenu.show(map_img, e.getScreenX(), e.getScreenY()));

        showEvent = new EventListener() {
            @Override
            public void onResponse(Object event) {
                Platform.runLater(() -> {
                    List<City> cityList = (List) event;
                    updatePoints(cityList);
                });
            }

            @Override
            public void onError(Object message) {

            }

        };
    }

    void draw(double w, double h){
        this.mainW = w;
        this.mainH = h;

        panelSize = new Dimension(Double.valueOf(w).intValue()/2, Double.valueOf(h).intValue()/2);
        NetworkManager.getInstance().show(showEvent);
    }

    private void updatePoints(List<City> cityList){
        if(bufCityList == null){
            bufCityList = cityList;
            bufCityList.forEach(this::addPoint);
        } else {
            List<Long> deletedCity = bufCityList.stream().filter(o1 -> cityList.stream().noneMatch(o2 -> o2.getId().equals(o1.getId()))).map(City::getId)
                    .collect(Collectors.toList());

            List<City> addedCity = cityList.stream().filter(o1 -> bufCityList.stream().noneMatch(o2 -> o2.getId().equals(o1.getId())))
                    .collect(Collectors.toList());

            Map<Long, City> changes = cityList.stream().filter(k -> bufCityList.stream().allMatch(v -> v.compare(k)))
                    .collect(Collectors.toMap(City::getId, x -> x));

            deletedCity.forEach(this::removeById);
            addedCity.forEach(this::addPoint);
            changes.forEach(this::changePoint);

            bufCityList = cityList;
        }
    }

    private void addPoint(City city){
        int diam;
        if (city.getArea() <= 50) {
            diam = 5;
        } else if (city.getArea() <= 200) {
            diam = 10;
        } else diam = 15;

        double x_coord = city.getCoordinates().getX() % panelSize.width + panelSize.width +  10.0;
        double y_coord = city.getCoordinates().getY() % panelSize.height + panelSize.height + 10.0;

        int red = city.getUsername().hashCode()*67%255;
        int green = city.getUsername().hashCode()*54%255;
        int blue = city.getUsername().hashCode()*78%255;


        Circle circle = new Circle();
        circle.setFill(Color.rgb(red, green, blue));
        circle.setCenterX(x_coord);
        circle.setCenterY(y_coord);
        circle.setRadius(diam);
        circle.setCursor(Cursor.HAND);
        circle.setUserData(city);



        circle.setOnMouseEntered(event -> {
            Circle self = (Circle) event.getSource();
            City _city = (City) self.getUserData();

            double eventX = event.getX();
            double eventY = event.getY();
            double tooltipX = eventX + self.getScene().getX() + self.getScene().getWindow().getX() + 200;
            double tooltipY = eventY + self.getScene().getY() + self.getScene().getWindow().getY() ;

            tooltip.setText(_city.getName());
            tooltip.show(circle, tooltipX, tooltipY);
        });

        circle.setOnMouseExited(event -> {
            tooltip.hide();
        });


        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY){
                    Circle self = (Circle) event.getSource();
                    City city = (City) self.getUserData();
                    TableCityModel cityModel = new TableCityModel();
                    cityModel.setId(city.getId());
                    cityModel.setName(city.getName());
                    cityModel.setX(city.getCoordinates().getX());
                    cityModel.setY(city.getCoordinates().getY());
                    cityModel.setCreationDate(city.getCreationDate());
                    cityModel.setArea(city.getArea());
                    cityModel.setPopulation(city.getPopulation());
                    cityModel.setMetersAboveSeaLevel(city.getMetersAboveSeaLevel());
                    cityModel.setTimezone(city.getTimezone());
                    cityModel.setGovernment(city.getGovernment().name());
                    cityModel.setGovernor(city.getGovernor().getAge());
                    cityModel.setUserID(city.getUserID());
                    cityModel.setCapital(city.getCapital());
                    cityModel.setUsername(city.getUsername());

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), resources);
                        Parent root = loader.load();
                        AddUpdateView addUpdateView = loader.getController();
                        addUpdateView.transferData(cityModel, true);

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
                }
            }
        });

        mainPane.getChildren().add(circle);
        circleHashMap.put(city.getId(), circle);
    }

    private void removeById(Long id){
        mainPane.getChildren().remove(circleHashMap.get(id));
        circleHashMap.remove(id);
    }

    private void changePoint(Long id, City city){
        int diam;
        if (city.getArea() <= 50) {
            diam = 5;
        } else if (city.getArea() <= 200) {
            diam = 10;
        } else diam = 15;

        double x_coord = city.getCoordinates().getX() % panelSize.width + panelSize.width +  10.0;
        double y_coord = city.getCoordinates().getY() % panelSize.height + panelSize.height + 10.0;
        Circle circle = circleHashMap.get(id);
        if(circle != null) {
            circle.setCenterX(x_coord);
            circle.setCenterY(y_coord);
            circle.setUserData(city);

            Timeline timeline = new Timeline();
            KeyValue keyRadius = new KeyValue(circle.radiusProperty(), diam);
            Duration duration = Duration.millis(2000);
            KeyFrame keyFrame = new KeyFrame(duration, event -> {} , keyRadius);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

            circleHashMap.put(id, circle);
        }
    }
}
