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
package app.supernaut.camera.app.testcam;

import app.supernaut.fx.webcam.camera.CameraPublisher;
import app.supernaut.camera.fx.CameraPublisherView;
import app.supernaut.fx.webcam.camera.TestCameraPublisher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Sample Application that subscribes to and displays simulated video from
 * {@link TestCameraPublisher}.
 */
public class CountingWebcamApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(CountingWebcamApplication.class);

    private CameraPublisherView cameraView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CameraPublisher<BufferedImage> cameraPublisher = new TestCameraPublisher();
        cameraView  = new CameraPublisherView(cameraPublisher);
        BorderPane pane = new BorderPane();
        pane.setCenter(cameraView);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Webcam using " + cameraPublisher.getClass().getSimpleName());
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        cameraView.close();
    }
}
