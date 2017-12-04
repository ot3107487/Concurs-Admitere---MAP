
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application
{



    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartApplication.class.getResource("/view/mainView.fxml"));
        Pane rootLayout = null;
        try {
            rootLayout = (Pane)loader.load();
            //get the Controller class and set the service
            MainController rootController=loader.getController();
            //ser.addObserver(rootController);
            primaryStage.setScene(new Scene(rootLayout, 500, 421));
            primaryStage.setTitle("Concurs admitere");
            rootController.setPrimaryStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        launch(args);
    }
}