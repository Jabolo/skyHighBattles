package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * start aplikacji
 */
public class Sky_High_Battles extends Application {


    static void main(String[] args) {
        launch(args);
    }


    /**
     * wywołanie menu początkowego
     *
     * @param theStage
     * @throws Exception
     */
    @Override
    public void start(Stage theStage) throws Exception {
        Pane root = new Pane(new ImageView(new Image("kwadraty.jpg")));
        Parent sceneBuild = FXMLLoader.load(getClass().getResource("menuStart.fxml"));
        root.getChildren().add(sceneBuild);
        theStage.setScene(new Scene(root, 600, 600));
        theStage.show();
    }
}
