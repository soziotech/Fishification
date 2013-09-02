/*
 * FishEntity.java
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
 *     Date: 9/2/13 11:24 PM
 */

package de.unibw.inf2.fishification.entities;

import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.categories.CategoryType;
import de.unibw.inf2.fishification.data.ContentItem;
import de.unibw.inf2.fishification.util.XlsLogger;
import de.unibw.inf2.fishification.util.XlsLoggerEntry;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.entities.Entity;
import org.sociotech.unui.javafx.engine2d.entities.EntityState;
import org.sociotech.unui.javafx.engine2d.sprites.Sprite;
import org.sociotech.unui.javafx.engine2d.util.RandomFactory;

import java.util.Date;

/**
 * Initializes and draws fish and its flag.
 *
 * @author Martin Burkhard, Sonja Maier
 */
public class FishEntity extends Entity {

    // adjust the following numbers to your needs:

    private static final double FISH_TOP_OFFSET    = 200.0;
    private static final double FISH_BOTTOM_OFFSET = 100.0;

    private static final double BUBBLE_RADIUS = 200;

    // the X- and Y-offsets of the bubble relative to the screen
    private static final double BUBBLE_OFFSET_X = 50;
    private static final double BUBBLE_OFFSET_Y = -BUBBLE_RADIUS / 1.3;

    // number of seconds until the bubble disappears
    private static final int BUBBLE_HIDE_SECONDS = 30;

    // the speed of the fishes in x- and y-direction
    private double s_fishSpeedX = 0.2;
    private double s_fishSpeedY = 0.1;

    private static final double FISH_SPRITE_OPACITY               = 0.8;
    private static final double FISH_SIZE_MINIMUM                 = 0.4;
    private static final double FISH_SIZE_FACTOR                  = 0.05;
    private static final int    FISH_SIZE_VARIATION               = 1;
    private static final int    FISH_ANIMATION_DURATION_MINIMUM   = 1500;
    private static final int    FISH_ANIMATION_DURATION_VARIATION = 200;

    private static final double FISH_TEXT_OFFSET_X       = 10.0;
    private static final String FLAG_TEXT_FONT_TYPE      = "Comic Sans MS";
    private static final int    FLAG_TEXT_FONT_SIZE      = 12;
    private static final int    FLAG_TEXT_WRAPPING_WIDTH = 180; // the size of the text in
    // the flag
    private static final double FLAG_PADDING             = 3.0; // the padding around the flag
    private static final double FLAG_ROPE_STROKE_WIDTH   = 0.5;

    private final ContentItem m_fishContent;
    private final Sprite      m_fishSprite;
    private       double      m_fishPosX;
    private       double      m_fishPosY;

    private final Text         m_flagText    = new Text();
    private final Rectangle    m_flagRect    = new Rectangle();
    private final Path         m_flagRope1   = new Path();
    private final Path         m_flagRope2   = new Path();
    private       BubbleEntity m_bubble      = null;
    private       Timeline     m_bubbleTimer = null;

    private boolean s_directionInvertedX     = false;
    private double  s_flagWaveEffectValue    = 0.0;
    private boolean s_flagWaveEffectIncrease = true;

    private double m_dragLastX = 0.0, m_dragLastY = 0.0;
    private boolean m_dragged = false;

    private final        FloatMap        m_floatMap        = new FloatMap();
    private final        DisplacementMap m_displacementMap = new DisplacementMap();
    private static final int             WAVE_HEIGHT       = 100;
    private static final double          WAVE_FREQUENCY    = 50.0;

    private                            FishWorld m_fishWorld = null;
    private static final Logger    m_log       = LogManager.getLogger();

    public FishEntity(ContentItem content, Sprite sprite, double posX, double posY) {

        // Assignments
        m_fishContent = content;
        m_fishSprite = sprite;
        m_fishPosX = posX;
        m_fishPosY = posY;
    }

