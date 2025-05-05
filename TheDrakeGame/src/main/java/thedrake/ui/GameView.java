//package thedrake.ui;
//
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.StackPane;
//import thedrake.GameState;
//import thedrake.PlayingSide;
//
//
//public class GameView extends BorderPane {
//    BoardView boardView;
//    public GameView(GameState gameState) {
//        boardView = new BoardView(gameState);
//        boardView.setPadding(new Insets(10, 10, 10, 10));
//        this.setCenter(boardView);
//
//        StackPane topStack = new StackPane(boardView.getOrangeStack());
//        topStack.setAlignment(Pos.CENTER);
//        topStack.setPadding(new Insets(50, 10, 10, 10));
//        this.setTop(topStack);
//
//        StackPane bottomStack = new StackPane(boardView.getBlueStack());
//        bottomStack.setAlignment(Pos.CENTER);
//        bottomStack.setPadding(new Insets(10, 10, 50, 10));
//        this.setBottom(bottomStack);
//
//
//        CapturedView capturedBlue = boardView.getCapturedBlue();
//        capturedBlue.setMinWidth(100);
//        this.setRight(capturedBlue);
//
//        CapturedView capturedOrange = boardView.getCapturedOrange();
//        capturedOrange.setMinWidth(100);
//        this.setLeft(capturedOrange);
//        setStyle("-fx-background-color: #6b8e23;");
//    }
//
//    public PlayingSide getWinner() {
//        return boardView.getState().armyNotOnTurn().side();
//    }
//}


package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import thedrake.GameState;
import thedrake.PlayingSide;

public class GameView extends BorderPane {
    private final BoardView boardView;

    public GameView(GameState gameState) {
        boardView = new BoardView(gameState);
        initializeView();
        applyStyles();
    }

    private void initializeView() {
        // Nastavenie hracej dosky na ľavú stranu
        boardView.setPadding(new Insets(10, 10, 10, 200));
        this.setLeft(boardView);

        // Nastavenie panelov pre stĺpce s jednotkami (stacks)
        configureStacks();
    }

    private void configureStacks() {
        VBox stacks = new VBox();
        stacks.getChildren().addAll(boardView.getOrangeStack(), boardView.getBlueStack());
        stacks.setAlignment(Pos.CENTER);
        stacks.setSpacing(100);
        stacks.setPadding(new Insets(10, 10, 10, 10));
        this.setRight(stacks);
    }

    private void applyStyles() {
        // Pozadie s vojenskou zelenou
        this.setStyle("-fx-background-color: #6b8e23;");
    }

    public PlayingSide getWinner() {
        return boardView.getState().armyNotOnTurn().side();
    }
}