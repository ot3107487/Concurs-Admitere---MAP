package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Open menu for candidati
     * @param ev - click
     */
    @FXML
    public void handleButtonCandidati(MouseEvent ev)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/CandidatView.fxml"));
            Pane root = (Pane) loader.load();
            Stage newStage=new Stage();
            newStage.setTitle("Meniu Candidati");
            newStage.initModality(Modality.WINDOW_MODAL);
            Scene scene=new Scene(root);
            newStage.setScene(scene);
            CandidatController candidatController=loader.getController();
            candidatController.setView(newStage);
            candidatController.setPreviousStage(primaryStage);
            primaryStage.close();
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open menu for sectii
     * @param ev - click
     */
    @FXML
    public void handleButtonSectii(MouseEvent ev){
        {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/SectieView.fxml"));
                Pane root = (Pane) loader.load();
                Stage newStage=new Stage();
                newStage.setTitle("Meniu Sectii");
                newStage.initModality(Modality.WINDOW_MODAL);
                Scene scene=new Scene(root);
                newStage.setScene(scene);
                SectieController sectieController=loader.getController();
                sectieController.setView(newStage);
                sectieController.setPreviousStage(primaryStage);
                primaryStage.close();
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
