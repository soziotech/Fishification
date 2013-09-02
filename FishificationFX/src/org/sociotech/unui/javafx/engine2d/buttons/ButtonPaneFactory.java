/*
 * ButtonPaneFactory.java
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
 *     Date: 9/2/13 8:52 AM
 */

package org.sociotech.unui.javafx.engine2d.buttons;

import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.layout.TilePane;

import java.util.List;

public final class ButtonPaneFactory {

    private ButtonPaneFactory() {
    }

    public static <T extends Control> TilePane createTilePane(int spacing, List<T> controlList, Orientation orientation) {

        TilePane tilePane = new TilePane();
        tilePane.setVgap(spacing);
        tilePane.setHgap(spacing);
        tilePane.setLayoutX(spacing);
        tilePane.setLayoutY(spacing);
        tilePane.setPrefColumns(controlList.size());
        tilePane.setOrientation(orientation);
        tilePane.getChildren().addAll(controlList);
        return tilePane;
    }

}