    @Override
    public void onInit(AbstractWorld world) {

        // Set initial position
        setPosition(new Point2D(m_fishPosX, m_fishPosY));

        // Adjust sprite
        m_fishSprite.getSpriteView().opacityProperty().set(FISH_SPRITE_OPACITY);

        // Get world
        m_fishWorld = (FishWorld) world;

        // Init float map
        m_floatMap.setWidth(FLAG_TEXT_WRAPPING_WIDTH);
        m_floatMap.setHeight(WAVE_HEIGHT);

        // Add text to node group
        initFlagText(m_fishContent.getFlagTitle());

        // Set random animation speed
        randomAnimationSpeed();

        // Everything starts moving
        setCurrentState(EntityState.MOVE);

        // Generate random scale
        randomScale();

        // Generate random directions
        randomDirection();

        // Create flag for the text
        createFlagWithRopes();

        // Add nodes - the order defines the Z position
        addNode(m_flagRope1);
        addNode(m_flagRope2);
        addNode(m_flagRect);
        addNode(m_flagText);
        addNode(m_fishSprite.getSpriteView());
    }

    private void initFlagText(String text) {
        m_flagText.setText(text);
        m_flagText.setTranslateY(m_fishSprite.getHeight() / 2); // set relative
        // Y-Position
        m_flagText.setFont(Font.font(FLAG_TEXT_FONT_TYPE, FLAG_TEXT_FONT_SIZE));
        m_flagText.setFontSmoothingType(FontSmoothingType.LCD);
        m_flagText.setWrappingWidth(FLAG_TEXT_WRAPPING_WIDTH);

        if (m_fishWorld.isWaveEffectEnabled()) {
            applyWaveEffect(m_flagText, s_flagWaveEffectValue);
        }
    }

    private void randomAnimationSpeed() {
        int speed = FISH_ANIMATION_DURATION_MINIMUM + RandomFactory.getRandomInt(FISH_ANIMATION_DURATION_VARIATION);

        // First start animation as we need the first frame
        m_fishSprite.startAnimation(new Duration(speed));
    }

    private void randomScale() {
        //
        double scale = FISH_SIZE_FACTOR * RandomFactory.getRandomInt(FISH_SIZE_VARIATION) + FISH_SIZE_MINIMUM;
        m_fishSprite.getSpriteView().setScaleX(scale);
        m_fishSprite.getSpriteView().setScaleY(scale);
    }

    private void randomDirection() {

        // Calculate speed in X-direction
        s_fishSpeedX = RandomFactory.getRandomDouble() * s_fishSpeedX;
        s_fishSpeedY = RandomFactory.getRandomDouble() * s_fishSpeedY;
        s_directionInvertedX = RandomFactory.getRandomBoolean();
        boolean directionInvertedY = RandomFactory.getRandomBoolean();

        if (!s_directionInvertedX) {

            // Set text behind sprite in normal direction
            double textWidth = Math.min(m_flagText.getWrappingWidth(), m_flagText.getBoundsInLocal().getWidth());
            if (textWidth == 0.0) {
                textWidth = m_flagText.getBoundsInLocal().getWidth();
            }
            m_flagText.setTranslateX(-textWidth - FISH_TEXT_OFFSET_X);
            m_flagText.setTextAlignment(TextAlignment.RIGHT);

        } else { // invert

            // move in opposite direction
            s_fishSpeedX = -s_fishSpeedX;

            // flip sprite so that it shows in other direction
            m_fishSprite.flipX();

            // Set text behind sprite in inverted direction

            m_flagText.setTranslateX(m_fishSprite.getWidth() + FISH_TEXT_OFFSET_X);
        }

        if (directionInvertedY) { // invert

            // move in opposite direction

            s_fishSpeedY = -s_fishSpeedY;
        }

    }

