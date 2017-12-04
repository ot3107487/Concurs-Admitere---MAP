package controller;

import domain.Candidat;
import domain.Optiune;
import domain.Sectie;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import repository.SectieRepository;
import service.OptiuneService;
import service.SectieService;
import validators.SectieValidator;

import javafx.event.ActionEvent;
import view.MessageAlert;

public class AddOptiuneController {
    @FXML
    private ListView listView;

    private Candidat candidat;
    private OptiuneService optiuneService;
    private Stage stage;

    public OptiuneService getOptiuneService() {
        return optiuneService;
    }

    private SectieService service;

    public void setOptiuneService(OptiuneService optiuneService,Stage stage) {
        this.optiuneService = optiuneService;
        this.stage=stage;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public AddOptiuneController() {
        SectieRepository sectieRepository = new SectieRepository(new SectieValidator(), ".\\src\\sectii.txt");
        sectieRepository.readFromFile();
        this.service = new SectieService(sectieRepository);
    }

    @FXML
    public void initialize() {
        try {
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listView.getItems().addAll(service.getAll());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void handleButtonSave() {
        listView.getSelectionModel().getSelectedItems().forEach(sectie -> {
                        try {
                            optiuneService.save(new Optiune(candidat, (Sectie) sectie));
                        } catch (Exception e) {
                            MessageAlert.showErrorMessage(null, e.getMessage());
                        }
                }
        );
        stage.close();
    }
}
