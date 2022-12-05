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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.concurrent.Flow;

/**
 * CameraSubscriber: GUI framework-independent subscriber to {@link BufferedImage}
 * See {@code app.supernaut.camera.fx.CameraPublisherView} for JavaFX support.
 */
public class CameraSubscriber implements Flow.Subscriber<BufferedImage> {
    private static final Logger log = LoggerFactory.getLogger(CameraSubscriber.class);

    private final CameraSubscriberView view;
    private Flow.Subscription subscription;

    public CameraSubscriber(CameraSubscriberView cameraSubscriberView) {
        view = cameraSubscriberView;
    }

    public void cancel() {
        subscription.cancel();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.debug("onSubscribe");
        this.subscription = subscription;
        subscription.request(Integer.MAX_VALUE);
        view.updateStatus(false, "Subscribed - waiting for data");
    }

    @Override
    public void onNext(BufferedImage awtImage) {
        log.debug("onNext");
        view.updateImage(awtImage);
        view.updateStatus(true, "Running");
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("onError: ", throwable);
        view.updateStatus(false, "Error:" + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
        view.updateStatus(false, "Stopped");
    }
}
