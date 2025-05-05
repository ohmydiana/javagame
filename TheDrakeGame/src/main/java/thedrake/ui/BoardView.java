package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import thedrake.*;

import java.util.List;

public class BoardView extends VBox implements TileViewContext {

    private GameState gameState;
    private ValidMoves validMoves;
    private TileView tileViewSelected;
    private StackView stackViewSelected;
    private StackView blueStack, orangeStack;
    private CapturedView capturedBlue, capturedOrange;
    private final Label sideOnTurnLabel;
    private final GridPane boardGrid;

    public BoardView(GameState gameState) {
        this.gameState = gameState;
        validMoves = new ValidMoves(gameState);

        this.boardGrid = new GridPane();
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);
        boardGrid.setPadding(new Insets(15, 100, 15, 100));
        boardGrid.setAlignment(Pos.CENTER);


        PositionFactory positionFactory = gameState.board().positionFactory();
        for (int y = 0; y < positionFactory.dimension(); y++){
            for (int x = 0; x < positionFactory.dimension(); x++){
                BoardPos boardPos = positionFactory.pos(x,positionFactory.dimension() - y - 1);
                boardGrid.add(new TileView(gameState.tileAt(boardPos), boardPos, this), x, y);
            }
        }


        sideOnTurnLabel = new Label();
        sideOnTurnLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        sideOnTurnLabel.setAlignment(Pos.CENTER);
        sideOnTurnLabel.setPadding(new Insets(0, 15, 0, 15));

        blueStack = createStackView(PlayingSide.BLUE);
        orangeStack = createStackView(PlayingSide.ORANGE);
        capturedBlue = createCapturedView(PlayingSide.BLUE);
        capturedOrange = createCapturedView(PlayingSide.ORANGE);

        VBox topContent = new VBox(10);
        topContent.setAlignment(Pos.CENTER);
        topContent.getChildren().addAll(orangeStack, capturedOrange);

        VBox bottomContent = new VBox(10);
        bottomContent.setAlignment(Pos.CENTER);
        bottomContent.getChildren().addAll(capturedBlue, blueStack);

        VBox rightContent = new VBox();
        rightContent.setAlignment(Pos.CENTER);
        rightContent.getChildren().add(sideOnTurnLabel);

        HBox mainContent = new HBox(15);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().addAll(boardGrid, rightContent);

        setSpacing(10);
        setAlignment(Pos.CENTER);
        getChildren().addAll(
                topContent,
                mainContent,
                bottomContent
        );

        updateSideOnTurn();
    }

    private void updateTiles() {
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof TileView tileView) {
                tileView.setTile(gameState.tileAt(tileView.position()));
                tileView.update();
            }
        }
        blueStack.update();
        orangeStack.update();
        updateSideOnTurn();
    }

    private StackView createStackView(PlayingSide side) {
        StackView stackView = new StackView(this, side);
        stackView.setMaxSize(100, 100);
        return stackView;
    }

    private CapturedView createCapturedView(PlayingSide side) {
        CapturedView capturedView = new CapturedView();
        capturedView.SetSide(side);
        return capturedView;
    }


    private void updateSideOnTurn() {
        sideOnTurnLabel.setText("Na rade: " + gameState.sideOnTurn());
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (tileView != tileViewSelected) {
            unselectPrevious();
            tileViewSelected = tileView;
            stackViewSelected = null;
            clearMoves();
            showMoves(validMoves.boardMoves(tileView.position()));
        }
    }

    @Override
    public void stackViewSelected(StackView stackView) {
        if (stackView != stackViewSelected) {
            unselectPrevious();
            stackViewSelected = stackView;
            tileViewSelected = null;
            clearMoves();
            if (gameState.sideOnTurn() == stackView.side()) {
                showMoves(validMoves.movesFromStack());
            }
        }
    }

    private void unselectPrevious() {
        if (tileViewSelected != null) {
            tileViewSelected.unselect();
            tileViewSelected = null;
        }
        if (stackViewSelected != null) {
            stackViewSelected.unselect();
            stackViewSelected = null;
        }
    }

    private void updateBoardState() {
        updateTiles();
        capturedBlue.stateChanged(gameState);
        capturedOrange.stateChanged(gameState);
        GameResult.setResult(gameState.result());
    }

    @Override
    public void executeMove(Move move) {
        unselectPrevious();
        tileViewSelected = null;
        stackViewSelected = null;
        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateBoardState();
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    private void showMoves(List<Move> moves){
        for (Move move : moves) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    private void clearMoves() {
        for (Node node : boardGrid.getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private TileView tileViewAt(BoardPos target){
        int index = (gameState.board().dimension() - 1 - target.j()) * gameState.board().dimension() + target.i();
        return (TileView) boardGrid.getChildren().get(index);
    }

    public StackView getBlueStack() { return blueStack; }
    public StackView getOrangeStack() { return orangeStack; }
//    public CapturedView getCapturedBlue() { return capturedBlue; }
//    public CapturedView getCapturedOrange() { return capturedOrange; }

}
