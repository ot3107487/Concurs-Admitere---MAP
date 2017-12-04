package controller;

import domain.Sectie;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.SectieService;
import view.MessageAlert;

public class AddSectieController {
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNume;
    @FXML
    private TextField txtNrLocuri;

    private SectieService service;
    private Stage stage;
    private Sectie sectie;

    public void setService(SectieService service, Stage stage, Sectie sectie) { //candidat candidat poate pt update daca nu iese
        this.service = service;
        this.stage = stage;
        this.sectie=sectie;
        if (null != sectie) {
            setFields(sectie);
        }
    }

    @FXML
    public void handleSave() {
        try {
            long id = Long.parseLong(txtId.getText());

            String nume = txtNume.getText();
            int nrLocuri = Integer.parseInt(txtNrLocuri.getText());
            Sectie sectie = new Sectie(id, nume, nrLocuri);
            if (null == this.sectie) {
                saveCandidat(sectie);
            } else {
                updateCandidat(sectie);
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    private void updateCandidat(Sectie sectie) {
        try {
            this.service.put(sectie);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare sectie", "Sectia a fost modificata");
            stage.close();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void setFields(Sectie sectie) {
        txtId.setDisable(true);
        txtId.setText(sectie.getId().toString());
        txtNume.setText(sectie.getNume());
        txtNrLocuri.setText(Integer.toString(sectie.getNumarLocuri()));
    }

    private void saveCandidat(Sectie sectie) {
        try {
            this.service.save(sectie);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Adaugare sectie", "Sectia a fost adaugata");
            stage.close();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

}
