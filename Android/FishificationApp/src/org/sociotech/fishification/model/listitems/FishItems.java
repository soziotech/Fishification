/*
 * FishItems.java
 *
 * Copyright (c) 2013 Martin Burkhard.
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
 *     Date: 8/30/13 7:46 PM
 */

package org.sociotech.fishification.model.listitems;

import org.sociotech.fishification.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class listing all FishItems.
 */
public final class FishItems {

    private FishItems() {
    }

    private static List<FishItem> s_fishItems = null;

    public static List<FishItem> getFishItems() {

        if (s_fishItems == null) {
            s_fishItems = new ArrayList<FishItem>();
            s_fishItems.add(new FishItem("Burbot", "Burbot", R.drawable.burbot));
            s_fishItems.add(new FishItem("Glasses Fish", "GlassesFish", R.drawable.glasses_fish));
            s_fishItems.add(new FishItem("Koffer Fish", "KofferFish", R.drawable.koffer_fish));
            s_fishItems.add(new FishItem("Lady Fish", "Lady Fish", R.drawable.lady_fish));
            s_fishItems.add(new FishItem("Mark the Shark", "MarkTheShark", R.drawable.mark_the_shark));
            s_fishItems.add(new FishItem("Octopus", "Octopus", R.drawable.octopus));
            s_fishItems.add(new FishItem("Pinky", "Pinky", R.drawable.pinky));
            s_fishItems.add(new FishItem("Red Fish", "RedFish", R.drawable.red_fish));
            s_fishItems.add(new FishItem("Seahorse", "Seahorse", R.drawable.seahorse));
        }
        return s_fishItems;
    }

    public static String getFishTypeNameByIndex(int index) {
        List<FishItem> items = getFishItems();
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(index).getFishType();
    }
}
