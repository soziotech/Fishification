/*
 * FishFoodBoxes.java
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

/**
 * Utility class listing all fish food box drawables.
 */
public final class FishFoodBoxes {

    private FishFoodBoxes() {
    }

    private static Integer[] s_imageList = null;

    public static Integer[] getImageList() {

        if (s_imageList == null) {
            s_imageList = new Integer[]{R.drawable.foodbox_cscm, R.drawable.foodbox_mendeley, R.drawable.foodbox_sociotech, R.drawable.foodbox_studiendekan, R.drawable.foodbox_twitter, R.drawable.foodbox_unibwm};
        }

        return s_imageList;
    }

    public static String getFoodBoxNameByIndex(int index) {
        switch (index) {
            case 0:
                return "cscm";
            case 1:
                return "mendeley";
            case 2:
                return "sociotech";
            case 3:
                return "studiendekan";
            case 4:
                return "twitter";
            case 5:
                return "unibwm";
        }
        return null;
    }
}
