/*
 * Copyright 2019-2022 M. Sean Gilligan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.supernaut.fx.webcam.app.webcam;

import app.supernaut.camera.sarxos.CameraService;
import app.supernaut.camera.sarxos.CameraView;
import com.github.sarxos.webcam.Webcam;
//import com.github.sarxos.webcam.ds.openimaj.OpenImajDriver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * Demonstration QR code scanner app with no bitcoinj/crypto dependencies
 */
public class DemoWebcamApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(DemoWebcamApplication.class);
    private static final long GET_WEBCAMS_TIMEOUT = 10000;

    private CameraService cameraQRService;
    private Stage primaryStage;

//    static {
//        Webcam.setDriver(new OpenImajDriver());
//    }

    @Override
    public void init() {
        // note this is in init as it **must not** be called on the FX Application Thread:
//        Webcam camera = Webcam.getWebcams().get(0);

        Webcam camera = null;
        try {
            camera = Webcam.getWebcams(GET_WEBCAMS_TIMEOUT).get(0);
        } catch (TimeoutException toex) {
            log.warn("No Webcam found within {} ms", GET_WEBCAMS_TIMEOUT);
        } catch (Exception e) {
            log.error("Exception getting Webcam", e);
        }

//        camera = findWebcam();
        cameraQRService = new CameraService(camera);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        CameraView captureView  = new CameraView(cameraQRService);
        BorderPane pane = new BorderPane();
        pane.setCenter(captureView);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void scanListener(String result) {
        System.out.println("Result: " + result);
    }

    private void closeListener(Object result) {
        if (primaryStage != null) {
            primaryStage.hide();
        }
    }

    private Webcam findWebcam() {
//        Webcam selectedWebcam = null;
//        for ( Webcam webcam : Webcam.getWebcams() ) {
//
//            if (webcam.getName().contains("FaceTime")) {
//
//                selectedWebcam = webcam;
//            }
//
//            log.info( "webcam: {}", webcam);
//        }
        //Webcam.setDriver(new OpenImajDriver());
        Webcam selectedWebcam = Webcam.getDefault();
        log.info("Returning selected webcam: {}", selectedWebcam);
        return selectedWebcam;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
