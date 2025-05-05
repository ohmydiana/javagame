package thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        if (!(pos instanceof  BoardPos)) {
            return Optional.empty();
        }

        return Optional.ofNullable(troopMap.get(pos));
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (troopMap.containsKey(target)) {
            throw new IllegalArgumentException("Target position occupied");
        }

        Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
        newMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));

        TilePos newLeaderPosition = leaderPosition;
        int newGuards = guards;

        if (!isLeaderPlaced()) {
            newLeaderPosition = target;
        } else if (isPlacingGuards()) {
            newGuards++;
        }

        return new BoardTroops(playingSide, newMap, newLeaderPosition, newGuards);
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced() || isPlacingGuards()) {
            throw new IllegalStateException("Cannot move troops before leader and guards are placed");
        }

        if (!troopMap.containsKey(origin) || troopMap.containsKey(target)) {
            throw new IllegalArgumentException("Origin empty or target occupied");
        }

        Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
        TroopTile movedTile = newMap.remove(origin).flipped();
        newMap.put(target, movedTile);

        TilePos newLeaderPosition = leaderPosition.equals(origin) ? target : leaderPosition;

        return new BoardTroops(playingSide, newMap, newLeaderPosition, guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide, newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced() || isPlacingGuards()) {
            throw new IllegalStateException("Cannot move troops before leader and guards are placed");
        }

        if (!troopMap.containsKey(target)) {
            throw new IllegalArgumentException("No troop in target position to remove");
        }

        Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
        newMap.remove(target);

        TilePos newLeaderPosition = leaderPosition.equals(target) ? TilePos.OFF_BOARD : leaderPosition;

        return new BoardTroops(playingSide, newMap, newLeaderPosition, guards);
    }

    @Override
    public void toJSON (PrintWriter writer) {
        writer.print("{");

        writer.print("\"side\":\"" + this.playingSide + "\",");
        writer.print("\"leaderPosition\":");
        this.leaderPosition.toJSON(writer);
        writer.print(",\"guards\":" + this.guards + ",");

        writer.print("\"troopMap\":");
        writer.print("{");

        Set <BoardPos> positions = new TreeSet<>(Comparator.comparing(BoardPos::toString));
        positions.addAll(troopMap.keySet());
        boolean isInside = false;
        for (BoardPos pos : positions) {
            TroopTile tile = troopMap.get(pos);

            if (tile != null) {
                if (!isInside) {
                    pos.toJSON(writer);
                    isInside = true;
                }
                else {
                    writer.print(",");
                    pos.toJSON(writer);
                }
                writer.print(":");
                tile.toJSON(writer);
            }
        }
        writer.print("}");
        writer.print("}");
    }
}
