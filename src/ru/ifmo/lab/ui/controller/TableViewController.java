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

import ru.ifmo.lab.object.Organization;
import ru.ifmo.lab.ui.ClientMainLauncher;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.ui.model.TableOrganizationModel;
import ru.ifmo.lab.utilits.I18N;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TableViewController implements Initializable {
    public TableView table_organization;
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
    public TableColumn tv_username;

    public MenuButton menubtn_filter;
    public TableColumn tv_annTurn;
    public TableColumn tv_fName;
    public TableColumn tv_empC;
    public TableColumn tv_type;
    public TableColumn tv_street;
    public TableColumn tv_zipCode;

    private TableColumn sortcolumn = null;
    private TableColumn.SortType st = null;

    private FilteredList<TableOrganizationModel> filteredData;

    private boolean filterIdIsSelect = false;
    private boolean filterNameIsSelect = false;
    private boolean filterXIsSelect = false;
    private boolean filterYIsSelect = false;
    private boolean filterDateIsSelect = false;
    private boolean filterAnnualTurnoverIsSelect = false;
    private boolean filterFullNameIsSelect = false;
    private boolean filterEmployeesCountisSelect = false;
    private boolean filterTypeIsSelect = false;
    private boolean filterStreetIsSelect= false;
    private boolean filterZipCodeIsSelect = false;
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
        tv_annTurn.textProperty().bind(I18N.createStringBinding("key.table_annTurn"));
        tv_fName.textProperty().bind(I18N.createStringBinding("key.table_fName"));
        tv_empC.textProperty().bind(I18N.createStringBinding("key.table_empC"));
        tv_type.textProperty().bind(I18N.createStringBinding("key.table_type"));
        tv_street.textProperty().bind(I18N.createStringBinding("key.table_street"));
        tv_zipCode.textProperty().bind(I18N.createStringBinding("key.table_zipCode"));
        tv_username.textProperty().bind(I18N.createStringBinding("key.table_username"));

        menubtn_filter.textProperty().bind(I18N.createStringBinding("key.filter_by"));

        tv_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tv_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tv_x.setCellValueFactory(new PropertyValueFactory<>("X"));
        tv_y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        tv_date.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        tv_annTurn.setCellValueFactory(new PropertyValueFactory<>("AnnualTurnover"));
        tv_fName.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        tv_empC.setCellValueFactory(new PropertyValueFactory<>("EmployeesCount"));
        tv_type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        tv_street.setCellValueFactory(new PropertyValueFactory<>("Street"));
        tv_zipCode.setCellValueFactory(new PropertyValueFactory<>("ZipCode"));
        tv_username.setCellValueFactory(new PropertyValueFactory<>("Username"));

        refreshTableLocale();
        makeContextMenuFilter();

        search_tb.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(organization -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (Long.valueOf(organization.getId()).toString().toLowerCase().contains(lowerCaseFilter) && filterIdIsSelect) {
                    return true;
                }

                if (organization.getName().toLowerCase().contains(lowerCaseFilter) && filterNameIsSelect) {
                    return true;
                }

                if (Float.valueOf(organization.getX()).toString().toLowerCase().contains(lowerCaseFilter) && filterXIsSelect) {
                    return true;
                }

                if (Double.valueOf(organization.getY()).toString().toLowerCase().contains(lowerCaseFilter) && filterYIsSelect) {
                    return true;
                }

                if (organization.getCreationDate().toString().toLowerCase().contains(lowerCaseFilter) && filterDateIsSelect) {
                    return true;
                }

                if (Integer.valueOf((organization.getAnnualTurnover())).toString().toLowerCase().contains(lowerCaseFilter) && filterAnnualTurnoverIsSelect) {
                    return true;
                }

                if (organization.getFullName().toLowerCase().contains(lowerCaseFilter) && filterFullNameIsSelect) {
                    return true;
                }

                if (Long.valueOf(organization.getEmployeesCount()).toString().toLowerCase().contains(lowerCaseFilter) && filterEmployeesCountisSelect) {
                    return true;
                }

                if (organization.getType().toLowerCase().contains(lowerCaseFilter) && filterTypeIsSelect) {
                    return true;
                }

                if (organization.getStreet().toLowerCase().contains(lowerCaseFilter) && filterStreetIsSelect) {
                    return true;
                }
                if (organization.getZipCode().toLowerCase().contains(lowerCaseFilter) && filterZipCodeIsSelect) {
                    return true;
                }

                if (organization.getUsername().toLowerCase().contains(lowerCaseFilter) && filterUsernameIsSelect) {
                    return true;
                }

                return false;
            });
        });

        table_organization.setRowFactory(param -> {
            TableRow<TableOrganizationModel> row = new TableRow<>();
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
                    if (table_organization.getSortOrder().size() > 0) {
                        sortcolumn = (TableColumn) table_organization.getSortOrder().get(0);
                        st = sortcolumn.getSortType();
                    }

                    List<Organization> organizationList = (List) event;
                    ObservableList<TableOrganizationModel> organizationModels = FXCollections.observableArrayList();

                    organizationList.forEach(x -> {
                        TableOrganizationModel organizationModel = new TableOrganizationModel();
                        organizationModel.setId(x.getId());
                        organizationModel.setName(x.getName());
                        organizationModel.setX(x.getCoordinates().getX());
                        organizationModel.setY(x.getCoordinates().getY());
                        organizationModel.setCreationDate(x.getCreationDate());
                        organizationModel.setAnnualTurnover(x.getAnnualTurnover());
                        organizationModel.setFullName(x.getFullName());
                        organizationModel.setEmployeesCount(x.getEmployeesCount());
                        organizationModel.setType(x.getType().name());
                        organizationModel.setStreet(x.getPostalAddress().getStreet());
                        organizationModel.setZipCode(x.getPostalAddress().getZipCode());
                        organizationModel.setUserID(x.getUserID());
                        organizationModel.setUsername(x.getUsername());

                        organizationModels.add(organizationModel);
                    });

                    filteredData = new FilteredList<>(organizationModels, p -> true);
                    SortedList<TableOrganizationModel> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(table_organization.comparatorProperty());

                    table_organization.setItems(sortedData);

                    notifySearchTextField();

                    if (sortcolumn != null) {
                        table_organization.getSortOrder().add(sortcolumn);
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
        tv_date.setCellFactory(column -> new TableCell<TableOrganizationModel, LocalDate>() {
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

        RadioMenuItem annualTurnover_ci = new RadioMenuItem();
        annualTurnover_ci.textProperty().bind(I18N.createStringBinding("key.table_annTurn"));
        annualTurnover_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterAnnualTurnoverIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem fullName_ci = new RadioMenuItem();
        fullName_ci.textProperty().bind(I18N.createStringBinding("key.table_fName"));
        fullName_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterFullNameIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem employeesCount_ci = new RadioMenuItem();
        employeesCount_ci.textProperty().bind(I18N.createStringBinding("key.table_empC"));
        employeesCount_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterEmployeesCountisSelect = newValue; notifySearchTextField(); });

        RadioMenuItem type_ci = new RadioMenuItem();
        type_ci.textProperty().bind(I18N.createStringBinding("key.table_type"));
        type_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterTypeIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem street_ci = new RadioMenuItem();
        street_ci.textProperty().bind(I18N.createStringBinding("key.table_street"));
        street_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterStreetIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem zipCode_ci = new RadioMenuItem();
        zipCode_ci.textProperty().bind(I18N.createStringBinding("key.table_governor"));
        zipCode_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterZipCodeIsSelect = newValue; notifySearchTextField(); });

        RadioMenuItem username_ci = new RadioMenuItem();
        username_ci.textProperty().bind(I18N.createStringBinding("key.table_username"));
        username_ci.selectedProperty().addListener((observable, oldValue, newValue) -> { filterUsernameIsSelect = newValue; notifySearchTextField(); });

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(id_ci);
        toggleGroup.getToggles().add(name_ci);
        toggleGroup.getToggles().add(x_ci);
        toggleGroup.getToggles().add(y_ci);
        toggleGroup.getToggles().add(date_ci);
        toggleGroup.getToggles().add(annualTurnover_ci);
        toggleGroup.getToggles().add(fullName_ci);
        toggleGroup.getToggles().add(employeesCount_ci);
        toggleGroup.getToggles().add(type_ci);
        toggleGroup.getToggles().add(street_ci);
        toggleGroup.getToggles().add(zipCode_ci);
        toggleGroup.getToggles().add(username_ci);

        menubtn_filter.getItems().add(id_ci);
        menubtn_filter.getItems().add(name_ci);
        menubtn_filter.getItems().add(x_ci);
        menubtn_filter.getItems().add(y_ci);
        menubtn_filter.getItems().add(date_ci);
        menubtn_filter.getItems().add(annualTurnover_ci);
        menubtn_filter.getItems().add(fullName_ci);
        menubtn_filter.getItems().add(employeesCount_ci);
        menubtn_filter.getItems().add(type_ci);
        menubtn_filter.getItems().add(street_ci);
        menubtn_filter.getItems().add(zipCode_ci);
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
