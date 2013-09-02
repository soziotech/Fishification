/*
 * PauseButton.java
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
 *     Date: 9/2/13 11:00 AM
 */

package de.unibw.inf2.fishification.buttons;

import de.unibw.inf2.fishification.FishWorld;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;

import java.util.List;

public class PauseButton extends ToggleButton implements EventHandler<Event> {

   private final String        m_title;
    private final String        m_toggleTitle;
    private final List<Control> m_controlList;
    private final FishWorld     m_fishWorld;

    public PauseButton(String title, String toggleTitle, String buttonStyle, double width, double height, List<Control> controlList, FishWorld world) {
        super(title);
        m_title = title;
        m_toggleTitle = toggleTitle;
        m_controlList = controlList;
        m_fishWorld = world;

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

        // Set pause or resume state for entities
        boolean paused = m_fishWorld.togglePause();


        if (paused) {

            // disable buttons during pause
            for (Control c : m_controlList) {
                c.setDisable(true);
            }

            // rename pause button
            setText(m_toggleTitle);

        } else {

            // enable buttons again
            for (Control c : m_controlList) {
                c.setDisable(false);
                if (c instanceof CentricButton) {
                    CentricButton centric = (CentricButton) c;
                    if (!m_fishWorld.isPersonCentric() && centric.getText().contains(
                            "source") || m_fishWorld.isPersonCentric() && centric.getText().contains("person")) {
                        centric.setDisable(true);
                    }
                }

            }

            // rename pause button
            setText(m_title);

            // Toggle pause button state
            setFocused(false);

            // Create Screenshot
            m_fishWorld.asyncTakeScreenshot();
        }
    }

}
