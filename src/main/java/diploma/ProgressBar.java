package diploma;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ProgressBar {

    private Stage progressBar;

    ProgressBar() {
        progressBar = new Stage();
        progressBar.initModality(Modality.APPLICATION_MODAL);
    }

    Stage create() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/progress.fxml"));
            Scene scene = new Scene(parent);
            progressBar.setScene(scene);
            progressBar.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return progressBar;
    }

    public Stage getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(Stage progressBar) {
        this.progressBar = progressBar;
    }
}
