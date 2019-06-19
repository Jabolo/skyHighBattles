package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Sky_High_Battles extends Application {


    static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage theStage) throws Exception {
        Pane root = new Pane(new ImageView(new Image("kwadraty.jpg")));
        root.setPrefSize(600, 600);

        Parent sceneBuild = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getChildren().add(sceneBuild);
        theStage.setScene(new Scene(root, 600, 600));


        theStage.show();
    }
}
