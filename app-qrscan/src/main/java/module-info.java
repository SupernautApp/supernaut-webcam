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
/**
 *
 */
module app.supernaut.fx.webcam.app.qrscan {
    requires javafx.controls;

    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires webcam.capture;
    requires org.slf4j;

    requires app.supernaut.fx.webcam.camera;
    requires app.supernaut.fx.webcam.cameraqr;
    requires app.supernaut.camera.sarxos;

    exports app.supernaut.fx.webcam.app.qrscan;
}
