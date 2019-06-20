package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * ta klasa nie jest w uzyciu, zawiera niedopracowana scene po zakonczeniu rozgrywki
 */
public class AfterGame implements Initializable {

    public void changeScreenToMainMenu(ActionEvent event) throws IOException {
        Parent x = FXMLLoader.load(getClass().getResource("Controller.fxml"));
        Scene scene = new Scene(x);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
