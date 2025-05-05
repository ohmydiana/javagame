package thedrake;

import java.io.PrintWriter;
import java.util.Arrays;

public class Board implements JSONSerializable {
    int dimension;
    private final BoardTile [][] tiles;
    private final PositionFactory positionFactory;

    // Constructor. Creates square game board of given size (dimension = width = height), with all places empty (containing BoardTile.EMPTY)
    public Board(int dimension) {
        this.dimension = dimension;
        this.tiles = new BoardTile[dimension][dimension];
        this.positionFactory = new PositionFactory(dimension);

        for (int i = 0; i < dimension; i++) {
            Arrays.fill(this.tiles[i], BoardTile.EMPTY);
        }
    }

    private Board (int dimension, BoardTile[][] tiles) {
        this.dimension = dimension;
        this.tiles = tiles;
        this.positionFactory = new PositionFactory(dimension);
    }

    // Size of the board
    public int dimension() {
        return this.dimension;
    }

    // Returns a tile on a provided position
    public BoardTile at(TilePos pos) {
        if (pos == TilePos.OFF_BOARD) {
            throw new IllegalArgumentException("Position is off board");
        }
        return tiles [pos.i()][pos.j()];
    }

    // Creates new board with new tiles provided by the ats parameter. All the other tiles stay the same
    public Board withTiles(TileAt... ats) {
        BoardTile [][]  newTiles = new BoardTile[this.dimension][this.dimension];

        for (int i = 0; i < this.dimension; i++) {
            newTiles[i] = this.tiles[i].clone();
        }

        for (TileAt at : ats) {
            newTiles [at.pos.i()][at.pos.j()] = at.tile;
        }

        return new Board(this.dimension, newTiles);
    }

    // Creates an instance of PositionFactory class for simpler creation of new position objects for this board
    public PositionFactory positionFactory() {
        return this.positionFactory;
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{");

        writer.print("\"dimension\":" + dimension);
        writer.print(",\"tiles\":[");

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                tiles[j][i].toJSON(writer);
                if (j < dimension - 1 || i < dimension - 1) {
                    writer.print(",");
                }
            }
        }
        writer.print("]");
        writer.print("}");
    }
}

