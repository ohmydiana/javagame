package thedrake;

import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile {
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

        List<TroopAction> actions = troop.actions(face);

        for (TroopAction action : actions) {
            moves.addAll(action.movesFrom(pos, side, state));
        }

        return moves;
    }

    public TroopTile flipped() {
        TroopFace newFace = (this.face == TroopFace.AVERS) ? TroopFace.REVERS : TroopFace.AVERS;
        return new TroopTile(this.troop, this.side, newFace);
    }
}
