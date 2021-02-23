package PopUpWindow;

import Elements.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameOverWindow {

    public void start(Player player){
        Stage stage = new Stage();
        Scene scene = new Scene(prepareLayout(player));
        stage.setTitle("Game Over");
        stage.setScene(scene);
        stage.show();
    }

    private VBox prepareLayout(Player player){
        Text dangerText = new Text("Przegrałeś. Twój wynik to: " + player.getScore());
        dangerText.setFont(new Font(20));
        dangerText.setTextAlignment(TextAlignment.CENTER);


        VBox vbox = new VBox(dangerText);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 50, 30, 50));
        return vbox;
    }

}
