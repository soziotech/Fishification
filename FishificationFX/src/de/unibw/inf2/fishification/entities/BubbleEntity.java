/*
 * BubbleEntity.java
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

package de.unibw.inf2.fishification.entities;

import de.unibw.inf2.fishification.data.ContentItem;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.entities.Entity;
import org.sociotech.unui.javafx.engine2d.entities.EntityState;

/**
 * The bubble created for every fish.
 *
 * @author Martin Burkhard
 */
public class BubbleEntity extends Entity {

    private static final double BUBBLE_OPACITY            = 0.3;
    private static final double BUBBLE_TEXT_WIDTH_FACTOR  = 1.5;
    private static final double BUBBLE_TEXT_HEIGHT_FACTOR = 1.15;
    private static final String BUBBLE_TEXT_FONT_TYPE     = "Comic Sans MS";
    private static final int    BUBBLE_TEXT_FONT_SIZE     = 12;
    private static final double SMALL_BUBBLE_OFFSET_X     = 60;
    private static final double SMALL_BUBBLE_OFFSET_Y     = 60;

    public BubbleEntity(ContentItem contentItem, double initialPosX, double initialPosY, double radius, boolean directionInvertedX) {

        double posX = initialPosX;

        // Large bubble for the contents
        Circle bigBubble = createBubbleCircle(radius);
        addNode(bigBubble);

        if (!directionInvertedX) {
            posX += SMALL_BUBBLE_OFFSET_X;
        }

        // Decorator bubble
        Circle smallBubble01 = createBubbleCircle(8);
        smallBubble01.setTranslateX(-4 + posX);
        if (!directionInvertedX) {
            smallBubble01.setTranslateX(4 + posX);
        }
        smallBubble01.setTranslateY(-6 + SMALL_BUBBLE_OFFSET_Y - initialPosY);
        addNode(smallBubble01);

        // Decorator bubble
        Circle smallBubble02 = createBubbleCircle(5);
        smallBubble02.setTranslateX(10 + posX);
        if (!directionInvertedX) {
            smallBubble02.setTranslateX(-10 + posX);
        }
        smallBubble02.setTranslateY(SMALL_BUBBLE_OFFSET_Y - initialPosY);
        addNode(smallBubble02);

        // Decorator bubble
        Circle smallBubble03 = createBubbleCircle(4);
        smallBubble03.setTranslateX(4 + posX);
        if (!directionInvertedX) {
            smallBubble03.setTranslateX(-4 + posX);
        }
        smallBubble03.setTranslateY(8 + SMALL_BUBBLE_OFFSET_Y - initialPosY);
        addNode(smallBubble03);

        // Set initial position
        setPosition(new Point2D(posX, initialPosY));

        // Show text in bubble
        double width = radius * BUBBLE_TEXT_WIDTH_FACTOR;
        double height = radius * BUBBLE_TEXT_HEIGHT_FACTOR;
        Text bubbleText = new Text();
        bubbleText.setText(contentItem.getFlagContent());
        bubbleText.setWrappingWidth(width);
        bubbleText.setTranslateX(-width / 2);
        bubbleText.setTranslateY(-height / 2);
        bubbleText.setFont(Font.font(BUBBLE_TEXT_FONT_TYPE, BUBBLE_TEXT_FONT_SIZE));
        bubbleText.setFill(Color.WHITE);
        bubbleText.setSmooth(true);
        bubbleText.setFontSmoothingType(FontSmoothingType.LCD);
        bubbleText.setTextOrigin(VPos.TOP);
        Rectangle clip = new Rectangle(width, height);
        bubbleText.setClip(clip);
        addNode(bubbleText);
    }

    private Circle createBubbleCircle(double radius) {
        CircleBuilder<?> circleBuilder = CircleBuilder.create();
        circleBuilder.radius(radius);
        circleBuilder.cache(true);
        Circle sphere = circleBuilder.build();
        sphere.setOpacity(BUBBLE_OPACITY);

        RadialGradientBuilder gradientBuilder = RadialGradientBuilder.create();
        gradientBuilder.centerX(sphere.getCenterX() - sphere.getRadius() / 3);
        gradientBuilder.centerY(sphere.getCenterY() - sphere.getRadius() / 3);
        gradientBuilder.radius(sphere.getRadius());
        gradientBuilder.proportional(false);
        gradientBuilder.stops(new Stop(0.0, Color.BLUE), new Stop(1.0, Color.BLACK));
        RadialGradient gradient = gradientBuilder.build();

        sphere.setFill(gradient);
        return sphere;
    }

    @Override
    protected void onInit(AbstractWorld world) {

        // Play Sound
        world.playAudioClip("bubble.wav", 0.5);

        // Everything starts moving
        setCurrentState(EntityState.MOVE);

    }

}
