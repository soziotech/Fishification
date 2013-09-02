/*
 * Launch.java
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

package de.unibw.inf2.fishification;

import de.unibw.inf2.fishification.server.FishificationServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.util.Log;

/**
 * JavaFX main entry point.
 *
 * @author Martin Burkhard
 */
public final class Launch extends Application {

    private AbstractWorld m_world;
    private boolean m_serverMode = false;

    private static final String TAG = "Launch";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Log.i(TAG, "Fishification launched.");

        // Store JavaFx stage for thread-safe access
        StageSingleton.injectStage(primaryStage);

        boolean fullScreenMode = true;
        int serverPort = 8088;
        String serverEndpoint = "fishworld";
        try {
            // Load configuration
            PropertiesConfiguration config = new PropertiesConfiguration("resources/app.properties");

            // Assign properties
            m_serverMode = config.getBoolean("fishification.server.active");
            fullScreenMode = config.getBoolean("fishification.fullscreen");
            serverPort = config.getInt("fishification.server.port");
            serverEndpoint = config.getString("fishification.server.endpoint");

        } catch (ConfigurationException e) {
            Log.w(TAG, "Error reading configuration.", e);
        }

        // Init Server ?
        if (!m_serverMode) {

            Log.i(TAG, String.format("Starting in client mode with fullscreen: '%b'", fullScreenMode));

            // Create World
            m_world = new FishWorld();

            // Init World
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            m_world.initialize(screenBounds.getWidth(), screenBounds.getHeight());

            // Let's go
            m_world.launch();

        } else {

            // Launch Server
            Log.i(TAG, String.format("Starting server with port: '%d' endpoint: '%s' fullscreen: '%b'", serverPort,
                                     serverEndpoint, fullScreenMode));

            try {
                // Set up server
                FishificationServer.launch(serverPort, serverEndpoint);

            } catch (Exception e) {
                Log.e(TAG, "Error launching the server. Exit application.", e);
                Platform.exit();
            }
        }

        // Focus window
        primaryStage.show();

        // Full Screen mode
        if (fullScreenMode) {
            primaryStage.setFullScreen(true);
        }

    }

    @Override
    public void stop() {

        Log.i(TAG, "Fishification shutting down ...");

        // Tidy up
        if (!m_serverMode) {
            m_world.shutdown();
        } else {
            FishificationServer.shutdown();
        }
        Log.i(TAG, "Fishification exit.");
        Platform.exit();
    }

}
