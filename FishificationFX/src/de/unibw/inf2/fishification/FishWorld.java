/*
 * FishWorld.java
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
 *     Date: 9/2/13 11:00 AM
 */

package de.unibw.inf2.fishification;

import de.unibw.inf2.fishification.buttons.ButtonManager;
import de.unibw.inf2.fishification.categories.CategoryMapper;
import de.unibw.inf2.fishification.categories.CategoryType;
import de.unibw.inf2.fishification.categories.PersonType;
import de.unibw.inf2.fishification.data.ContentLoader;
import de.unibw.inf2.fishification.entities.*;
import de.unibw.inf2.fishification.server.fishworld.IFishWorld;
import de.unibw.inf2.fishification.util.XlsLogger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.entities.Entity;
import org.sociotech.unui.javafx.engine2d.entities.StaticEntity;
import org.sociotech.unui.javafx.engine2d.util.Log;

import javax.inject.Inject;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The Fishification AquariumApp.
 */
public class FishWorld extends AbstractWorld implements IFishWorld {

    // Show Background
    private static boolean s_showBackground      = true;
    // Show Logo
    private static boolean s_showLogo            = true;
    // Show Foodbox
    private static boolean s_showFoodBox         = false;
    // Enable Wave effect
    private static boolean s_flagApplyWaveEffect = true;

    // Show Persons instead of contents
    private static boolean s_isPersonCentric = false;

    // Logo (default)
    private static int s_logoWidth  = 300;
    private static int s_logoHeight = 200;

    // Fish Food (default)
    private static int    s_foodBoxWidth       = 150;
    private static int    s_foodBoxHeight      = 178;
    private static double s_lastFeedTime       = 0.0;
    private static double s_feedFrequency      = 200.0;
    private static double s_fishIncreaseFactor = 0.01;

    // Button Images (default)
    private static int s_buttonImageWidth  = 80;
    private static int s_buttonImageHeight = 40;

    // Screenshots (default)
    private static String s_screenshotsPath     = "screenshots";
    private static String s_screenshotsFileName = "fishification";

    // Logging (default)
    private        XlsLogger m_xlsLogger                 = null;
    private static String    s_xlsLoggerOutputPath       = "eval";
    private static String    s_xlsLoggerOutputFileName   = "FishificationEval.xls";
    private static String    s_xlsLoggerInitialSheetName = "Fishification";

    // Content Loader
    private ContentLoader m_contentLoader;
    private        int m_fishCounter    = 0;
    private static int s_amountOfFishes = 10;

    // Button Manager
    private ButtonManager m_buttonManager = null;

    private static final int SCREENSHOT_DELAY = 500;
    private static final String TAG = "FishWorld";

    @Inject
    public FishWorld() {
        // call superclass and set title
        super("Fishification", StageSingleton.getStage());
    }

    @Override
    protected void onLoadConfiguration(PropertiesConfiguration config) {

        // Set properties
        s_amountOfFishes = config.getInt("fishification.fish.amount", 10);
        s_isPersonCentric = config.getBoolean("fishification.personcentric");
        s_showBackground = config.getBoolean("fishification.background.show");
        s_flagApplyWaveEffect = config.getBoolean("fishification.waveeffect.show");

        s_showLogo = config.getBoolean("fishification.logo.show");
        s_logoWidth = config.getInt("fishification.logo.width");
        s_logoHeight = config.getInt("fishification.logo.height");

        s_showFoodBox = config.getBoolean("fishification.foodbox.show");
        s_foodBoxWidth = config.getInt("fishification.foodbox.width");
        s_foodBoxHeight = config.getInt("fishification.foodbox.height");
        s_feedFrequency = config.getDouble("fishification.fishfood.frequency");
        s_fishIncreaseFactor = config.getDouble("fishification.fishfood.increase");

        s_buttonImageWidth = config.getInt("fishification.button.img.width");
        s_buttonImageHeight = config.getInt("fishification.button.img.height");

        s_screenshotsPath = config.getString("screenshots.path");
        s_screenshotsFileName = config.getString("screenshots.filename");

        String restBaseUrl = config.getString("communitymashup.restbaseurl");
        int restBasePort = config.getInt("communitymashup.restbaseurlport");
        String restEndpoint = config.getString("communitymashup.restendpoint");
        m_contentLoader = new ContentLoader(restBaseUrl, restBasePort, restEndpoint, this);

        s_xlsLoggerOutputPath = config.getString("logging.xls.path");
        s_xlsLoggerOutputFileName = config.getString("logging.xls.filename");
        s_xlsLoggerInitialSheetName = config.getString("logging.xls.sheetname");
    }

