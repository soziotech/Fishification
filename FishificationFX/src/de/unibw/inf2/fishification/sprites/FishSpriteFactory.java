/*
 * FishSpriteFactory.java
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
 *     Date: 9/2/13 8:46 AM
 */

package de.unibw.inf2.fishification.sprites;

import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.categories.CategoryMapper;
import de.unibw.inf2.fishification.categories.CategoryType;
import de.unibw.inf2.fishification.categories.PersonType;
import de.unibw.inf2.fishification.data.ContentItem;
import org.sociotech.communitymashup.data.Category;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;
import org.sociotech.unui.javafx.engine2d.sprites.Sprite;

import java.util.List;

/**
 * Factory for creating Fish sprites.
 *
 * @author Martin Burkhard
 */
public final class FishSpriteFactory {

    private FishSpriteFactory() {

    }

    public static Sprite createFishByName(String fishType, AbstractWorld fishWorld) {

        if (fishType.equalsIgnoreCase("Burbot")) {
            return new Burbot(fishWorld);
        } else if (fishType.equalsIgnoreCase("ChristmasFish")) {
            return new ChristmasFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("CscmFish")) {
            return new CscmFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("GlassesFish")) {
            return new GlassesFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("KofferFish")) {
            return new KofferFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("LadyFish")) {
            return new LadyFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("MarkTheShark")) {
            return new MarkTheShark(fishWorld);
        } else if (fishType.equalsIgnoreCase("MendeleyFish")) {
            return new MendeleyFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("Octopus")) {
            return new Octopus(fishWorld);
        } else if (fishType.equalsIgnoreCase("Pinky")) {
            return new Pinky(fishWorld);
        } else if (fishType.equalsIgnoreCase("RedFish")) {
            return new RedFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("Seahorse")) {
            return new Seahorse(fishWorld);
        } else if (fishType.equalsIgnoreCase("SociotechFish")) {
            return new SociotechFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("StudiendekanFish")) {
            return new StudiendekanFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("TwitterFish")) {
            return new TwitterFish(fishWorld);
        } else if (fishType.equalsIgnoreCase("UnibwmFish")) {
            return new UnibwmFish(fishWorld);
        }
        return null;
    }

    private static Sprite createFishByCategory(CategoryType type, AbstractWorld fishWorld) {
        switch (type) {
            case BURBOT:
                return new Burbot(fishWorld);
            case CHRISTMAS:
                return new ChristmasFish(fishWorld);
            case CSCM:
                return new CscmFish(fishWorld);
            case GLASSES:
                return new GlassesFish(fishWorld);
            case KOFFER:
                return new KofferFish(fishWorld);
            case LADY:
                return new LadyFish(fishWorld);
            case MARKTHESHARK:
                return new MarkTheShark(fishWorld);
            case MENDELEY:
                return new MendeleyFish(fishWorld);
            case OCTOPUS:
                return new Octopus(fishWorld);
            case PINKY:
                return new Pinky(fishWorld);
            case RED:
                return new RedFish(fishWorld);
            case SOCIOTECH:
                return new SociotechFish(fishWorld);
            case STUDIENDEKAN:
                return new StudiendekanFish(fishWorld);
            case TWITTER:
                return new TwitterFish(fishWorld);
            case UNIBWM:
                return new UnibwmFish(fishWorld);
            case SEAHORSE:
                return new Seahorse(fishWorld);
        }
        return null;
    }

    public static Sprite getFishSprite(ContentItem fishContent, FishWorld fishWorld) {
        if (fishWorld.isPersonCentric()) {
            return getFishSpritePerson(fishContent, fishWorld);
        }
        return getFishSpriteSource(fishContent, fishWorld);
    }

    private static Sprite getFishSpriteSource(ContentItem fishContent, AbstractWorld fishWorld) {

        if (fishContent == null) {
            return null;
        }
        List<Category> categories = fishContent.getCategories();

        // Map fishworld related to given category types
        Sprite fishSprite = null;
        for (Category c : categories) {

            // Get category name
            String categoryName = c.getName();

            // Map category name to corresponding type
            CategoryType type = CategoryMapper.map(categoryName);
            if (type == CategoryType.NONE) {
                continue;
            }

            // Create fish sprite by given category
            fishSprite = FishSpriteFactory.createFishByCategory(type, fishWorld);
            if (fishSprite != null) {
                break;
            }
        }

        // Return fish sprite in case it's defined for given category
        return fishSprite;
    }

    private static Sprite getFishSpritePerson(ContentItem fishContent, AbstractWorld fishWorld) {

        String name = fishContent.getAuthorName();

        int index = -1;
        for (String s : PersonType.getPersons()) {
            if (s.equals(name) || name.contains(s) || s.contains(name)) {
                index = PersonType.indexOf(s);
            }
        }

        if (index == -1) {
            index = PersonType.numberOfPersons();
            PersonType.addPerson(name);
        }

        switch (index) {
            case 0:
                return new LadyFish(fishWorld);
            case 1:
                return new GlassesFish(fishWorld);
            case 2:
                return new MarkTheShark(fishWorld);
            case 3:
                return new Octopus(fishWorld);
            case 4:
                return new Pinky(fishWorld);
            case 5:
                return new Seahorse(fishWorld);
            case 6:
                return new RedFish(fishWorld);
            case 7:
                return new KofferFish(fishWorld);
            case 8:
                return new Burbot(fishWorld);
            case 9:
                return new UnibwmFish(fishWorld);
            case 10:
                return new CscmFish(fishWorld);
            case 11:
                return new StudiendekanFish(fishWorld);
            case 12:
                return new SociotechFish(fishWorld);
            case 13:
                return new TwitterFish(fishWorld);
            case 14:
                return new MendeleyFish(fishWorld);
        }
        return new ChristmasFish(fishWorld);
    }
}
