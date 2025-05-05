package thedrake.ui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import thedrake.*;

import java.util.Objects;
import java.util.Optional;

public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GameResult.gameView = new GameView(GameResult.newSampleGameState());
        GameResult.gameScene = new Scene(GameResult.gameView, 1200, 900);

        Scene MainMenu = new Scene(FXMLLoader.load(Objects.requireNonNull(TheDrakeApp.class.getResource("/thedrake/main_menu.fxml"))), 800, 500);
        primaryStage.setTitle("The Drake Game");
        primaryStage.setScene(MainMenu);
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (GameResult.stateChanged) {
                    switch (GameResult.state) {
                        case IN_PLAY -> primaryStage.setScene(GameResult.gameScene);
                        case VICTORY -> endingDialog(GameResult.gameView.getWinner(), MainMenu, false, primaryStage);
                        case DRAW -> endingDialog(null, MainMenu, true, primaryStage);
                    }
                    primaryStage.show();
                    GameResult.stateChanged = false;
                }
            }
        }.start();
    }

    private void endingDialog(PlayingSide side, Scene mainMenu, boolean draw, Stage stage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);

            String winnerText;
            if (draw) {
                alert.setTitle("Koniec hry");
                winnerText = "Remíza! Nikto nezvíťazil.";
            } else {
                alert.setTitle("Gratulujeme!");
                winnerText = (side == PlayingSide.BLUE ? "Modrý" : "Oranžový") + " hráč zvíťazil!";
            }

            alert.setContentText(winnerText);

            ButtonType returnToMenu = new ButtonType("Späť do menu");
            ButtonType playAgain = new ButtonType("Hrať znova");

            alert.getButtonTypes().setAll(returnToMenu, playAgain);

            // Štýly dialógu
            DialogPane pane = alert.getDialogPane();
            pane.setStyle(
                    "-fx-background-color: #2e2e2e;" +
                            "-fx-border-color: #444;" +
                            "-fx-border-width: 2;" +
                            "-fx-font-family: 'Arial';" +
                            "-fx-font-size: 14px;"
            );

            // Štýly vnútorného content textu (aby nebol čierny na tmavom pozadí)
            var contentLabel = pane.lookup(".content");
            if (contentLabel != null) {
                contentLabel.setStyle("-fx-text-fill: white;");
            }

            // Štýly tlačidiel
            Button btnMenu = (Button) pane.lookupButton(returnToMenu);
            btnMenu.setStyle(
                    "-fx-background-color: #555;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 8 15 8 15;"
            );

            Button btnAgain = (Button) pane.lookupButton(playAgain);
            btnAgain.setStyle(
                    "-fx-background-color: #336699;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 8 15 8 15;"
            );

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == returnToMenu){
                stage.setScene(mainMenu);
                GameResult.gameView = new GameView(GameResult.newSampleGameState());
                GameResult.gameScene = new Scene(GameResult.gameView, 1200, 900);
            } else if (result.isPresent() && result.get() == playAgain) {
                GameResult.gameView = new GameView(GameResult.newSampleGameState());
                GameResult.gameScene = new Scene(GameResult.gameView, 1200, 900);
                GameResult.setResult(GameResult.IN_PLAY);
            } else {
                System.exit(0);
            }
        });
    }
}
