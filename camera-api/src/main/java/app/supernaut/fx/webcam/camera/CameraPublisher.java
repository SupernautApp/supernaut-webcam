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

import java.io.Closeable;
import java.util.concurrent.Flow;

/**
 * Publish a stream of (camera) images of a known height and width.
 * @param <T> The image type. Typically, this is {@link java.awt.image.BufferedImage} but in the future
 * there may be implementations using {@code javafx.scene.image.Image}.
 */
public interface CameraPublisher<T> extends Flow.Publisher<T>, Closeable {
    int getCameraWidth();
    int getCameraHeight();
}
