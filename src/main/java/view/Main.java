package view;

import controller.ProgramController;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        ProgramController.stage = primaryStage;
        Scene scene = ProgramController.createNewScene(getClass().getResource("/FXMLs/welcome.fxml"));
        FadeTransition textTransition = new FadeTransition(Duration.seconds(0.75), scene.lookup("#pressKey"));
        textTransition.setAutoReverse(true);
        textTransition.setFromValue(0);
        textTransition.setToValue(1);
        textTransition.setCycleCount(Transition.INDEFINITE);
        textTransition.play();
        ProgramController.stage.setTitle("Yu-Gi-Oh!");
        ProgramController.stage.show();
        scene.setOnKeyPressed(keyEvent -> {
            ProgramController programController = new ProgramController();
            try {
                programController.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
