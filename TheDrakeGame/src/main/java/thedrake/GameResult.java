package thedrake;

import javafx.scene.Scene;
import thedrake.ui.GameView;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable {
    VICTORY, DRAW, IN_PLAY;

    public static GameResult state;
    public static boolean stateChanged;
    public static Scene gameScene;
    public static GameView gameView;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"" + this.name() + "\"");
    }

    public static void setResult (GameResult result) {
        GameResult.state = result;
        GameResult.stateChanged = true;
    }

    public static GameState newSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
        board = board.withTiles(new Board.TileAt(positionFactory.pos(3, 2), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);

    }
}
