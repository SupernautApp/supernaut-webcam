/**
 *
 */
module app.supernaut.fx.webcam.camera {
    requires java.desktop;

    requires javafx.controls;
    requires javafx.swing;

    requires webcam.capture;
    requires org.slf4j;

    exports app.supernaut.fx.webcam.camera;
}
