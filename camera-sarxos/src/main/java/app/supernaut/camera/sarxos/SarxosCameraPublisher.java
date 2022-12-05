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

import app.supernaut.fx.webcam.camera.CameraPublisher;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.SubmissionPublisher;

/**
 * WIP
 */
public class SarxosCameraPublisher  extends SubmissionPublisher<BufferedImage>
                                    implements CameraPublisher<BufferedImage> {
    private static final Logger log = LoggerFactory.getLogger(SarxosCameraPublisher.class);
    private static final boolean logViewSizes = true;
    private final Webcam camera;
    private final WebcamResolution resolution;

    public SarxosCameraPublisher(Webcam camera, WebcamResolution resolution) {
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
    }

    public SarxosCameraPublisher(Webcam camera) {
        this(camera, WebcamResolution.VGA);
    }
    
    @Override
    public int getCameraWidth() {
        return resolution.getSize().width ;
    }

    @Override
    public int getCameraHeight() {
        return resolution.getSize().height ;
    }
}
