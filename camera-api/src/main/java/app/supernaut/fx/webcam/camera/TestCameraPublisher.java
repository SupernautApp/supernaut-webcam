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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Publish a stream of camera images created by a generator function.
 */
public class TestCameraPublisher    extends SubmissionPublisher<BufferedImage>
                                    implements CameraPublisher<BufferedImage> {
    private static final Logger log = LoggerFactory.getLogger(TestCameraPublisher.class);

    private static final int CAMERA_WIDTH = 640;
    private static final int CAMERA_HEIGHT = 480;

    /**
     * Generate a black image with a small, white frame number in the center.
     */
    public static final Function<Long, BufferedImage> DEFAULT_GENERATOR = (Long n) -> {
        var image = new BufferedImage(CAMERA_WIDTH, CAMERA_HEIGHT, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.drawString(Long.toString(n), CAMERA_WIDTH / 2,CAMERA_HEIGHT / 2);

        return image;
    };
    
    final ScheduledFuture<?> periodicTask;
    final ScheduledExecutorService scheduler;

    private long frameCount = 0;

    /**
     * Camera frame publisher with default timing (1 frame per second) and default frame
     * generation function.
     */
    public TestCameraPublisher() {
        this(1, TimeUnit.SECONDS, DEFAULT_GENERATOR);
    }

    /**
     * @param imageGenerator frame generation function
     */
    public TestCameraPublisher(Function<Long, BufferedImage> imageGenerator) {
        this(1, TimeUnit.SECONDS, imageGenerator);
    }

    /**
     * @param period Number of frames per time-unit
     * @param unit time-unit for frame generation
     * @param imageGenerator frame generation function
     */
    public TestCameraPublisher(long period, TimeUnit unit, Function<Long, BufferedImage> imageGenerator) {
        super();
        scheduler = new ScheduledThreadPoolExecutor(1);
        periodicTask = scheduler.scheduleAtFixedRate(() -> submit(imageGenerator.apply(frameCount++)), 0, period, unit);
    }

    @Override
    public void close() {
        log.info("Closing");
        periodicTask.cancel(false);
        scheduler.shutdown();
        super.close();
    }
    
    @Override
    public int getCameraWidth() {
        return CAMERA_WIDTH;
    }

    @Override
    public int getCameraHeight() {
        return CAMERA_HEIGHT;
    }
}
