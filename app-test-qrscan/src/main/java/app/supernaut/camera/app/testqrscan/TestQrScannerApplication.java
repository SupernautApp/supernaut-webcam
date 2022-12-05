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
package app.supernaut.camera.app.testqrscan;

import app.supernaut.fx.qr.QRCodeImages;
import app.supernaut.fx.webcam.camera.CameraPublisher;
import app.supernaut.fx.webcam.camera.TestCameraPublisher;
import app.supernaut.fx.webcam.cameraqr.QrScanProcessor;
import app.supernaut.fx.webcam.cameraqr.QrScanView;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 */
public class TestQrScannerApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(TestQrScannerApplication.class);

    private Stage primaryStage;
    CameraPublisher<BufferedImage> cameraPublisher;
    QrScanProcessor processor;

    @Override
    public void init() {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        cameraPublisher = new TestCameraPublisher(this::imageGenerator);
        processor = new QrScanProcessor();
        cameraPublisher.subscribe(processor);

        QrScanView scanView  = new QrScanView(cameraPublisher, processor, this::scanListener, this::closeListener);

        Scene scene = new Scene(scanView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        processor.close();
        cameraPublisher.close();
    }

    private void scanListener(String result) {
        System.out.println("Result: " + result);
    }

    private void closeListener(Object result) {
        if (primaryStage != null) {
            primaryStage.hide();
        }
    }

    /**
     * Generate counting frames for n <= 5, after that generate a QR that changes every 15 frames.
     * @param n input frame number
     * @return A generated frame image
     */
    BufferedImage imageGenerator(long n) {
        if (n > 5) {
            String url = "https://www.supernaut.app/#" + (n / 15);
            BitMatrix matrix = QRCodeImages.matrixFromString(url,
                    cameraPublisher.getCameraWidth(),
                    cameraPublisher.getCameraHeight());
            return MatrixToImageWriter.toBufferedImage(matrix);
        } else {
            return TestCameraPublisher.DEFAULT_GENERATOR.apply(n);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
