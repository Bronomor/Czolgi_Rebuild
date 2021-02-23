package Main;

import MapParametersPackage.MapParameters;
import PopUpWindow.ExceptionWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class MenuStart extends Application {

    static final int WINDOW_WIDTH = 600;
    static final int WINDOW_HEIGHT = 600;
    Stage window = new Stage();

    @Override
    public void start(Stage primaryStage){
        try {
            window.setTitle("Menu");
            window.setScene(prepareLayouts());
            window.show();
        }
        catch (IllegalArgumentException e) {
            ExceptionWindow exceptionWindow = new ExceptionWindow();
            exceptionWindow.start(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Scene prepareLayouts(){

        Text describe = new Text(" Poruszanie za pomocą klawiszy: \n\n" +
                "Aby wykonać prawidłowy ruch, zmień orientacje czołgu \n (lewo,prawo,góra,dół) i nacisnij klawisz: " +
                "w - ruch  \n  " + " k - strzelanie " + " \n Obrót bez straty rundy: \n <--  obrót w lewo \n --> obrót w prawo\n\n" +
                "czerwony kwadrat - przeszkoda \n zielony okrąg - losowy bonus\n\n" +
                "Wrogie czołgi pojawiają się z aktywnymi zestawem \nlosowych Power-Upów\n Kradną power-upy z mapy, ale są takie dobre, \nże ich nie uzywają ;) \n"
                );
        describe.setFont(new javafx.scene.text.Font(20));
        describe.setTextAlignment(TextAlignment.CENTER);

        Button startButton = new Button("Start");
        describe.setFont(new javafx.scene.text.Font(20));
        describe.setTextAlignment(TextAlignment.CENTER);

        startButton.setOnAction(actionEvent -> {
            try {
                MapParameters mapParameters = new MapParameters();
                World world = new World(mapParameters);
                world.start();
                window.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

        VBox vbox = new VBox(describe,startButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 50, 30, 50));


        return new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}
