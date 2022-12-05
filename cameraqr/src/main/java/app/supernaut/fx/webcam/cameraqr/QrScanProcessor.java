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
package app.supernaut.fx.webcam.cameraqr;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * Processor that finds valid QR Code {@link Result}s in a stream of {@link BufferedImage}s.
 */
public class QrScanProcessor extends SubmissionPublisher<Result> implements Flow.Processor<BufferedImage, Result> {
    private static final Logger log = LoggerFactory.getLogger(QrScanProcessor.class);

    public QrScanProcessor(/*CameraPublisher<BufferedImage> cameraPublisher*/) {
        //cameraPublisher.subscribe(this);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("onSubscribe");
        subscription.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(BufferedImage bufferedImage) {
        log.info("onNext");
        Result result = scanForQR(bufferedImage);
        if (result != null) {
            submit(result);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("onError: ", throwable);
        closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
        close();
    }

    @Override
    public void close() {
        super.close();
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
