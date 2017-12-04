package controller;

import domain.Candidat;
import domain.Sectie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
import validators.OptiuneValidator;
import validators.SectieValidator;
import view.MessageAlert;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SectieController implements Observer<Sectie> {
    @FXML
    private TableColumn columnNume;
    @FXML
    private TableColumn columnNrLocuri;
    @FXML
    private TableColumn columnCandidatNume;
    @FXML
    private TableColumn columnCandidatTelefon;
    @FXML
    private TableColumn columnCandidatSex;
    @FXML
    private TableColumn columnCandidatMail;
    @FXML
    private TableView tableViewSectii;
    @FXML
    private TableView tableViewCandidati;
    @FXML
    private PieChart chart;
    @FXML
    private TextField filter;
    @FXML
    private Label labelAplicanti;
    @FXML
    private Slider slider;

    private ObservableList<Sectie> model;
    private SectieService sectieService;
    private CandidatService candidatService;
    private OptiuneService service;
    private Stage previousStage;
    private Stage view;

    public void setView(Stage view) {
        this.view = view;
    }

    public void setModel(ObservableList<Sectie> model) {
        this.model = model;
    }


    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public SectieController() {

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
        sectieService.addObserver(this);
    }

    @FXML
    public void initialize() {
        try {
            model = FXCollections.observableArrayList(sectieService.getAll());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        columnNume.setCellValueFactory(new PropertyValueFactory<Sectie, String>("nume"));
        columnNrLocuri.setCellValueFactory(new PropertyValueFactory<Candidat, Integer>("numarLocuri"));
        columnCandidatMail.setCellValueFactory(new PropertyValueFactory<Candidat, String>("mail"));
        columnCandidatNume.setCellValueFactory(new PropertyValueFactory<Candidat, String>("nume"));
        columnCandidatSex.setCellValueFactory(new PropertyValueFactory<Candidat, String>("sex"));
        columnCandidatTelefon.setCellValueFactory(new PropertyValueFactory<Candidat, String>("telefon"));
        tableViewSectii.setItems(model);
    }
    @FXML
    public void handleFilterLookUp(KeyEvent keyEvent) {
        try {
            if (!filter.getText().equals("")) {
                ObservableList<Sectie> model = FXCollections.observableArrayList(sectieService.filterByNume(filter.getText()));
                tableViewSectii.setItems(model);
            } else
                tableViewSectii.setItems(this.model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @FXML
    public void handleButtonSterge(MouseEvent ev) {
        Sectie sectie = (Sectie) tableViewSectii.getSelectionModel().getSelectedItem();
        if (sectie == null)
            MessageAlert.showErrorMessage(null, "Trebuie sa selectezi ceva");
        else {
            try {
                sectieService.delete(sectie);
                tableViewCandidati.getItems().clear();
                service.maintenance();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Stergere", "Stergerea s-a efectuat cu succes");

            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }

    private void showAddView(Sectie sectie) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddSectieView.fxml"));
            Pane root = (Pane) loader.load();
            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.setTitle("Adaugare Sectie");
            stage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            AddSectieController addSectieController = loader.getController();
            addSectieController.setService(sectieService, stage, sectie);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleButtonModifica(MouseEvent actionEvent) {
        showAddView((Sectie) tableViewSectii.getSelectionModel().getSelectedItem());
        service.maintenance();
    }

    @FXML
    public void handleButtonAdauga(MouseEvent actionEvent) {
        showAddView(null);
    }

    //observer
    @Override
    public void notifyEvent(ListEvent<Sectie> e) {
        model.setAll(StreamSupport.stream(e.getList().spliterator(), false)
                .collect(Collectors.toList()));  // important
    }

    public ObservableList<Sectie> getModel() {
        return model;
    }

    @FXML
    public void handleButtonBack(MouseEvent actionEvent) {
        view.close();
        previousStage.show();
    }

    @FXML
    public void showCandidates(MouseEvent event) throws Exception {
        Sectie sectie = (Sectie) tableViewSectii.getSelectionModel().getSelectedItem();
        if (sectie != null) {
            ObservableList<Candidat> model = FXCollections.observableArrayList(service.filterCandidatBySectie(sectie.getId()));
            tableViewCandidati.setItems(model);

            ObservableList<PieChart.Data> pieChartData;
            long aplicanti = sectie.getAplicanti();
            int nrLocuri = sectie.getNumarLocuri();
            if (aplicanti > nrLocuri)
                pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("Locuri ocupate", nrLocuri),
                        new PieChart.Data("Locuri libere", 0));
            else
                pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("Locuri ocupate", aplicanti),
                        new PieChart.Data("Locuri libere", nrLocuri - aplicanti));
            chart.setData(pieChartData);
        }
    }
    @FXML
    public void handleFilterAplicanti(MouseEvent event) throws Exception {
        int aplicanti=(int)slider.getValue();
        labelAplicanti.setText(Integer.toString(aplicanti));
        ObservableList<Sectie> model=FXCollections.observableArrayList(sectieService.filterByAplicanti(aplicanti));
        tableViewSectii.setItems(model);
        tableViewCandidati.getItems().clear();
        chart.getData().clear();
    }
}
