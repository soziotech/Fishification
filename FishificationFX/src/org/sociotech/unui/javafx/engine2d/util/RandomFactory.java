/*
 * RandomFactory.java
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

package org.sociotech.unui.javafx.engine2d.util;

import javafx.geometry.Point2D;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates random values or value sets.
 *
 * @author Martin Burkhard
 */
public final class RandomFactory {

    private RandomFactory() {

    }

    public static boolean getRandomBoolean() {
        Random rand = getRandomInstance();
        return rand.nextBoolean();
    }

    public static int getRandomInt(int n) {
        Random rand = getRandomInstance();
        return rand.nextInt(n);
    }

    public static Point2D getRandomPosition(int width, int height) {

        return new Point2D(getRandomInt(width), getRandomInt(height));
    }

    public static double getRandomDouble() {
        Random rand = getRandomInstance();
        return rand.nextDouble();
    }

    public static Point2D getRandomPosition(double width, double height) {

        return new Point2D(getRandomDouble() * width, getRandomDouble() * height);
    }

    /**
     * Uses SecureRandom to shuffle collection.
     *
     * @param list Collection to be shuffled
     */
    public static void shuffle(List<?> list) {
        Collections.shuffle(list, getRandomInstance());
    }

    private static Random getRandomInstance() {

        try {
            // Generate new random number using Secure Random
            return SecureRandom.getInstance("SHA1PRNG");

        } catch (NoSuchAlgorithmException e) {
            // Generate new random number using Default Random
            Random rand = new Random();
            rand.setSeed(Calendar.getInstance().getTimeInMillis());
            return rand;
        }

    }
}
