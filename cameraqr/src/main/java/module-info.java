/**
 *
 */
module app.supernaut.fx.webcam.cameraqr {
    requires java.desktop;

    requires javafx.controls;
    requires javafx.swing;

    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires webcam.capture;
    requires org.slf4j;

    requires app.supernaut.fx.webcam.camera;

    exports app.supernaut.fx.webcam.cameraqr;
}
