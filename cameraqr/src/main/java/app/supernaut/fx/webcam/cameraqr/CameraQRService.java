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
package app.supernaut.fx.webcam.cameraqr;

import app.supernaut.fx.webcam.camera.CameraService;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 *
 */
public class CameraQRService extends CameraService {
    private static final Logger log = LoggerFactory.getLogger(CameraQRService.class);
    private Consumer<String> qrScanListener = null;


    public CameraQRService(Webcam camera, WebcamResolution resolution) {
        super(camera, resolution);
        addImageListener(this::acceptImage);
    }

    public CameraQRService(Webcam camera) {
        this(camera, WebcamResolution.VGA);
    }

    public void addQRListener(Consumer<String> listener) {
        this.qrScanListener = listener;
    }

    private void acceptImage(BufferedImage bufferedImage) {
        Result result = scanForQR(bufferedImage);
        if (result != null && qrScanListener != null) {
            Platform.runLater(() -> qrScanListener.accept(result.getText()));
        }
    }

    private Result scanForQR(BufferedImage image) {
        // TODO: Considering doing this on a different thread
        Result result = null;
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            result = new MultiFormatReader().decode(bitmap);
            log.debug("QR Result: {}", result.getText());
        } catch (NotFoundException e) {
            // fall thru, it means there is no QR code in image
            log.trace("QR code not found in image");
        }
        return result;
    }

}
