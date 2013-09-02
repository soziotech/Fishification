/*
 * EntityFactory.java
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
 *     Date: 9/2/13 12:55 AM
 */

package de.unibw.inf2.fishification.entities;

import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.data.ContentItem;
import de.unibw.inf2.fishification.sprites.ChristmasFish;
import de.unibw.inf2.fishification.sprites.FishSpriteFactory;
import javafx.geometry.Point2D;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.sprites.Sprite;
import org.sociotech.unui.javafx.engine2d.util.RandomFactory;

import java.util.Calendar;
import java.util.Random;

/**
 * Factory for creating entities.
 *
 * @author Martin Burkhard
 */
public final class EntityFactory {

    // the offset of fishes to the top, bottom and border of the screen
    private static final double FISH_TOP_OFFSET    = 200.0;
    private static final double FISH_BOTTOM_OFFSET = 100.0;
    private static final double FISH_BORDER_OFFSET = 200.0;

    private EntityFactory() {

    }

    public static FishEntity createChristmasFish(FishWorld world) {
        ContentItem christmasContent = new ContentItem("xmas", "Frohe Weihnachten & einen guten Start ins neue Jahr",
                                                       "Liebe Professorinnen und Professoren, Liebe  Mitarbeiterinnen und Mitarbeiter, Liebe Studentinnen und Studenten, wir wünschen Allen ein Frohes Weihnachtsfest, und erholsame Tage und einen guten Start ins Neues Jahr. Ihre Sonja Maier, Andrea Nutsi, Peter Lachenmaier & Martin Burkhard",
                                                       null, "Martin Burkhard", "");
        return createFishEntity(new ChristmasFish(world), christmasContent, world);
    }

    public static FishEntity createFishEntity(String fishSpriteName, ContentItem fishContent, FishWorld fishWorld) {
        final Sprite fishSprite = FishSpriteFactory.createFishByName(fishSpriteName, fishWorld);
        return createFishEntity(fishSprite, fishContent, fishWorld);
    }

    public static FishEntity createFishEntity(ContentItem fishContent, FishWorld fishWorld) {

        // Get random fish sprite
        Sprite fishSprite = FishSpriteFactory.getFishSprite(fishContent, fishWorld);

        // Return entity
        return createFishEntity(fishSprite, fishContent, fishWorld);
    }

    private static FishEntity createFishEntity(Sprite fishSprite, ContentItem fishContent, FishWorld world) {

        // Define inner screen area
        double innerWidth = world.getWidth() - FISH_TOP_OFFSET - FISH_BORDER_OFFSET;
        double innerHeight = world.getHeight() - FISH_BOTTOM_OFFSET - FISH_BORDER_OFFSET;

        Point2D randomPos = RandomFactory.getRandomPosition(innerWidth, innerHeight);
        return new FishEntity(fishContent, fishSprite, FISH_BORDER_OFFSET + randomPos.getX(),
                              FISH_BORDER_OFFSET + randomPos.getY());
    }

    public static FoodEntity createFoodEntity(String source, AbstractWorld world) {

        // Define screen offsets
        int screenOffset = 400;
        int screenOffsetLeft = 200;
        int screenTopOffset = 50;

        // Define inner screen area
        int innerWidth = (int) world.getWidth() - screenOffset;

        // Generate new random number
        Random rand = new Random();
        rand.setSeed(Calendar.getInstance().getTimeInMillis());

        int randomPosX = RandomFactory.getRandomInt(innerWidth);
        return new FoodEntity(source, randomPosX + screenOffsetLeft, screenTopOffset);
    }

}
