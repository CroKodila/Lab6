package ru.ifmo.lab.ui.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import ru.ifmo.lab.object.Organization;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.ui.model.TableOrganizationModel;
import ru.ifmo.lab.utilits.I18N;

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
    private List<Organization> bufOrgList = null;

    private Tooltip tooltip;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        tooltip = new Tooltip();

        MenuItem add = new MenuItem();
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
                    List<Organization> organizationList = (List) event;
                    updatePoints(organizationList);
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

    private void updatePoints(List<Organization> cityList){
        if(bufOrgList == null){
            bufOrgList = cityList;
            bufOrgList.forEach(this::addPoint);
        } else {
            List<Long> deletedOrg = bufOrgList.stream().filter(o1 -> cityList.stream().noneMatch(o2 -> o2.getId().equals(o1.getId()))).map(Organization::getId)
                    .collect(Collectors.toList());

            List<Organization> addedOrg = cityList.stream().filter(o1 -> bufOrgList.stream().noneMatch(o2 -> o2.getId().equals(o1.getId())))
                    .collect(Collectors.toList());

            Map<Long, Organization> changes = cityList.stream().filter(k -> bufOrgList.stream().allMatch(v -> v.compare(k)))
                    .collect(Collectors.toMap(Organization::getId, x -> x));

            deletedOrg.forEach(this::removeById);
            addedOrg.forEach(this::addPoint);
            changes.forEach(this::changePoint);

            bufOrgList = cityList;
        }
    }

    private void addPoint(Organization organization){
        int diam;
        if (organization.getAnnualTurnover() <= 50) {
            diam = 5;
        } else if (organization.getAnnualTurnover() <= 200) {
            diam = 10;
        } else diam = 15;

        double x_coord = organization.getCoordinates().getX() % panelSize.width + panelSize.width +  10.0;
        double y_coord = organization.getCoordinates().getY() % panelSize.height + panelSize.height + 10.0;

        int red = organization.getUsername().hashCode()*67%255;
        int green = organization.getUsername().hashCode()*54%255;
        int blue = organization.getUsername().hashCode()*78%255;


        Circle circle = new Circle();
        circle.setFill(Color.rgb(red, green, blue));
        circle.setCenterX(x_coord);
        circle.setCenterY(y_coord);
        circle.setRadius(diam);
        circle.setCursor(Cursor.HAND);
        circle.setUserData(organization);



        circle.setOnMouseEntered(event -> {
            Circle self = (Circle) event.getSource();
            Organization _organization = (Organization) self.getUserData();

            double eventX = event.getX();
            double eventY = event.getY();
            double tooltipX = eventX + self.getScene().getX() + self.getScene().getWindow().getX() + 200;
            double tooltipY = eventY + self.getScene().getY() + self.getScene().getWindow().getY() ;

            tooltip.setText(_organization.getName());
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
                    Organization organization = (Organization) self.getUserData();
                    TableOrganizationModel organizationModel = new TableOrganizationModel();
                    organizationModel.setId(organization.getId());
                    organizationModel.setName(organization.getName());
                    organizationModel.setX(organization.getCoordinates().getX());
                    organizationModel.setY(organization.getCoordinates().getY());
                    organizationModel.setCreationDate(organization.getCreationDate());
                    organizationModel.setAnnualTurnover(organization.getAnnualTurnover());
                    organizationModel.setFullName(organization.getFullName());
                    organizationModel.setEmployeesCount(organization.getEmployeesCount());
                    organizationModel.setType(organization.getType().name());
                    organizationModel.setStreet(organization.getPostalAddress().getStreet());
                    organizationModel.setZipCode(organization.getPostalAddress().getZipCode());
                    organizationModel.setUserID(organization.getUserID());
                    organizationModel.setUsername(organization.getUsername());

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddUpdateView.fxml"), resources);
                        Parent root = loader.load();
                        AddUpdateView addUpdateView = loader.getController();
                        addUpdateView.transferData(organizationModel, true);

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
        circleHashMap.put(organization.getId(), circle);
    }

    private void removeById(Long id){
        mainPane.getChildren().remove(circleHashMap.get(id));
        circleHashMap.remove(id);
    }

    private void changePoint(Long id, Organization organization){
        int diam;
        if (organization.getAnnualTurnover() <= 50) {
            diam = 5;
        } else if (organization.getAnnualTurnover() <= 200) {
            diam = 10;
        } else diam = 15;

        double x_coord = organization.getCoordinates().getX() % panelSize.width + panelSize.width +  10.0;
        double y_coord = organization.getCoordinates().getY() % panelSize.height + panelSize.height + 10.0;
        Circle circle = circleHashMap.get(id);
        if(circle != null) {
            circle.setCenterX(x_coord);
            circle.setCenterY(y_coord);
            circle.setUserData(organization);

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
