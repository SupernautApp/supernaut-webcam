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
package app.supernaut.camera.fx;

import javafx.scene.image.Image;

import java.util.concurrent.Flow;
import java.awt.image.BufferedImage;

/**
 * FxImageProcessor: defines a {@link Flow.Processor} that converts a stream
 * of {@link BufferedImage} to a stream of {@link Image}.
 * TBD: An implementation of this interface.
 */
public interface FxImageProcessor extends Flow.Processor<BufferedImage, Image> {
}
