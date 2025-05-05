package thedrake;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button endButton;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private StackPane rootPane;

    @FXML
    private void onEndButtonClick(ActionEvent event) {
        Stage stage = (Stage) endButton.getScene().getWindow();
        stage.close();
    }

    public void onPlayerVsPlayerClicked(ActionEvent actionEvent) {
        GameResult.setResult(GameResult.IN_PLAY);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image backgroundImage = new Image("/img/thedrake_pic.jpeg");
        backgroundImageView.setImage(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
    }

}
