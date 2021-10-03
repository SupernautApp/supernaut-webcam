/**
 *
 */
module app.supernaut.fx.webcam.app.webcam {
    requires java.desktop;

    requires javafx.controls;
    requires javafx.swing;

    requires webcam.capture;
    requires org.slf4j;

    requires app.supernaut.fx.webcam.camera;

    exports app.supernaut.fx.webcam.app.webcam;
}
