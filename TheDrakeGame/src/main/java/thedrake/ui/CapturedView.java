package thedrake.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import thedrake.*;

import java.util.List;

public class CapturedView extends VBox {
    private PlayingSide side;
    private final Label titleLabel;
    private final Label capturedUnitsLabel;

    public CapturedView() {
        this.titleLabel = new Label();
        this.capturedUnitsLabel = new Label();

        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        capturedUnitsLabel.setStyle("-fx-font-size: 12px;");

        setAlignment(Pos.CENTER);
        setSpacing(5);
        getChildren().addAll(titleLabel, capturedUnitsLabel);
    }

    public void SetSide(PlayingSide side) {
        this.side = side;
        titleLabel.setText("Zajatí vojaci hráča " + side.name());
    }

    public void stateChanged(GameState gameState) {
        List<Troop> captured;
        if (side == PlayingSide.BLUE) {
            captured = gameState.capturedTroops(PlayingSide.BLUE);
        } else {
            captured = gameState.capturedTroops(PlayingSide.ORANGE);
        }

        if (captured.isEmpty()) {
            capturedUnitsLabel.setText("(Žiadni zajatí vojaci)");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Troop t : captured) {
                sb.append(t.name()).append("\n");
            }
            capturedUnitsLabel.setText(sb.toString());
        }
    }
}
