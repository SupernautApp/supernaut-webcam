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
package app.supernaut.fx.webcam.app.qrscan;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * Demonstration QR code scanner app with no bitcoinj/crypto dependencies
 */
public class DemoQRScannerApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(DemoQRScannerApplication.class);
    private static final long GET_WEBCAMS_TIMEOUT = 10000;

    private CameraQRService cameraQRService;
    private Stage primaryStage;

    @Override
    public void init() {
        // note this is in init as it **must not** be called on the FX Application Thread:
//        Webcam camera = Webcam.getWebcams().get(0);
        Webcam camera = null;
        try {
            camera = Webcam.getWebcams(GET_WEBCAMS_TIMEOUT).get(0);
        } catch (TimeoutException toex) {
            log.warn("No Webcam found within {} ms", GET_WEBCAMS_TIMEOUT);
        }

        cameraQRService = new CameraQRService(camera);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        QrCaptureView captureView  = new QrCaptureView(cameraQRService, this::scanListener, this::closeListener);

        Scene scene = new Scene(captureView);
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

    public static void main(String[] args) {
        launch(args);
    }
}
