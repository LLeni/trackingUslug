package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final int WIDTH_SCENE = 800;
    private final int HEIGHT_SCENE = 600;
    private final int MIN_WIDTH_SCENE = 800;
    private final int MIN_HEIGHT_SCENE = 600;
    private final int MAX_WIDTH_SCENE = 800;
    private final int MAX_HEIGHT_SCENE = 600;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/main.fxml"));
        Parent root = loader.load();
        Controller controller  = loader.getController();
        controller.setMainStage(primaryStage);
        primaryStage.setTitle("Услуги");
        primaryStage.setScene(new Scene(root, WIDTH_SCENE, HEIGHT_SCENE));
        primaryStage.setMinWidth(MIN_WIDTH_SCENE);
        primaryStage.setMinHeight(MIN_HEIGHT_SCENE);
        primaryStage.setMaxWidth(MAX_WIDTH_SCENE);
        primaryStage.setMaxHeight(MAX_HEIGHT_SCENE);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
