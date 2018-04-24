package diploma;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Terms Extraction");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
//
//        MenuBar menuBar = new MenuBar();
//        Menu menuFile = new Menu("Add file");
//        Menu menuHelp = new Menu("Help");
//        menuBar.getMenus().addAll(menuFile, menuHelp);
//
//        menuHelp.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                Alert alert = new Alert(Alert.AlertType.NONE);
//                alert.setTitle("Help");
//                alert.setHeaderText(null);
//                alert.setContentText("I have a great message for you!");
//                alert.showAndWait();
//            }
//        });
//
//        menuFile.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.setTitle("Open Resource File");
//                fileChooser.showOpenDialog(primaryStage);
//            }
//        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
