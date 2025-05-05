package thedrake.ui;

import javafx.scene.image.Image;
import thedrake.PlayingSide;
import thedrake.TroopFace;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

public class TroopImageSet {
    private final Map<TroopFace, Image> blueTroops;
    private final Map<TroopFace, Image> orangeTroops;

    public TroopImageSet(String troopName) {
        blueTroops = new HashMap<>();
        orangeTroops = new HashMap<>();

        blueTroops.put(TroopFace.AVERS, loadImage("front" + troopName + "B"));
        blueTroops.put(TroopFace.REVERS, loadImage("back" + troopName + "B"));

        orangeTroops.put(TroopFace.AVERS, loadImage("front" + troopName + "O"));
        orangeTroops.put(TroopFace.REVERS, loadImage("back" + troopName + "O"));
    }

    private Image loadImage(String fileName) {
        InputStream imageStream = getClass().getResourceAsStream("/assets/" + fileName + ".png");
        if (imageStream == null) {
            throw new IllegalArgumentException("Image not found: " + fileName);
        }
        return new Image(imageStream);
    }

    public Image get(PlayingSide side, TroopFace face) {
        return switch (side) {
            case BLUE -> blueTroops.get(face);
            case ORANGE -> orangeTroops.get(face);
        };
    }
}