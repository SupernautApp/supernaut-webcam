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
package app.supernaut.fx.qr;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URI;

/**
 * ImageView wrapper for displaying QRCode images from a String (or URI)
 */
public class QRCodeImageView extends ImageView {
    private final StringProperty qrString = new SimpleStringProperty("");

    public QRCodeImageView() {
        super();
        qrString.addListener((observableVal, oldVal, newVal) -> imageProperty().set(render(newVal)));
    }

    public QRCodeImageView(String qrCodeString) {
        this();
        setQrCode(qrCodeString);
    }

    public QRCodeImageView(URI qrCodeUri) {                 
        this();
        setQrCode(qrCodeUri);
    }

    /**
     * Bind to a String property that provides the QR Code. When the property changes,
     * so will the image.
     * @param qrCodeProperty String property that provides the QR Code
     */
    public void bindQr(ReadOnlyStringProperty qrCodeProperty) {
        qrString.bind(qrCodeProperty);
    }

    /**
     * Bind to a URI property that provides the QR Code. When the property changes,
     * so will the image.
     * @param qrCodeUriProperty URI property that provides the QR Code
     */
    public void bindQr(ReadOnlyProperty<URI> qrCodeUriProperty) {
        qrString.bind(Bindings.createStringBinding(
                () -> qrCodeUriProperty.getValue().toString(), qrCodeUriProperty
            )
        );
    }

    public StringProperty qrCodeProperty() {
        return qrString;
    }

    public void setQrCode(String qrCodeString) {
        qrString.set(qrCodeString);
    }

    public void setQrCode(URI qrCodeUri) {
        setQrCode(qrCodeUri.toString());
    }

    private Image render(String string) {
        // TODO: Handle resizing, etc.
        if (string.isEmpty()) {
            return QRCodeImages.emptyImage();
        } else {
            return QRCodeImages.imageFromString(string, 320, 240);
        }
    }
}
