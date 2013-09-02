/*
 * CategoryButton.java
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
 *     Date: 9/2/13 12:58 AM
 */

package de.unibw.inf2.fishification.buttons;

import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.categories.CategoryType;
import de.unibw.inf2.fishification.entities.FishEntity;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import org.sociotech.unui.javafx.engine2d.buttons.SyncedToggleButton;
import org.sociotech.unui.javafx.engine2d.entities.Entity;

public class CategoryButton extends SyncedToggleButton implements EventHandler<Event> {

    private final CategoryType m_categoryType;
    private final FishWorld    m_fishWorld;

    public CategoryButton(CategoryType categoryType, String title, ImageView imageView, String buttonStyle, FishWorld fishWorld) {
        super(title, imageView);
        m_categoryType = categoryType;
        m_fishWorld = fishWorld;

        // Set handler
        setOnMousePressed(this);

        // Set style
        setStyle(buttonStyle);
        setAlignment(Pos.CENTER_LEFT);
        setMaxWidth(Double.MAX_VALUE);
        setSelected(true);
    }

    @Override
    public void handle(Event event) {

        for (Entity entity : m_fishWorld.getEntityManager().getEntities()) {
            if (entity instanceof FishEntity) {
                FishEntity fishEntity = (FishEntity) entity;

                if (fishEntity.getType() == m_categoryType) {
                    fishEntity.toggleVisibility();
                }
            }
        }
    }
}
