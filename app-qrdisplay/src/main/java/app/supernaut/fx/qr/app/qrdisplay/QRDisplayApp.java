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
package app.supernaut.fx.qr.app.qrdisplay;

import app.supernaut.fx.qr.QRCodeImageView;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Sample JavaFX application that displays QR codes
 */
public class QRDisplayApp extends Application {
    private static final Logger log = LoggerFactory.getLogger(QRDisplayApp.class);
    private static final URI SUPERNAUT_URI = URI.create("https://www.supernaut.app");

    // Text input field for QR Code text (URI or non-URI)
    private final TextField textField = new TextField();
    // Hyperlink that displays the full URL as the link text  or disabled when not a "valid" URI
    private final Hyperlink hyperlink = new Hyperlink();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        textField.textProperty().addListener(this::inputTextChanged);
        textField.textProperty().set(SUPERNAUT_URI.toString());

        hyperlink.setOnAction(this::linkClicked);

        // Create an QRCodeImageView bound to codeStringProperty
        var image = new QRCodeImageView();
        image.bindQr(textField.textProperty());
        image.setEffect(new DropShadow());

        // Build a simple Window (aka Stage) containing the Hyperlink and Image
        var box = new VBox(textField, image, hyperlink);
        var pane = new Pane(box);
        var scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("QR Code Display Sample App");
        primaryStage.show();
    }

    /**
     * A {@link javafx.beans.value.ChangeListener} for the input {@link TextField}
     * @param observable The observable that changed.
     * @param oldVal The old value
     * @param newVal the new value
     */
    private void inputTextChanged(ObservableValue<? extends String> observable, String oldVal, String newVal) {
        boolean disable;
        String linkText;
        if (!newVal.isEmpty()) {
            try {
                URI uri = new URI(newVal);
                disable = !isClickable(uri);
                linkText = uri.toString();
            } catch (Exception e) {
                disable = true;
                linkText = "Invalid URI";
            }
        } else {
            disable = true;
            linkText = "-- empty string --";
        }
        hyperlink.setDisable(disable);
        hyperlink.textProperty().set(linkText);
    }

    private boolean isClickable(URI uri) {
        return uri.isAbsolute() && (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
    }
    
    private void linkClicked(ActionEvent event) {
        URI uri = URI.create(textField.getText()); // Throw an exception if textField is not a valid URI (shouldn't happen)
        this.getHostServices().showDocument(uri.toString());
    }
}
