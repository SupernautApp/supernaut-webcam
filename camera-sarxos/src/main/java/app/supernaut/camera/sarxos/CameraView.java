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
package app.supernaut.camera.sarxos;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Based on https://github.com/sarxos/webcam-capture/tree/master/webcam-capture-examples/webcam-capture-javafx-service
 * @deprecated Use {@link app.supernaut.camera.fx.CameraPublisherView} instead.
 */
@Deprecated
public class CameraView extends Region {
    private static final Logger log = LoggerFactory.getLogger(CameraView.class);
    private final ImageView imageView ;
    private final CameraService service ;
    private final Label statusPlaceholder ;

    public CameraView(CameraService cameraService) {
        service = cameraService ;
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        // make the cam behave like a mirror:
        imageView.setScaleX(-1);

        this.statusPlaceholder = new Label();

        service.stateProperty().addListener((obs, oldState, newState) -> {
            switch (newState) {
                case READY:
                    statusPlaceholder.setText("Initializing");
                    getChildren().setAll(statusPlaceholder);
                    break;
                case SCHEDULED:
                    statusPlaceholder.setText("Waiting");
                    getChildren().setAll(statusPlaceholder);
                    break;
                case RUNNING:
                    imageView.imageProperty().unbind();
                    imageView.imageProperty().bind(cameraService.valueProperty());
                    getChildren().setAll(imageView);
                    break;
                case CANCELLED:
                    log.info("Cancelled");
                    imageView.imageProperty().unbind();
                    imageView.setImage(null);
                    statusPlaceholder.setText("Stopped");
                    getChildren().setAll(statusPlaceholder);
                    log.info("Processed cancel in view");
                    break;
                case FAILED:
                    imageView.imageProperty().unbind();
                    statusPlaceholder.setText("Error");
                    getChildren().setAll(statusPlaceholder);
                    log.error("FAILED: ", cameraService.getException());
                    break;
                case SUCCEEDED:
                    // unreachable...
                    imageView.imageProperty().unbind();
                    statusPlaceholder.setText("");
                    getChildren().clear();
            }
            requestLayout();
        });
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        double w = getWidth();
        double h = getHeight();
        if (service.isRunning()) {
            imageView.setFitWidth(w);
            imageView.setFitHeight(h);
            imageView.resizeRelocate(0, 0, w, h);
        } else {
            double labelHeight = statusPlaceholder.prefHeight(w);
            double labelWidth = statusPlaceholder.prefWidth(labelHeight);
            statusPlaceholder.resizeRelocate((w - labelWidth)/2, (h-labelHeight)/2, labelWidth, labelHeight);
        }
    }

    @Override
    protected double computePrefWidth(double height) {
        return service.getCameraWidth();
    }
    @Override
    protected double computePrefHeight(double width) {
        return service.getCameraHeight();
    }

    public CameraService getService() {
        return service ;
    }
}
