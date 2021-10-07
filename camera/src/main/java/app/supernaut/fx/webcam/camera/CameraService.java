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
package app.supernaut.fx.webcam.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 * Webcam JavaFX Service. Views, such as {@link CameraView} can bind to {@link CameraService#valueProperty()} to
 * get a series of updated {@link Image}s.
 * <p>
 * Based on https://github.com/sarxos/webcam-capture/tree/master/webcam-capture-examples/webcam-capture-javafx-service
 */
public class CameraService extends Service<Image> {
    private static final Logger log = LoggerFactory.getLogger(CameraService.class);
    private static final boolean logViewSizes = true;

    private final Webcam camera;
    private final WebcamResolution resolution;
    private final WritableImage fxImage;
    private Consumer<BufferedImage> imageListener = null;

    public CameraService(Webcam camera, WebcamResolution resolution) {
        this.camera = camera;
        this.resolution = resolution;
        //camera.setCustomViewSizes(resolution.getSize());
        camera.setViewSize(resolution.getSize());
        if (logViewSizes) {
            log.info("Camera supports the following sizes:");
            for (Dimension size : camera.getViewSizes()) {
                log.info(size.toString());
            }
        }

        fxImage = new WritableImage(resolution.getWidth(), resolution.getHeight());
    }

    public CameraService(Webcam camera) {
        this(camera, WebcamResolution.VGA);
    }

    /**
     * Add an image listener if you need to process raw images. For example, you might
     * want to scan for QR codes.
     * @param listener an image listener
     */
    public void addImageListener(Consumer<BufferedImage> listener) {
        this.imageListener = listener;
    }

    @Override
    public Task<Image> createTask() {
        return new Task<>() {
            @Override
            protected Image call() {

                try {
                    camera.open();
                    while (!isCancelled()) {
                        if (camera.isImageNew()) {
                            BufferedImage bimg = camera.getImage();
                            log.debug("BufferedImage: {}", bimg);
                            log.debug("BufferedImage: {} x {}", bimg.getWidth(), bimg.getHeight());
                            SwingFXUtils.toFXImage(bimg, fxImage);
                            updateValue(fxImage);
                            if (imageListener != null) {
                                imageListener.accept(bimg); // TODO: Really run this on current thread??
                            }
                        }
                    }
                    log.info("Cancelled, closing camera");
                    camera.close();
                    log.debug("Camera closed");
                    return getValue();
                } finally {
                    camera.close();
                }
            }

        };
    }

    public int getCameraWidth() {
        return resolution.getSize().width ;
    }

    public int getCameraHeight() {
        return resolution.getSize().height ;
    }
}
