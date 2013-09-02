/*
 * StageSingleton.java
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

package de.unibw.inf2.fishification;

import javafx.stage.Stage;

/**
 * Singleton for accessing JavaFX primary stage.
 *
 * @author Martin Burkhard
 */
final class StageSingleton {

    private static Stage s_stage = null;

    private StageSingleton() {
    }

    public static void injectStage(Stage stage) {
        s_stage = stage;
    }

    public static Stage getStage() {
        return s_stage;
    }
}
