/*
 * SyncedToggleButton.java
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
 *     Date: 9/2/13 8:50 AM
 */

package org.sociotech.unui.javafx.engine2d.buttons;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;

public class SyncedToggleButton extends ToggleButton {

    private SyncedToggleGroup m_syncedToggleGroup = null;

    public SyncedToggleButton(String title) {
        super(title);
        addSelectedListener();
    }

    protected SyncedToggleButton(String title, ImageView imageView) {
        super(title, imageView);
        addSelectedListener();
    }

    public void setSyncedToggleGroup(SyncedToggleGroup syncedToggleGroup) {
        m_syncedToggleGroup = syncedToggleGroup;
        syncedToggleGroup.add(this);
    }

    private void addSelectedListener() {
        selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldSelected, Boolean newSelected) {
                if (m_syncedToggleGroup != null) {
                    m_syncedToggleGroup.setSelected(newSelected);
                }
            }
        });
    }
}
