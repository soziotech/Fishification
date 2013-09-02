/*
 * RefreshButton.java
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

package de.unibw.inf2.fishification.buttons;

import de.unibw.inf2.fishification.FishWorld;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;

/**
 * Refreshes data.
 */
public class RefreshButton extends Button implements EventHandler<Event> {

    private final FishWorld m_fishWorld;

    public RefreshButton(String title, String buttonStyle, double width, double height, FishWorld fishWorld) {
        super(title);
        m_fishWorld = fishWorld;

        // Set handler
        setOnMousePressed(this);

        // Set style
        setStyle(buttonStyle);
        setPrefSize(width, height);
    }

    @Override
    public void handle(Event event) {

        EventType eventType = event.getEventType();
        if (eventType != MouseEvent.MOUSE_PRESSED && eventType != TouchEvent.TOUCH_PRESSED) {
            return;
        }

        // remove contents
        m_fishWorld.clearAll();

        // load contents
        m_fishWorld.loadFishContents();
    }
}
