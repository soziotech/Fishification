/*
 * CategoryMapper.java
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

package de.unibw.inf2.fishification.categories;

/**
 * Maps between text and type of fish.
 *
 * @author Martin Burkhard
 */
public final class CategoryMapper {

    private CategoryMapper() {

    }

    public static CategoryType map(String categoryName) {
        if (categoryName.equalsIgnoreCase("cscm")) {
            return CategoryType.CSCM;
        }
        if (categoryName.equalsIgnoreCase("mendeley")) {
            return CategoryType.MENDELEY;
        }
        if (categoryName.equalsIgnoreCase("sociotech")) {
            return CategoryType.SOCIOTECH;
        }
        if (categoryName.equalsIgnoreCase("studiendekan")) {
            return CategoryType.STUDIENDEKAN;
        }
        if (categoryName.equalsIgnoreCase("twitter")) {
            return CategoryType.TWITTER;
        }
        if (categoryName.equalsIgnoreCase("unibwm")) {
            return CategoryType.UNIBWM;
        }

        return CategoryType.NONE;
    }
}
