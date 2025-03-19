package thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {
    public SlideAction(Offset2D offset) {
        super(offset);
    }

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos currentPos = origin;
        TilePos target = currentPos.stepByPlayingSide(offset(), side);

        while (state.canStep(currentPos, target) || state.canCapture(currentPos, target)) {
            if (state.canStep(currentPos, target)) {
                result.add(new StepOnly((BoardPos) currentPos, (BoardPos) target));
            }

            if (state.canCapture(currentPos, target)) {
                result.add(new StepAndCapture((BoardPos) currentPos, (BoardPos) target));
                break;
            }

            currentPos = (BoardPos) target;
            target = currentPos.stepByPlayingSide(offset(), side);
        }

        return result;
    }
}
