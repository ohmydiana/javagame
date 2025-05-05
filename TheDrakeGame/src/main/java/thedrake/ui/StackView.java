package thedrake.ui;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.*;

import java.util.List;


public class StackView extends Pane {

    private final TileViewContext context;
    private final PlayingSide side;
    private final Border selectionBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));
    private final TileBackgrounds backgrounds = new TileBackgrounds();
    public StackView(TileViewContext context, PlayingSide side) {
        this.context = context;
        this.side = side;
        setPrefSize(200, 200);
        setOnMouseClicked(e -> onClick());
        update();
    }

    public void select() {
        setBorder(selectionBorder);
        context.stackViewSelected(this);
    }

    private void onClick() {
        select();
    }

    public void unselect() {
        setBorder(null);
    }

    public PlayingSide side() {
        return side;
    }

    public void update() {
        List<Troop> stack = context.getState().army(side).stack();
        if (!stack.isEmpty()) {
            Tile tile = new TroopTile(stack.get(0), side, TroopFace.AVERS);
            setBackground(backgrounds.get(tile));
        } else {
            setBackground(null);
        }
    }
}