    private void createFlagWithRopes() {

        // Fill the flag with yellow gradient
        LinearGradient textRectGradient = LinearGradientBuilder.create().startX(50).startY(50).endX(100).endY(
                100).proportional(false).stops(new Stop(0.0f, Color.rgb(220, 220, 220, .784)),
                                               new Stop(1.0f, Color.rgb(180, 180, 180, .784))).build();
        m_flagRect.setFill(textRectGradient);

        // Adjust the flags position
        Bounds textBounds = m_flagText.getBoundsInLocal();
        double baseLineOffset = m_flagText.getBaselineOffset();
        m_flagRect.setTranslateX(m_flagText.getTranslateX() - FLAG_PADDING);
        m_flagRect.setTranslateY(m_flagText.getTranslateY() - baseLineOffset - FLAG_PADDING);
        m_flagRect.setWidth(textBounds.getWidth() + FLAG_PADDING * 2);
        m_flagRect.setHeight(textBounds.getHeight() + FLAG_PADDING * 2);

        // Create flag rope #01
        MoveTo moveTo1 = new MoveTo(m_fishSprite.getWidth() / 2, m_fishSprite.getHeight() / 2);
        LineTo lineTo1 = new LineTo(m_flagRect.getTranslateX() + m_flagRect.getWidth(), m_flagRect.getTranslateY());
        if (s_directionInvertedX) {
            lineTo1.setX(m_flagRect.getTranslateX());
        }
        m_flagRope1.getElements().add(moveTo1);
        m_flagRope1.getElements().add(lineTo1);
        m_flagRope1.setStrokeWidth(FLAG_ROPE_STROKE_WIDTH);
        m_flagRope1.setStroke(Color.rgb(0, 0, 0, 0.5));

        // Create flag rope #02
        MoveTo moveTo2 = new MoveTo(m_fishSprite.getWidth() / 2, m_fishSprite.getHeight() / 2);
        LineTo lineTo2 = new LineTo(m_flagRect.getTranslateX() + m_flagRect.getWidth(),
                                    m_flagRect.getTranslateY() + m_flagRect.getHeight());
        if (s_directionInvertedX) {
            lineTo2.setX(m_flagRect.getTranslateX());
        }
        m_flagRope2.getElements().add(moveTo2);
        m_flagRope2.getElements().add(lineTo2);
        m_flagRope2.setStrokeWidth(FLAG_ROPE_STROKE_WIDTH);
        m_flagRope2.setStroke(Color.rgb(0, 0, 0, 0.5));
    }

    /**
     * Applies wave distortion to given node.
     *
     * @param node  Node the effect is applied to
     * @param value Size of distortion.
     */
    private void applyWaveEffect(Node node, double value) {

        for (int i = 0; i < FLAG_TEXT_WRAPPING_WIDTH; i++) {
            double v = (Math.sin(i / 20.0 * Math.PI * value) - 0.5) / WAVE_FREQUENCY;

            for (int j = 0; j < WAVE_HEIGHT; j++) {
                m_floatMap.setSamples(i, j, 0.0f, (float) v);
            }
        }

        m_displacementMap.setMapData(m_floatMap);
        node.setEffect(m_displacementMap);
    }

    @Override
    public void onUpdate(Scene worldCanvas, EntityState entityState, double framesPerSecond) {

        if (entityState == EntityState.MOVE) {
            Point2D oldPosition = getPosition();
            double oldPosX = oldPosition.getX();
            double oldPosY = oldPosition.getY();

            // Move in X-Direction
            double newPosX = oldPosX + s_fishSpeedX;

            // Fishes that leave on the right side reappear on the left
            if (newPosX > worldCanvas.getWidth()) {
                newPosX = -m_fishSprite.getWidth();
            }

            // Fishes that leave on the left side reappear on the right
            if (newPosX < -m_fishSprite.getWidth()) {
                newPosX = worldCanvas.getWidth();
            }

            // Move in Y-Direction
            double newPosY = oldPosY + s_fishSpeedY;

            // Fishes that leave the bottom side will turn around
            if (newPosY > worldCanvas.getHeight() - m_fishSprite.getHeight() - FISH_BOTTOM_OFFSET) {
                s_fishSpeedY = -s_fishSpeedY;
            }

            // Fishes that leave the top side will turn around
            if (newPosY < m_fishSprite.getHeight() + FISH_TOP_OFFSET) {
                s_fishSpeedY = -s_fishSpeedY;
            }

            setPosition(new Point2D(newPosX, newPosY));
        }

        // startAnimation Wave effect
        if (m_fishWorld.isWaveEffectEnabled()) {

            if (entityState != EntityState.PAUSE) {
                double delta = 0.01;
                if (s_flagWaveEffectIncrease) {
                    s_flagWaveEffectValue = s_flagWaveEffectValue + delta;
                    if (s_flagWaveEffectValue > 1.0) {
                        s_flagWaveEffectIncrease = false;
                    }
                } else {
                    s_flagWaveEffectValue -= delta;
                    if (s_flagWaveEffectValue < -1.0) {
                        s_flagWaveEffectIncrease = true;
                    }
                }
            }
            applyWaveEffect(m_flagText, s_flagWaveEffectValue);
            applyWaveEffect(m_flagRect, s_flagWaveEffectValue);
        }
    }

