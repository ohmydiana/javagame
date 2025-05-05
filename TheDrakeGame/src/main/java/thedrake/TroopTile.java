package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile, JSONSerializable {
    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side() {
        return this.side;
    }

    public TroopFace face() {
        return this.face;
    }

    public Troop troop() {
        return this.troop;
    }

    @Override
    public boolean canStepOn() {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> moves = new ArrayList<>();

        for (TroopAction action : troop.actions(face)) {
            moves.addAll(action.movesFrom(pos, side, state));
        }

        return moves;
    }

    public TroopTile flipped() {
        TroopFace newFace = (this.face == TroopFace.AVERS) ? TroopFace.REVERS : TroopFace.AVERS;
        return new TroopTile(this.troop, this.side, newFace);
    }

    @Override
    public void toJSON (PrintWriter writer) {
        writer.print("{");

        writer.print("\"troop\":\"" + this.troop.name() + "\",\"side\":\"" + this.side +  "\",\"face\":\"" + this.face + "\"");

        writer.print("}");
    }
}
