/*
 * FoodItemEntity.java
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

import de.unibw.inf2.fishification.FishWorld;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.entities.Entity;
import org.sociotech.unui.javafx.engine2d.entities.EntityState;

import java.util.Calendar;
import java.util.Random;

/**
 * Random fish food item as part of FoodEntity.
 *
 * @author Martin Burkhard
 */
public class FoodItemEntity extends Entity {

    // Constants
    private static final double FOOD_OPACITY      = 0.8;
    private static final double FOOD_BASE_SPEED_X = 0.12;
    private static final double FOOD_BASE_SPEED_Y = 0.1;
    private static final double VAR_SPEED_X       = 0.03;
    private static final double VAR_SPEED_Y       = 0.03;

    private final Circle m_food;
    private final String m_foodSource;
    private double m_speedX = 0.0;
    private double m_speedY = 0.0;

    // Constructor
    FoodItemEntity(String source, double initialPosX, double initialPosY) {
        m_foodSource = source;

        // Generate new random number
        Random rand = new Random();
        rand.setSeed(Calendar.getInstance().getTimeInMillis());

        m_food = createFoodCircle(3 + rand.nextInt(2));
        m_food.setTranslateX(initialPosX);
        m_food.setTranslateY(initialPosY);

        addNode(m_food);
    }

    private Circle createFoodCircle(double radius) {
        CircleBuilder<?> circleBuilder = CircleBuilder.create();
        circleBuilder.radius(radius);
        circleBuilder.cache(true);
        Circle sphere = circleBuilder.build();
        sphere.setOpacity(FOOD_OPACITY);

        RadialGradientBuilder gradientBuilder = RadialGradientBuilder.create();
        gradientBuilder.centerX(sphere.getCenterX() - sphere.getRadius() / 3);
        gradientBuilder.centerY(sphere.getCenterY() - sphere.getRadius() / 3);
        gradientBuilder.radius(sphere.getRadius());
        gradientBuilder.proportional(false);

        if (m_foodSource.equalsIgnoreCase("twitter")) {
            gradientBuilder.stops(new Stop(0.0, Color.LIGHTCYAN), new Stop(1.0, Color.DARKCYAN));
        } else if (m_foodSource.equalsIgnoreCase("sociotech")) {
            gradientBuilder.stops(new Stop(0.0, Color.GRAY), new Stop(1.0, Color.DARKGRAY));
        } else if (m_foodSource.equalsIgnoreCase("cscm")) {
            gradientBuilder.stops(new Stop(0.4, Color.ORANGE), new Stop(1.0, Color.BLACK));
        } else if (m_foodSource.equalsIgnoreCase("unibwm")) {
            gradientBuilder.stops(new Stop(0.0, Color.DARKORANGE), new Stop(1.0, Color.BLACK));
        } else if (m_foodSource.equalsIgnoreCase("mendeley")) {
            gradientBuilder.stops(new Stop(0.0, Color.RED), new Stop(1.0, Color.BLACK));
        } else if (m_foodSource.equalsIgnoreCase("studiendekan")) {
            gradientBuilder.stops(new Stop(0.0, Color.SANDYBROWN), new Stop(1.0, Color.BLACK));
        } else {
            gradientBuilder.stops(new Stop(0.0, Color.YELLOW), new Stop(1.0, Color.BLACK));
        }
        RadialGradient gradient = gradientBuilder.build();

        sphere.setFill(gradient);
        return sphere;
    }

    @Override
    protected void onInit(AbstractWorld world) {
        // Generate new random number
        Random rand = new Random();
        rand.setSeed(Calendar.getInstance().getTimeInMillis());

        // Random initial position
        double posX = 0.0;
        if (rand.nextBoolean()) {
            posX += rand.nextDouble() * 20;
        } else {
            posX -= rand.nextDouble() * 20;
        }
        double posY = (double) 5 + rand.nextDouble();
        if (rand.nextBoolean()) {
            posY *= -1;
        }
        m_food.setTranslateX(posX);
        m_food.setTranslateY(posY);

        // Random speed
        m_speedX = FOOD_BASE_SPEED_X + rand.nextDouble() * VAR_SPEED_X;
        if (rand.nextBoolean()) {
            m_speedX *= -1;
        }
        m_speedY = FOOD_BASE_SPEED_Y + rand.nextDouble() * VAR_SPEED_Y;
    }

    @Override
    protected void onUpdate(Scene worldCanvas, EntityState entityState, double framesPerSecond) {
        if (entityState == EntityState.MOVE) {

            // Move in X-Direction
            double newPosX = m_food.getTranslateX() + m_speedX;
            double newPosY = m_food.getTranslateY() + m_speedY;

            if (newPosY <= 100) {
                setPosition(newPosX, newPosY);
            } else {
                destroy();
            }

            m_food.setTranslateX(newPosX);
            m_food.setTranslateY(newPosY);
        }
    }

    @Override
    protected void onDestroy() {
        removeNode(m_food);

        // Increase fish
        ((FishWorld) getWorld()).increaseFishSize(m_foodSource);
    }

}