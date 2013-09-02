/*
 * AbstractWorld.java
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
 *     Date: 9/2/13 10:43 AM
 */

package org.sociotech.unui.javafx.engine2d;

import com.google.common.io.Resources;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.sociotech.unui.javafx.engine2d.entities.Entity;
import org.sociotech.unui.javafx.engine2d.entities.EntityManager;
import org.sociotech.unui.javafx.engine2d.util.Log;
import org.sociotech.unui.javafx.engine2d.util.ScreenshotHelper;

import java.io.IOException;
import java.net.URL;

/**
 * Abstract 2D-Engine World for Sprite-based Entities.
 */
public abstract class AbstractWorld {

    // JavaFX Stage
    private final Stage m_worldStage;
    private       Scene m_worldCanvas;
    private       Group m_worldNodes;

    // Resources
    private EntityManager m_entityManager;
    private final ResourceManager<Image>     m_imageResourceManager = new ResourceManager<Image>();
    private final ResourceManager<AudioClip> m_audioResourceManager = new ResourceManager<AudioClip>();

    // Configuration
    private PropertiesConfiguration m_config = null;

    // Main Loop
    private boolean m_isPaused = false;
    private Timeline m_eventLoop;

    // FPS counter
    private double m_fps     = 0.0;
    private long   m_oldTime = 0;
    private int m_fpsCounter;

    // Text Font (default)
    private static String s_TextFontType = "Comic Sans MS";
    private static int    s_TextFontSize = 12;

    // Frames Per Second (FPS)
    private static boolean s_showFps = false;
    private Text m_fpsText;
    private static double s_fpsTextOffsetX = 90.0;
    private static double s_fpsTextOffsetY = 20.0;

    // Screen width and height
    private double m_width  = 0.0;
    private double m_height = 0.0;

    private static final String TAG = "World";

    protected AbstractWorld(String title, Stage worldStage) {
        m_worldStage = worldStage;
        m_worldStage.setTitle(title);
    }

    public void initialize(double width, double height) {

        m_width = width;
        m_height = height;

        // Init JavaFX Scene
        m_worldNodes = new Group();
        m_worldCanvas = new Scene(m_worldNodes, width, height);
        m_worldStage.setScene(m_worldCanvas);

        // Init Entity and Button Manager
        m_entityManager = new EntityManager(this);

        // Load configuration
        try {
            m_config = new PropertiesConfiguration("app.properties");
            loadConfiguration();

        } catch (ConfigurationException e) {
            Log.w("World", "Error reading configuration file 'app.properties' from resources.", e);
            return;
        }

        // Initialize event loop
        initEventLoop();

        // Initialize derived class
        onInitialize();

        // Initialize FPS text
        initFpsText();
    }

    private void loadConfiguration() {

        try {
            s_TextFontType = m_config.getString("fishification.text.font.type");
            s_TextFontSize = m_config.getInt("fishification.text.font.size");
            s_showFps = m_config.getBoolean("fishification.fps.show");
            s_fpsTextOffsetX = m_config.getDouble("fishification.label.fps.offset.x");
            s_fpsTextOffsetY = m_config.getDouble("fishification.label.fps.offset.y");

            onLoadConfiguration(m_config);
        } catch (Exception e) {
            Log.e(TAG, "Error loading configuration.", e);
        }
    }

    protected abstract void onLoadConfiguration(PropertiesConfiguration config);

    protected synchronized PropertiesConfiguration getConfiguration() {
        return m_config;
    }

