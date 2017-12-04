package controller;

import domain.Candidat;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.CandidatService;
import view.MessageAlert;

import java.awt.*;
import java.time.LocalDateTime;

public class AddCandidatController {
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNume;
    @FXML
    private TextField txtTelefon;
    @FXML
    private TextField txtSex;
    @FXML
    private TextField txtMail;

    private CandidatService service;
    private Stage stage;
    private Candidat candidat;

    public void setService(CandidatService service, Stage stage, Candidat candidat) { //candidat candidat poate pt update daca nu iese
        this.service = service;
        this.stage = stage;
        this.candidat = candidat;
        if (null != candidat) {
            setFields(candidat);
        }
    }

    @FXML
    public void handleSave() {
        try {
            long id = Long.parseLong(txtId.getText());

            String nume = txtNume.getText();
            String telefon = txtTelefon.getText();
            String sex = txtSex.getText();
            String mail = txtMail.getText();
            Candidat candidat = new Candidat(id, nume, telefon, mail, sex);
            if (null == this.candidat) {
                saveCandidat(candidat);
            } else {
                updateCandidat(candidat);
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    private void updateCandidat(Candidat candidat) {
        try {
            this.service.put(candidat);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare candidat", "Candidatul a fost modificat");
            stage.close();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void setFields(Candidat candidat) {
        txtId.setDisable(true);
        txtId.setText(candidat.getId().toString());
        txtSex.setText(candidat.getSex());
        txtMail.setText(candidat.getMail());
        txtTelefon.setText(candidat.getTelefon());
        txtNume.setText(candidat.getNume());
    }

    private void saveCandidat(Candidat candidat) {
        try {
            this.service.save(candidat);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Adaugare candidat", "Candidatul a fost adaugat");
            stage.close();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }


}
