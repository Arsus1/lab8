package ru.ifmo.lab.ui.model;

import javafx.beans.property.*;
import ru.ifmo.lab.models.Coordinates;
import ru.ifmo.lab.models.Government;
import ru.ifmo.lab.models.Human;

import java.time.LocalDate;

public class TableCityModel {

    private LongProperty id = new SimpleLongProperty();
    private StringProperty name = new SimpleStringProperty();
    private FloatProperty x = new SimpleFloatProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private ObjectProperty<LocalDate> creationDate = new SimpleObjectProperty<>();
    private DoubleProperty area = new SimpleDoubleProperty();
    private LongProperty population = new SimpleLongProperty();
    private LongProperty metersAboveSeaLevel = new SimpleLongProperty();
    private DoubleProperty timezone = new SimpleDoubleProperty();
    private BooleanProperty capital = new SimpleBooleanProperty();
    private StringProperty government = new SimpleStringProperty();
    private IntegerProperty governor = new SimpleIntegerProperty();
    private IntegerProperty userID = new SimpleIntegerProperty();
    private StringProperty username = new SimpleStringProperty();

    public TableCityModel(){}
    public TableCityModel(LongProperty id, StringProperty name, FloatProperty x, DoubleProperty y, ObjectProperty<LocalDate> creationDate, DoubleProperty area, LongProperty population, LongProperty metersAboveSeaLevel, DoubleProperty timezone, BooleanProperty capital, StringProperty government, IntegerProperty governor, IntegerProperty userID, StringProperty username){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.timezone = timezone;
        this.capital = capital;
        this.government = government;
        this.governor = governor;
        this.userID = userID;
        this.username = username;
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public float getX() {
        return x.get();
    }

    public FloatProperty xProperty() {
        return x;
    }

    public void setX(float x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public LocalDate getCreationDate() {
        return creationDate.get();
    }

    public ObjectProperty<LocalDate> creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }

    public double getArea() {
        return area.get();
    }

    public DoubleProperty areaProperty() {
        return area;
    }

    public void setArea(double area) {
        this.area.set(area);
    }

    public long getPopulation() {
        return population.get();
    }

    public LongProperty populationProperty() {
        return population;
    }

    public void setPopulation(long population) {
        this.population.set(population);
    }

    public long getMetersAboveSeaLevel() {
        return metersAboveSeaLevel.get();
    }

    public LongProperty metersAboveSeaLevelProperty() {
        return metersAboveSeaLevel;
    }

    public void setMetersAboveSeaLevel(long metersAboveSeaLevel) {
        this.metersAboveSeaLevel.set(metersAboveSeaLevel);
    }

    public double getTimezone() {
        return timezone.get();
    }

    public DoubleProperty timezoneProperty() {
        return timezone;
    }

    public void setTimezone(double timezone) {
        this.timezone.set(timezone);
    }

    public boolean isCapital() {
        return capital.get();
    }

    public BooleanProperty capitalProperty() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital.set(capital);
    }

    public String getGovernment() {
        return government.get();
    }

    public StringProperty governmentProperty() {
        return government;
    }

    public void setGovernment(String government) {
        this.government.set(government);
    }

    public int getGovernor() {
        return governor.get();
    }

    public IntegerProperty governorProperty() {
        return governor;
    }

    public void setGovernor(int governor) {
        this.governor.set(governor);
    }

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
}
