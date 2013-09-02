/*
 * ScreenshotHelper.java
 *
 * Copyright (c) 2013 Martin Burkhard and Sonja Maier.
 * CSCM Cooperation Systems Center Munich, Institute for Software Technology.
 * Bundeswehr University Munich. All rights reserved.
 *
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at <http://www.eclipse.org/legal/epl-v10.html>.
 *
 * The accompanying materials are made available under the terms
 * of Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * You should have received a copy of the license along with this
 * work.  If not, see <http://creativecommons.org/licenses/by-sa/3.0/>.
 *
 *  Project: FishificationFX
 *   Author: Martin Burkhard
 *     Date: 9/2/13 12:48 AM
 */

package org.sociotech.unui.javafx.engine2d.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ScreenshotHelper {

    private static final String TAG = ScreenshotHelper.class.getName();

    private ScreenshotHelper() {

    }

    public static void takeScreenshot(Scene scene, String path, String fileName) throws IOException {

        // Create output directory
        File filePath = new File(path);
        File outputDirectory = new File(filePath.getAbsolutePath());
        if (!outputDirectory.exists()) {
            boolean mkdirsSuccess = outputDirectory.mkdirs();
            if (!mkdirsSuccess) {
                Log.w("ScreenshotHelper", "Screenshot directory could not be created.");
                return;
            }
        }

        // Take snapshot
        WritableImage sceneSnapshot = scene.snapshot(null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(sceneSnapshot, null);

        // Create new file for storing the image
        File outputFile = new File(outputDirectory, String.format("%s000.png", fileName));

        // Adds numbers to the end of the images
        int counter = 0;
        while (outputFile.exists()) {
            String outputFileName = String.format("%s%03d.png", fileName, counter);
            outputFile = new File(outputDirectory, outputFileName);
            counter++;
        }

        Log.i(TAG, String.format("Taking screenshot: %s", outputFile));

        // Store BufferedImage
        ImageIO.write(bufferedImage, "png", outputFile);

    }
}