    @Override
    protected void onPause(boolean isPaused, EntityState beforePauseState) {
        m_fishSprite.pauseAnimation(isPaused);

        if (m_bubbleTimer != null) {
            if (isPaused) {
                m_bubbleTimer.pause();
            } else {
                m_bubbleTimer.play();
            }
        }
    }

    @Override
    protected void onShow() {
        m_fishSprite.setVisible(true);
        m_flagText.setVisible(true);
        m_flagRect.setVisible(true);
        m_flagRope1.setVisible(true);
        m_flagRope2.setVisible(true);
    }

    @Override
    protected void onHide() {
        m_fishSprite.setVisible(false);
        m_flagText.setVisible(false);
        m_flagRect.setVisible(false);
        m_flagRope1.setVisible(false);
        m_flagRope2.setVisible(false);
        hideBubble();
    }

    @Override
    public void onCollisionWith(Entity e) {

    }

    @Override
    protected boolean onMousePressedEvent(MouseEvent mouseEvent) {

        m_dragLastX = mouseEvent.getX();
        m_dragLastY = mouseEvent.getY();
        m_dragged = false;

        return true;
    }

    @Override
    protected boolean onMouseDraggedEvent(MouseEvent mouseEvent) {

        // Calculate delta
        double layoutX = getLayoutX() + mouseEvent.getX() - m_dragLastX;
        double layoutY = getLayoutY() + mouseEvent.getY() - m_dragLastY;

        // Assign new position
        setLayoutX(layoutX);
        setLayoutY(layoutY);

        m_dragged = true;

        return true;
    }

    @Override
    protected boolean onMouseClickedEvent(MouseEvent mouseEvent) {

        if (getCurrentState() == EntityState.PAUSE || m_dragged) {
            return false;
        }

        Point2D position = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        toggleBubble(position, "click");

        return true;
    }

    void showBubble() {

        // Stop fishworld movements
        setCurrentState(EntityState.IDLE);

        // Show bubble relative to the position of the fishworld
        m_bubble = new BubbleEntity(m_fishContent, BUBBLE_OFFSET_X, BUBBLE_OFFSET_Y, BUBBLE_RADIUS,
                                    s_directionInvertedX);
        addChildEntity(m_bubble);

        // Start timer to automatically hide bubble after the given amount of
        // seconds
        m_bubbleTimer = new Timeline(
                new KeyFrame(Duration.seconds(BUBBLE_HIDE_SECONDS), new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        hideBubble();
                    }
                }));
        m_bubbleTimer.play();
    }

    void hideBubble() {

        if (m_bubble == null) {
            return;
        }

        removeNode(m_bubble);
        m_bubble = null;

        // Start fishworld movements
        setCurrentState(EntityState.MOVE);
    }

    void toggleBubble(Point2D absolutePosition, String interaction) {

        String intention = "N/A";

        switch (getCurrentState()) {
            case IDLE:
                hideBubble();
                intention = "hide bubble";
                break;
            case MOVE:
                showBubble();
                intention = "show bubble";
                break;
            default:
                hideBubble();
                break;
        }

        // Write to XLS Logger
        try {
            FishWorld fishWorld = (FishWorld) getWorld();
            XlsLogger xlsLogger = fishWorld.getXlsLogger();
            Point2D pos = getPosition();
            xlsLogger.writeData(
                    new XlsLoggerEntry(fishWorld.getAmountOfFishes(), new Date(), m_fishSprite.getSpriteImageName(), pos.getX(),
                                       pos.getY(), intention, interaction, absolutePosition.getX(),
                                       absolutePosition.getY(), m_fishContent));
        } catch (Exception e) {
            m_log.error(MarkerManager.getMarker("EXCEPTION"), "Error while closing XLS logger.", e);
        }
    }

    public void increaseSize(double deltaSize) {

        double scaleX = getScaleX();
        double scaleY = getScaleY();
        if (scaleX >= 2.0 || scaleY >= 2.0) {
            return;
        }
        setScaleX(scaleX + deltaSize);
        setScaleY(scaleY + deltaSize);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public CategoryType getType() {
        return m_fishSprite.getType();
    }

}
