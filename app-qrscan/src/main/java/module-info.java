/**
 *
 */
module app.supernaut.fx.webcam.app.qrscan {
    requires javafx.controls;

    requires com.google.zxing;

    requires webcam.capture;
    requires org.slf4j;

    requires app.supernaut.fx.webcam.camera;
    requires app.supernaut.fx.webcam.cameraqr;

    exports app.supernaut.fx.webcam.app.qrscan;
}