    private void initEventLoop() {
        final Duration fps = Duration.millis(1000 / (float) 60);

        final KeyFrame eventLoop = new KeyFrame(fps, new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                // destroy entities on list
                getEntityManager().destroy();

                updateEntities();
                updateFps();

                onPostUpdate(m_fps);
            }

        });

        // Create Loop
        TimelineBuilder builder = TimelineBuilder.create();
        builder.cycleCount(Animation.INDEFINITE);
        builder.keyFrames(eventLoop);
        m_eventLoop = builder.build();
    }

    private void initFpsText() {
        m_fpsText = new Text();
        m_fpsText.setFont(Font.font(s_TextFontType, s_TextFontSize));
        m_fpsText.setX(getWorldCanvas().getWidth() - s_fpsTextOffsetX);
        m_fpsText.setY(s_fpsTextOffsetY);
        addNode(m_fpsText);
    }

    private void updateFps() {
        // Count frames
        m_fpsCounter++;

        long nanoTime = System.nanoTime();
        if (nanoTime > m_oldTime + 1000000000) {

            // Measure FPS
            m_fps = m_fpsCounter;

            m_fpsCounter = 0;
            m_oldTime = nanoTime;
        }
    }

    private synchronized void updateEntities() {
        for (Entity e : m_entityManager.getEntities()) {

            // Call update of every Entity
            e.update(getWorldCanvas(), e.getCurrentState(), m_fps);

            // Check collision with another Entity
            if (e.isCollidable()) {
                for (Entity e2 : m_entityManager.getEntities()) {
                    if (e2.isCollidable()) {

                        e.onCollisionWith(e2);
                    }
                }
            }
        }
    }

    protected abstract void onInitialize();

    private void onPostUpdate(double framesPerSecond) {

        if (s_showFps) {
            m_fpsText.setText(String.format("FPS: %.2f", framesPerSecond));
        }
    }

    public void launch() {
        m_eventLoop.play();
    }

    public void shutdown() {

        onShutdown();
        if (m_eventLoop != null) {
            m_eventLoop.stop();
            m_eventLoop = null;
        }

        if (m_entityManager != null) {
            m_entityManager.destroy();
            m_entityManager = null;
        }

        m_worldCanvas = null;
        m_worldNodes = null;
    }

    protected void onShutdown() {
    }

    public synchronized double getWidth() {
        return m_width;
    }

    public synchronized double getHeight() {
        return m_height;
    }

    public synchronized EntityManager getEntityManager() {
        return m_entityManager;
    }

    protected synchronized Scene getWorldCanvas() {
        return m_worldCanvas;
    }

    public synchronized Group getWorldNodes() {
        return m_worldNodes;
    }

    public synchronized boolean addNode(Node node) {
        return m_worldNodes.getChildren().add(node);
    }

    public synchronized boolean removeNode(Node node) {
        return m_worldNodes.getChildren().remove(node);
    }

    public synchronized ResourceManager<Image> getImageResourceManager() {
        return m_imageResourceManager;
    }

    protected synchronized void addImage(String imageName) {
        m_imageResourceManager.put(imageName, new Image(Resources.getResource("images/" + imageName).toExternalForm()));
    }

    protected synchronized void addImage(String imageName, int width, int height) {
        m_imageResourceManager.put(imageName,
                                   new Image(Resources.getResource("images/" + imageName).toExternalForm(), width,
                                             height, true, true));
    }

    synchronized ResourceManager<AudioClip> getAudioResourceManager() {
        return m_audioResourceManager;
    }

    protected void addAudioClip(String audioClipName) {
        URL bubbleAudioResource = Resources.getResource("sound/" + audioClipName);
        AudioClip bubbleAudioClip = new AudioClip(bubbleAudioResource.toExternalForm());
        getAudioResourceManager().put(audioClipName, bubbleAudioClip);
    }

    public void playAudioClip(String audioClipName, double volume) {
        // Play Audio
        ResourceManager<AudioClip> audioResourceManager = getAudioResourceManager();
        AudioClip audioClip = audioResourceManager.get(audioClipName);
        if (audioClip != null) {
            audioClip.play(volume);
        }
    }

    /**
     * Takes screenshot of the World's visible contents.
     *
     * @param path     Screenshot directory path
     * @param fileName Screenshot name added by number
     */
    protected synchronized void takeScreenshot(String path, String fileName) {
        try {
            ScreenshotHelper.takeScreenshot(m_worldCanvas, path, fileName);
        } catch (IOException e) {
            Log.e("World", "Error creating screenshot.", e);
        }
    }

    public synchronized boolean togglePause() {

        // toggle pause
        m_isPaused = !m_isPaused;

        if (m_isPaused) {
            m_eventLoop.pause();    // pause event loop
        } else {
            m_eventLoop.play();     // continue event loop
        }

        if (m_entityManager != null) {
            m_entityManager.pauseEntities(m_isPaused);
        }

        onPause(m_isPaused);

        return m_isPaused;
    }

    protected abstract void onPause(boolean isPaused);

    protected synchronized void toggleFps() {
        s_showFps = !s_showFps;
        if (!s_showFps) {
            m_fpsText.setText("");
        }
    }
}
