/*
 * SyncedToggleGroup.java
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
 *     Date: 9/2/13 12:56 AM
 */

package org.sociotech.unui.javafx.engine2d.buttons;

import java.util.ArrayList;
import java.util.List;

/**
 * Ensures that all ToggleButtons will be toggled together.
 */
public class SyncedToggleGroup {

    private final List<SyncedToggleButton> m_toggleButtons = new ArrayList<SyncedToggleButton>();

    public SyncedToggleGroup() {

    }

    public synchronized void add(SyncedToggleButton syncedToggleButton) {
        m_toggleButtons.add(syncedToggleButton);
    }

    synchronized List<SyncedToggleButton> getToggleButtons() {

        return m_toggleButtons;
    }

    public void setSelected(boolean selected) {
        for (SyncedToggleButton button : getToggleButtons()) {
            button.setSelected(selected);
        }
    }
}
