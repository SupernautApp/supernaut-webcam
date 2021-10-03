/**
 *
 */
module app.supernaut.fx.webcam.app.qrdisplay {
    requires java.desktop;

    requires javafx.controls;
    requires javafx.swing;

    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires webcam.capture;
    requires org.slf4j;

    requires app.supernaut.fx.webcam.camera;
    requires app.supernaut.fx.webcam.cameraqr;

    exports app.supernaut.fx.webcam.app.qrdisplay;
}
