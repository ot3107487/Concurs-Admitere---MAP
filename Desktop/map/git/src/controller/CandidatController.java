package controller;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import domain.Candidat;
import domain.Optiune;
import domain.Sectie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.CandidatRepository;
import repository.OptiuneRepository;
import repository.SectieRepository;
import service.CandidatService;
import service.OptiuneService;
import service.SectieService;
import utils.ListEvent;
import utils.Observer;
import validators.CandidatValidator;


import javafx.event.ActionEvent;
import validators.OptiuneValidator;
import validators.SectieValidator;
import view.MessageAlert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class CandidatController implements Observer<Candidat> {

    //view components
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn columnNume;
    @FXML
    private TableColumn columnTelefon;
    @FXML
    private TableColumn columnSex;
    @FXML
    private TableColumn columnMail;
    @FXML
    private ListView listView;

    @FXML
    private TextField filter;
    @FXML
    private ComboBox cboFilters;

    private Stage previousStage;
    private Stage view;

    //services
    private OptiuneService service;
    private CandidatService candidatService;
    private SectieService sectieService;
    private ObservableList<Candidat> model;


    //constructor and initializer
    public CandidatController() {

        CandidatRepository candidatRepository = new CandidatRepository(new CandidatValidator(), ".\\src\\candidati.txt");
        candidatRepository.readFromFile();
        candidatService = new CandidatService(candidatRepository);
        SectieRepository sectieRepository = new SectieRepository(new SectieValidator(), ".\\src\\sectii.txt");
        sectieRepository.readFromFile();
        sectieService = new SectieService(sectieRepository);
        OptiuneRepository optiuneRepository = new OptiuneRepository(new OptiuneValidator(),
                ".\\src\\optiuni.txt",
                candidatRepository,
                sectieRepository);
        optiuneRepository.readFromFile();
        service = new OptiuneService(optiuneRepository, sectieRepository, candidatRepository);
        candidatService.addObserver(this);
    }

    @FXML
    public void initialize() {
        try {
            model = FXCollections.observableArrayList(candidatService.getAll());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        columnMail.setCellValueFactory(new PropertyValueFactory<Candidat, String>("mail"));
        columnNume.setCellValueFactory(new PropertyValueFactory<Candidat, String>("nume"));
        columnSex.setCellValueFactory(new PropertyValueFactory<Candidat, String>("sex"));
        columnTelefon.setCellValueFactory(new PropertyValueFactory<Candidat, String>("telefon"));
        tableView.setItems(model);

        cboFilters.getItems().addAll("Nume", "Sex", "Telefon", "Mail", "All");

    }
    //getters and setter

    public Stage getView() {
        return view;
    }

    public void setView(Stage view) {
        this.view = view;
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public OptiuneService getService() {
        return service;
    }

    public void setService(OptiuneService service) {
        this.service = service;
    }

    //chestii cu tabelul (pe cnadidati)

    @FXML
    public void handleButtonAdauga(MouseEvent actionEvent) {
        showAddView(null);
    }

    @FXML
    public void handleButtonSterge(MouseEvent ev) {
        Candidat candidat = (Candidat) tableView.getSelectionModel().getSelectedItem();
        if (candidat == null)
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi ceva");
        else {
            try {
                candidatService.delete(candidat);
                clearListView();
                service.maintenance();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Stergere", "Stergerea s-a efectuat cu succes");

            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }

    @FXML
    public void handleButtonModifica(MouseEvent actionEvent) {
        showAddView((Candidat) tableView.getSelectionModel().getSelectedItem());
        service.maintenance();
    }

    @FXML
    public void handleFilterLookUp(KeyEvent keyEvent) {
        try {
            if (!filter.getText().equals("")) {
                ObservableList<Candidat> model = FXCollections.observableArrayList(filterChoise());
                tableView.setItems(model);
            } else
                tableView.setItems(this.model);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }
    //revenire fereastra anterioara

    @FXML
    public void handleButtonBack(MouseEvent actionEvent) {
        view.close();
        previousStage.show();
    }
    //chestii cu listview (pe optiuni

    @FXML
    public void handleStergeOptiune(MouseEvent event) {
        listView.getSelectionModel().getSelectedItems().forEach(optiune -> {
            if (optiune != null)
                try {
                    if (service.delete((Optiune) optiune)) {
                        listView.getItems().remove(optiune);
                    } else
                        MessageAlert.showErrorMessage(null, "Optiunea nu a fost stearsa");
                } catch (Exception e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
        });
        showOptions(event);
    }

    @FXML
    public void handleAddOptiune(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddOptiuneView.fxml"));
            Pane root = (Pane) loader.load();
            Candidat candidat = (Candidat) tableView.getSelectionModel().getSelectedItem();
            Stage stage = new Stage();
            stage.setTitle("Adaugare Optiune");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            AddOptiuneController addOptiuneController = loader.getController();
            addOptiuneController.setOptiuneService(service, stage);
            addOptiuneController.setCandidat(candidat);
            stage.show();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
    //utils table ( candidat )

    private void showAddView(Candidat candidat) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddCandidatView.fxml"));
            Pane root = (Pane) loader.load();
            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.setTitle("Adaugare Candidat");
            stage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            AddCandidatController candidatController = loader.getController();
            candidatController.setService(candidatService, stage, candidat);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Candidat> filterChoise() throws Exception {
        Object choise = cboFilters.getSelectionModel().getSelectedItem();
        if (choise!=null)
            switch ((String)choise) {
                case "Nume":
                    return candidatService.filterByNume(filter.getText());
                case "Sex":
                    return candidatService.filterBySex(filter.getText());
                case "Telefon":
                    return candidatService.filterByPhone(filter.getText());
                case "Mail":
                    return candidatService.filterByMail(filter.getText());
                case "All":
                    return candidatService.filterByAll(filter.getText());
            }
        return candidatService.filterByAll(filter.getText());
    }

    //utils listview ( optiune )

    public void clearListView() {
        listView.getItems().clear();
    }

    @FXML
    public void showOptions(MouseEvent event) {
        Candidat candidat = (Candidat) tableView.getSelectionModel().getSelectedItem();
        if (candidat != null) {
            try {
                ArrayList<Sectie> list = (ArrayList) service.filterSectiiByCandidat(candidat.getId());
                listView.getItems().setAll(list);
                listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }

    //partea de observer

    @Override
    public void notifyEvent(ListEvent<Candidat> e) {
        model.setAll(StreamSupport.stream(e.getList().spliterator(), false)
                .collect(Collectors.toList()));  // important
    }

    public ObservableList<Candidat> getModel() {
        return model;
    }
}
