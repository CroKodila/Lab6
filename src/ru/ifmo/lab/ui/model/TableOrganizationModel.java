package ru.ifmo.lab.ui.model;

import javafx.beans.property.*;
import ru.ifmo.lab.object.Address;


import java.time.LocalDate;

public class TableOrganizationModel {

    private LongProperty id = new SimpleLongProperty();
    private StringProperty name = new SimpleStringProperty();
    private LongProperty x = new SimpleLongProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private ObjectProperty<LocalDate> creationDate = new SimpleObjectProperty<>();
    private IntegerProperty annualTurnover = new SimpleIntegerProperty();
    private StringProperty fullName = new SimpleStringProperty();
    private LongProperty employeesCount = new SimpleLongProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty street = new SimpleStringProperty();
    private StringProperty zipCode = new SimpleStringProperty();
    private IntegerProperty userID = new SimpleIntegerProperty();
    private StringProperty username = new SimpleStringProperty();

    public TableOrganizationModel() {
    }

    public TableOrganizationModel(LongProperty id, StringProperty name, LongProperty x, DoubleProperty y, ObjectProperty<LocalDate> creationDate, IntegerProperty annualTurnover, StringProperty fullName, LongProperty employeesCount, StringProperty type, StringProperty street, StringProperty zipCode,  IntegerProperty userID, StringProperty username) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = type;
        this.street = street;
        this.zipCode = zipCode;
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

    public long getX() {
        return x.get();
    }

    public LongProperty xProperty() {
        return x;
    }

    public void setX(long x) {
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

    public int getAnnualTurnover() {
        return annualTurnover.get();
    }

    public IntegerProperty annualTurnoverProperty() {
        return annualTurnover;
    }

    public void setAnnualTurnover(int annualTurnover) {
        this.annualTurnover.set(annualTurnover);
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public long getEmployeesCount() {
        return employeesCount.get();
    }

    public LongProperty employeesCountProperty() {
        return employeesCount;
    }

    public void setEmployeesCount(long employeesCount) {
        this.employeesCount.set(employeesCount);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public StringProperty zipCodeProperty() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }
}
