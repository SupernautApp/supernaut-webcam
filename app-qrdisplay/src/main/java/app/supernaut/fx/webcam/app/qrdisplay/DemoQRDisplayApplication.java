package app.supernaut.fx.webcam.app.qrdisplay;

import app.supernaut.fx.qr.QRCodeImages;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 */
public class DemoQRDisplayApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(DemoQRDisplayApplication.class);

    @Override
    public void init() {
        // note this is in init as it **must not** be called on the FX Application Thread:
    }

    @Override
    public void start(Stage primaryStage) {
        Image qrImage = QRCodeImages.imageFromString("hello world", 320, 240);
        ImageView view = new ImageView(qrImage);

        Pane pane = new Pane(view);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