    @Override
    protected void onInitialize() {

        initResources();

        // Load Fish Contents
        loadFishContents();

        initVisuals();

        // Initialize XLS Logger
        initXlsLogger();

        // Register for key events
        registerKeys();
    }

    @Override
    protected void onShutdown() {
        m_contentLoader.shutdown();
    }

    @Override
    protected void onPause(boolean isPaused) {
        m_contentLoader.pause(isPaused);
    }

    private void initResources() {
        // Init Image resources
        addImage("Burbot_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Burbot_Animation.png");
        addImage("Christmas_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Christmas_Fish_Animation.png");
        addImage("CSCM_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("CSCM_Fish_Animation.png");
        addImage("CSCM_logo_color.png", s_logoWidth, s_logoHeight);
        addImage("Glasses_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Glasses_Fish_Animation.png");
        addImage("Koffer_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Koffer_Fish_Animation.png");
        addImage("Lady_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Lady_Fish_Animation.png");
        addImage("Mark_the_Shark_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Mark_the_Shark_Animation.png");
        addImage("Mendeley_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Mendeley_Fish_Animation.png");
        addImage("Octopus_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Octopus_Animation.png");
        addImage("Pinky_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Pinky_Animation.png");
        addImage("Red_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Red_Fish_Animation.png");
        addImage("Seahorse_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Seahorse_Animation.png");
        addImage("Sociotech_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Sociotech_Fish_Animation.png");
        addImage("Studiendekan_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Studiendekan_Fish_Animation.png");
        addImage("Twitter_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("Twitter_Fish_Animation.png");
        addImage("UniBwM_Fish_Button.png", s_buttonImageWidth, s_buttonImageHeight);
        addImage("UniBwM_Fish_Animation.png");
        addImage("Foodbox.png", s_foodBoxWidth, s_foodBoxHeight);

        // Init Audio resources
        addAudioClip("bubble.wav");
    }

    private void initVisuals() {
        // Show aquarium
        if (s_showBackground) {
            fillAquariumBackground();
        }

        // Show logo
        if (s_showLogo) {
            createStaticEntity(new LogoEntity());
        }

        // Show foodbox
        if (s_showFoodBox) {
            createStaticEntity(new FoodboxEntity());
        }

        // Create Buttons
        createButtons();
    }

    private void createButtons() {
        m_buttonManager = new ButtonManager(getConfiguration(), this);
        m_buttonManager.initialize();
    }

    public void loadFishContents() {

        Log.i(TAG, "Loading contents...");

        // Asynchronously adds contents
        m_contentLoader.getContentItems(s_amountOfFishes);

        // Christmas Time =)
        if (getConfiguration().getBoolean("fishification.christmasfish.show")) {
            Log.i(TAG, "Added Christmas fish.");
            getEntityManager().addEntity(EntityFactory.createChristmasFish(this));
            m_fishCounter++;
        }
    }

    private void registerKeys() {
        getWorldCanvas().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode().equals(KeyCode.DIGIT1)) {
                    toggleFps();
                } else if (keyEvent.getCode().equals(KeyCode.DIGIT2)) {
                    toggleBackground();
                } else if (keyEvent.getCode().equals(KeyCode.DIGIT3)) {
                    toggleWaveEffect();
                } else if (keyEvent.getCode().equals(KeyCode.DIGIT0)) {
                    asyncTakeScreenshot();
                } else if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    feedFish("all");
                } else if (keyEvent.getCode().equals(KeyCode.SPACE)) {
                    togglePause();
                }

            }
        });
    }

    private void createStaticEntity(StaticEntity entity) {
        getEntityManager().addEntity(entity);
    }

    private void initXlsLogger() {
        try {

            // Prepare application string
            String applicationVersion = getConfiguration().getString("application.version", "0.0.1");
            applicationVersion += "." + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            // Init Logger
            File outputDirectory = new File(s_xlsLoggerOutputPath);
            m_xlsLogger = new XlsLogger(outputDirectory, s_xlsLoggerOutputFileName, s_xlsLoggerInitialSheetName,
                                        applicationVersion);
        } catch (Exception e) {
            Log.e("FishEntity", "Error while opening XLS logger.", e);
            m_xlsLogger = null;
        }
    }

    /**
     * Draws the background of the aquarium.
     */
    private void fillAquariumBackground() {

        RadialGradientBuilder gradientBuilder = RadialGradientBuilder.create();
        gradientBuilder.centerX(getWorldCanvas().getWidth() / 2);
        gradientBuilder.centerY(50);
        gradientBuilder.radius(1000);
        gradientBuilder.proportional(false);
        gradientBuilder.stops(new Stop(0.0, Color.WHITE), new Stop(0.05, Color.rgb(245, 251, 251)),
                              new Stop(0.1, Color.rgb(220, 242, 239)), new Stop(0.2, Color.rgb(164, 214, 211)),
                              new Stop(0.3, Color.rgb(142, 195, 199)), new Stop(0.4, Color.rgb(111, 170, 184)),
                              new Stop(0.5, Color.rgb(84, 145, 166)), new Stop(0.6, Color.rgb(61, 125, 152)),
                              new Stop(0.7, Color.rgb(50, 111, 140)), new Stop(0.8, Color.rgb(33, 96, 129)),
                              new Stop(0.9, Color.rgb(25, 85, 121)), new Stop(1.0, Color.rgb(19, 77, 115)));
        RadialGradient gradient = gradientBuilder.build();

        getWorldCanvas().setFill(gradient);
    }

    public int getAmountOfFishes() {
        return m_fishCounter;
    }

    /**
     * Removes all fishes.
     */
    public void clearAll() {

        Log.d(TAG, "Removing all fish entities, buttons and Persons.");

        m_contentLoader.stopContentLoader();

        // Remove Buttons
        getButtonManager().removeCategoryButtons();

        PersonType.clear();
        getEntityManager().clear();
        m_fishCounter = 0;
    }

    public XlsLogger getXlsLogger() {

        return m_xlsLogger;
    }

    void toggleBackground() {
        s_showBackground = !s_showBackground;
        if (!s_showBackground) {
            getWorldCanvas().setFill(Color.GHOSTWHITE);
        } else {
            fillAquariumBackground();
        }

    }

    void toggleWaveEffect() {
        s_flagApplyWaveEffect = !s_flagApplyWaveEffect;
    }

    public boolean isWaveEffectEnabled() {
        return s_flagApplyWaveEffect;
    }

    public boolean isPersonCentric() {
        return s_isPersonCentric;
    }

    public boolean toggleSourcePersonCentric() {
        s_isPersonCentric = !s_isPersonCentric;
        return s_isPersonCentric;
    }

    public synchronized ButtonManager getButtonManager() {
        return m_buttonManager;
    }

    /**
     * Asynchronoulsy takes screenshots with short delay.
     */
    public void asyncTakeScreenshot() {

        // First, asynchronous wait
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SCREENSHOT_DELAY);
                } catch (InterruptedException e) {
                    Log.w("PauseButton", "Screenshot sleep interrupted.");
                }

                // Second, Screenshot in main thread
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        takeScreenshot(s_screenshotsPath, s_screenshotsFileName);
                    }
                });

            }
        }).start();


    }

    public boolean addFishEntity(final FishEntity fishEntity) {

        // Required for backend thread calls
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getEntityManager().addEntity(fishEntity);

                // Remove buttons
                getButtonManager().removeCategoryButtons();

                // Create buttons
                getButtonManager().initCategoryButtons();
            }
        });

        // Increase fish counter
        m_fishCounter++;

        return true;
    }

    public boolean feedFish(final String source) {

        if (source == null || source.isEmpty()) {
            return false;
        }

        final FoodEntity foodEntity = EntityFactory.createFoodEntity(source, this);
        if (foodEntity == null) {
            return false;
        }

        // Required for backend thread calls
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                getEntityManager().addEntity(foodEntity);
            }
        });
        return true;

    }

    public void increaseFishSize(String source) {

        // Prevent too many increase operations
        long actualTime = System.currentTimeMillis();
        if (actualTime - s_lastFeedTime < s_feedFrequency) {
            return;
        }
        s_lastFeedTime = actualTime;

        // Convert source name to category type
        CategoryType type = CategoryMapper.map(source);

        // Copy all FishEntities
        List<FishEntity> fishEntities = new ArrayList<FishEntity>();
        for (Entity entity : getEntityManager().getEntities()) {
            if (entity instanceof FishEntity) {
                FishEntity fishEntity = (FishEntity) entity;

                // Add fish if the category matches
                if (type == CategoryType.NONE || type == fishEntity.getType()) {
                    fishEntities.add(fishEntity);
                }
            }
        }

        if (type == CategoryType.NONE) {

            // Shuffle Fish randomly
            Collections.shuffle(fishEntities);

            // Increase only the first in the shuffled list
            FishEntity fishToFeed = fishEntities.get(0);
            fishToFeed.increaseSize(s_fishIncreaseFactor);

        } else {

            // Increase all Fish according to source
            for (FishEntity fishToFeed : fishEntities) {
                fishToFeed.increaseSize(s_fishIncreaseFactor);
            }

        }
    }
}
