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
package app.supernaut.fx.webcam.camera;


import java.awt.image.BufferedImage;

/**
 *  This could be a JavaFX view, but could also be a Swing or other view
 */
public interface CameraSubscriberView {
    void updateImage(BufferedImage awtImage);
    void updateStatus(boolean running, String statusText);
}
