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

import app.supernaut.camera.fx.CameraPublisherView;
import app.supernaut.fx.webcam.camera.CameraPublisher;
import com.google.zxing.Result;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 *
 */
public class QrScanView extends BorderPane {
    private static final Logger log = LoggerFactory.getLogger(QrScanView.class);

    private final Consumer<String> acceptedQRListener;
    private final Consumer<Object> closeListener;
    private final CameraPublisherView cameraView;
    private final Flow.Publisher<Result> qrCodePublisher;
    private final QrSubscriber qrSubscriber;
    private TextArea previewText;
    private Button cancelButton;
    private Button rescanButton;
    private Button acceptButton;

    private String previewResult;

    /**
     * JavaFX View with UI for a WebCam-based QR Code Scanner
     * @param cameraPublisher imagePublisher
     * @param qrCodePublisher qrCodePublisher
     * @param acceptedQRListener Listener called when QR code is accepted (accept button)
     * @param closeListener Listener called when view is closed (cancel or accept button)
     */
    public QrScanView(CameraPublisher<BufferedImage> cameraPublisher,
                      Flow.Publisher<Result> qrCodePublisher,
                      Consumer<String> acceptedQRListener,
                      Consumer<Object> closeListener) {
        //this.cameraPublisher = cameraPublisher;
        this.qrCodePublisher = qrCodePublisher;
        cameraView = new CameraPublisherView(cameraPublisher);
        this.acceptedQRListener = acceptedQRListener;
        this.closeListener = closeListener;

        setCenter(cameraView);

        Pane bottomBox = bottom();

        BorderPane.setAlignment(cameraView, Pos.CENTER);
        BorderPane.setMargin(cameraView, new Insets(5));
        BorderPane.setAlignment(bottomBox, Pos.CENTER);
        BorderPane.setMargin(bottomBox, new Insets(5));
        setBottom(bottomBox);

        qrSubscriber = new QrSubscriber();
        qrCodePublisher.subscribe(qrSubscriber);

        scan();
    }

    private VBox bottom() {
        previewText = new TextArea();

        Pane buttonPane = buttonBox();

        VBox bottomBox = new VBox(10);
        bottomBox.getChildren().addAll(previewText, buttonPane);
        return bottomBox;
    }

    private HBox buttonBox() {
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(this::cancelAction);

        rescanButton = new Button("Rescan");
        rescanButton.setOnAction(this::rescanAction);

        acceptButton = new Button("Accept");
        acceptButton.setOnAction(this::acceptAction);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(cancelButton, rescanButton, acceptButton);
        return buttonBox;
    }

    /*
     * This method is called when the user presses the "Accept" button.
     * At this point the
     */
    private void acceptAction(ActionEvent actionEvent) {
        acceptedQRListener.accept(previewResult);
        closeParent();
    }

    private void cancelAction(ActionEvent actionEvent) {
        closeParent();
    }

    private void rescanAction(ActionEvent e) {
        scan();
    }

    /*
     * This method is called every time a valid QR code is detected and parsed.
     * The result is stored in {@code previewText} and displayed to the user.
     */
    private void resultListener(Result result) {
//        if (cameraQRService.isRunning()) {
//            cameraQRService.cancel();
//        }
        // Subscriber is already stopped because it only requests 1 result at a time
        log.info("QR result: {}", result);
        previewText.setText(result.getText());
        previewResult = result.getText();

        rescanButton.setDisable(false);
        acceptButton.setDisable(false);

    }

    private void scan() {
//        if (!cameraQRService.isRunning()) {
//            cameraQRService.restart();
//        }
        qrSubscriber.restart();
        previewText.setText("");
        acceptButton.setDisable(true);
        rescanButton.setDisable(true);
    }

    private void stopCameraService() {
//        if (cameraQRService.isRunning()) {
//            cameraQRService.cancel();
//        }
        qrSubscriber.cancel();
    }

    private void closeParent() {
        stopCameraService();
        closeListener.accept(null);
    }

    class QrSubscriber implements Flow.Subscriber<Result> {
        private Flow.Subscription subscription;
        AtomicInteger requests = new AtomicInteger(0);

        public boolean restart() {
            if (subscription != null) {
                if (requests.incrementAndGet() == 1) {
                    subscription.request(1);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public void cancel() {
            if (subscription != null) {
                subscription.cancel();
            }
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            log.info("onSubscribe");
            this.subscription = subscription;
            requests.incrementAndGet();
            subscription.request(1);
        }

        @Override
        public void onNext(Result item) {
            log.debug("onNext");
            requests.decrementAndGet();
            Platform.runLater(() -> resultListener(item));
        }

        @Override
        public void onError(Throwable throwable) {
            log.error("onError", throwable);
        }

        @Override
        public void onComplete() {
            log.info("onComplete");
        }
    }
}
