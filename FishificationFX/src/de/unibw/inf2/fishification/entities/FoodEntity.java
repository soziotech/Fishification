/*
 * FoodEntity.java
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

import javafx.scene.Scene;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.entities.Entity;
import org.sociotech.unui.javafx.engine2d.entities.EntityState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Generates random fish food items appearing on top of the screen.
 *
 * @author Martin Burkhard
 */
public class FoodEntity extends Entity {

    private final double m_initialPosX;
    private final double m_initialPosY;
    private final String m_foodSource;

    private final List<FoodItemEntity> m_foodItemEntities = new ArrayList<FoodItemEntity>();

    public FoodEntity(String source, double initialPosX, double initialPosY) {
        m_initialPosX = initialPosX;
        m_initialPosY = initialPosY;
        m_foodSource = source;

        // Set position of food
        setPosition(initialPosX, initialPosY);
    }

    @Override
    protected void onInit(AbstractWorld world) {

        // Generate new random number
        Random rand = new Random();
        rand.setSeed(Calendar.getInstance().getTimeInMillis());

        for (int i = 0; i < rand.nextInt(10) + 5; i++) {

            FoodItemEntity foodItemEntity = new FoodItemEntity(m_foodSource, m_initialPosX, m_initialPosY);
            m_foodItemEntities.add(foodItemEntity);
            addChildEntity(foodItemEntity);
        }

        // Everything starts moving
        setCurrentState(EntityState.MOVE, true);
    }

    @Override
    protected void onUpdate(Scene worldCanvas, EntityState entityState, double framesPerSecond) {

        if (getCurrentState() == EntityState.MOVE) {
            for (FoodItemEntity foodItemEntity : m_foodItemEntities) {
                if (foodItemEntity.getCurrentState() == EntityState.DESTROYED) {
                    destroy();
                }
            }
        }

    }

}
