/*
 * Copyright 2019-2021 M. Sean Gilligan.
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
package app.supernaut.camera.fx;

import app.supernaut.fx.webcam.camera.CameraPublisher;
import app.supernaut.fx.webcam.camera.CameraSubscriber;
import app.supernaut.fx.webcam.camera.CameraSubscriberView;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

/**
 * JavaFX CameraView that is serviced by a {@link CameraPublisher}. Displays video when running or a label
 * with status/error information. Also handles conversion from {@link BufferedImage} to {@link javafx.scene.image.Image}.
 */
public class CameraPublisherView extends Region implements CameraSubscriberView, Closeable {
    private static final Logger log = LoggerFactory.getLogger(CameraPublisherView.class);
    private final ImageView imageView ;

    private final Label statusPlaceholder = new Label();

    private final CameraPublisher<BufferedImage> cameraPublisher;

    private final CameraSubscriber cameraSubscriber ;

    private final WritableImage fxImage;

    private boolean running = false;

    public CameraPublisherView(CameraPublisher<BufferedImage> cameraPublisher) {
        fxImage = new WritableImage(cameraPublisher.getCameraWidth(), cameraPublisher.getCameraHeight());
        this.cameraPublisher = cameraPublisher;
        this.cameraSubscriber = new CameraSubscriber(this);
        
        imageView = new ImageView();
        imageView.setImage(fxImage);
        imageView.setPreserveRatio(true);
        // make the cam behave like a mirror:
        //imageView.setScaleX(-1);

        statusPlaceholder.setText("Application started");
        getChildren().setAll(statusPlaceholder);
        requestLayout();

        cameraPublisher.subscribe(cameraSubscriber);
    }

    @Override
    protected void layoutChildren() {
        log.info("Layout");
        super.layoutChildren();
        double w = getWidth();
        double h = getHeight();
        if (isRunning()) {
            imageView.setFitWidth(w);
            imageView.setFitHeight(h);
            imageView.resizeRelocate(0, 0, w, h);
        } else {
            double labelHeight = statusPlaceholder.prefHeight(w);
            double labelWidth = statusPlaceholder.prefWidth(labelHeight);
            statusPlaceholder.resizeRelocate((w - labelWidth)/2, (h-labelHeight)/2, labelWidth, labelHeight);
        }
    }

    boolean isRunning() {
        return running;
    }

    @Override
    protected double computePrefWidth(double height) {
        return cameraPublisher.getCameraWidth();
    }
    @Override
    protected double computePrefHeight(double width) {
        return cameraPublisher.getCameraHeight();
    }

    @Override
    public void close() throws IOException {
        cameraSubscriber.cancel();
        cameraPublisher.close();
    }

    @Override
    public void updateImage(BufferedImage awtImage) {
        SwingFXUtils.toFXImage(awtImage, fxImage);
    }
    @Override
    public void updateStatus(boolean running, String statusText) {
        Platform.runLater(() -> {
            statusPlaceholder.setText(statusText);
            if (running) {
                if (this.running != running) {
                    getChildren().setAll(imageView);
                    requestLayout();
                }

            } else {
                if (this.running != running) {
                    getChildren().setAll(statusPlaceholder);
                    requestLayout();
                }
            }
            this.running = running;
        });
    }

}
