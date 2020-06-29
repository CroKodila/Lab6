package ru.ifmo.lab.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import ru.ifmo.lab.object.Address;
import ru.ifmo.lab.object.Coordinates;
import ru.ifmo.lab.object.Organization;
import ru.ifmo.lab.object.OrganizationType;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.ui.model.TableOrganizationModel;
import ru.ifmo.lab.utilits.I18N;
import ru.ifmo.lab.utilits.NumUtil;


import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class AddUpdateView implements Initializable {
    public TextField id_tb;
    public TextField name_tb;
    public TextField x_tb;
    public TextField y_tb;
    public TextField date_tb;

    public Button add_btn;
    public Label id_lb;
    public Label name_lb;
    public Label x_lb;
    public Label y_lb;
    public Label date_lb;
    public Label annTurn_lb;
    public TextField annTurn_tb;
    public Label fName_lb;
    public TextField fName_tb;
    public Label empC_lb;
    public TextField empC_tb;
    public Label type_lb;
    public ComboBox type_cb;
    public Label street_lb;
    public TextField street_tb;
    public Label zipCode_lb;
    public TextField zipCode_tb;

    private ResourceBundle resources;


    private TableOrganizationModel currentOrganization = new TableOrganizationModel();
    private boolean isUpdate = false;
    private Integer errorCount = 0;


    public void btnOnClick(ActionEvent actionEvent) {
        if(errorCount != 0) showErrorDialog(I18N.get("error.input_ua"));
        else {
            /*
            UPDATE
             */
            if(isUpdate){
                Organization organization = new Organization(
                        currentOrganization.getId(),
                        name_tb.getText(),
                        Long.parseLong(x_tb.getText()),
                        Double.parseDouble(y_tb.getText()),
                        currentOrganization.getCreationDate(),
                        Integer.parseInt(annTurn_tb.getText()),
                        fName_tb.getText(),
                        Long.parseLong(empC_tb.getText()),
                        OrganizationType.valueOf(type_cb.getSelectionModel().getSelectedItem().toString()),
                        street_tb.getText(),
                        zipCode_tb.getText(),
                        currentOrganization.getUserID(),
                        currentOrganization.getUsername()
                );

                NetworkManager.getInstance().update(currentOrganization.getId(), organization, new EventListener() {
                    @Override
                    public void onResponse(Object event) {
                        showErrorDialog((String) event);
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            }else{
                /*
                ADD
                 */
                Organization organization = new Organization(
                        name_tb.getText(),
                        new Coordinates(Long.parseLong(x_tb.getText()), Double.parseDouble(y_tb.getText())),
                        Integer.parseInt(annTurn_tb.getText()),
                        fName_tb.getText(),
                        Long.parseLong(empC_tb.getText()),
                        OrganizationType.valueOf(type_cb.getSelectionModel().getSelectedItem().toString()),
                        new Address(street_tb.getText(), zipCode_tb.getText())
                );

                NetworkManager.getInstance().add(organization, new EventListener() {
                    @Override
                    public void onResponse(Object event) {
                        showErrorDialog((String) event);
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            }
        }
    }

    void transferData(TableOrganizationModel organization, boolean isUpdate){
        this.currentOrganization = organization;
        this.isUpdate = isUpdate;

        add_btn.textProperty().bind(I18N.createStringBinding(isUpdate ? "key.update" : "key.add"));

        type_cb.getSelectionModel().select(0);

        if(isUpdate) {
            id_tb.setText(Long.valueOf(currentOrganization.getId()).toString());
            name_tb.setText(currentOrganization.getName());
            x_tb.setText(Long.valueOf(currentOrganization.getX()).toString());
            y_tb.setText(Double.valueOf(currentOrganization.getY()).toString());
            date_tb.setText(currentOrganization.getCreationDate().toString());
            annTurn_tb.setText(Integer.valueOf(currentOrganization.getAnnualTurnover()).toString());
            fName_tb.setText(currentOrganization.getFullName());
            empC_tb.setText(Long.valueOf(currentOrganization.getEmployeesCount()).toString());
            type_cb.getSelectionModel().select(currentOrganization.getType());
            street_tb.setText(currentOrganization.getStreet());
            zipCode_tb.setText(currentOrganization.getZipCode());
        }else {
            id_tb.setManaged(false);
            id_lb.setManaged(false);
            date_tb.setManaged(false);
            date_lb.setManaged(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        id_lb.textProperty().bind(I18N.createStringBinding("key.id"));
        name_lb.textProperty().bind(I18N.createStringBinding("key.name"));
        x_lb.textProperty().bind(I18N.createStringBinding("key.x"));
        y_lb.textProperty().bind(I18N.createStringBinding("key.y"));
        date_lb.textProperty().bind(I18N.createStringBinding("key.date"));
        annTurn_lb.textProperty().bind(I18N.createStringBinding("key.annualTurnover"));
        fName_lb.textProperty().bind(I18N.createStringBinding("key.fullName"));
        empC_lb.textProperty().bind(I18N.createStringBinding("key.employeesCount"));
        type_lb.textProperty().bind(I18N.createStringBinding("key.type"));
        street_lb.textProperty().bind(I18N.createStringBinding("key.street"));
        zipCode_lb.textProperty().bind(I18N.createStringBinding("key.zipCode"));



        type_cb.getItems().removeAll(type_cb.getItems());
        type_cb.getItems().addAll(OrganizationType.values());

        validateRange(x_tb, NumUtil.LONG_MIN, NumUtil.LONG_MAX, false);
        validateRange(y_tb, new BigDecimal(-587.0), NumUtil.DOUBLE_MAX, false);
        validateRange(annTurn_tb, new BigDecimal(0), NumUtil.INTEGER_MAX, false);
        validateRange(empC_tb, new BigDecimal(0), NumUtil.LONG_MAX, false);
        name_tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(name_tb.getText().isEmpty()) setErrorInput(name_tb);
                else removeErrorInput(name_tb);
            }
        });
        fName_tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(fName_tb.getText().isEmpty()) setErrorInput(fName_tb);
                else removeErrorInput(fName_tb);
            }
        });
        street_tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(street_tb.getText().isEmpty()) setErrorInput(street_tb);
                else removeErrorInput(street_tb);
            }
        });
        zipCode_tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(zipCode_tb.getText().isEmpty()) setErrorInput(zipCode_tb);
                else removeErrorInput(zipCode_tb);
            }
        });
    }

    private void validateRange(TextField tb, BigDecimal min, BigDecimal max, boolean isFloat){
        String intPattern = "^[-]?\\d*$";
        String decimalPattern = "^[-]?\\d+([.,]\\d+)?$";
        tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                try {
                    if(tb.getText().matches(isFloat ? decimalPattern : intPattern) && !tb.getText().isEmpty()) {
                        NumberFormat format = NumberFormat.getInstance();
                        if (!NumUtil.isInRange(format.parse(tb.getText().replace(',', '.')), min, max)) {
                            setErrorInput(tb);
                        } else removeErrorInput(tb);
                    }else setErrorInput(tb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setErrorInput(TextField tb){
        if(tb.getStyle().isEmpty()) {
            tb.setStyle("-fx-text-fill:red; -fx-border-color: red;");
            errorCount++;
        }
    }

    private void removeErrorInput(TextField tb){
        if(!tb.getStyle().isEmpty()) {
            tb.setStyle(null);
            errorCount--;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
