/*
 * FishWorldFactory.java
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

package de.unibw.inf2.fishification.server.fishworld;

import com.google.inject.Singleton;
import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.data.ContentItem;
import de.unibw.inf2.fishification.entities.EntityFactory;
import de.unibw.inf2.fishification.entities.FishEntity;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.sociotech.communitymashup.data.Category;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Creates server-side FishWorld JavaFX application.
 *
 * @author Martin Burkhard
 */
@ThreadSafe
@Singleton  // Important: otherwise several FishWorlds will be generated
public class FishWorldFactory {

    private final FishWorld m_fishWorld;

    @Inject
    FishWorldFactory(@Nonnull FishWorld fishWorld) {

        m_fishWorld = fishWorld;

        // Get screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        m_fishWorld.initialize(screenBounds.getWidth(), screenBounds.getHeight());

        // Launches JavaFX application
        m_fishWorld.launch();
    }

    public boolean addFish(String fishName, String spriteName, String title, String stringValue, String authorName) {

        final ContentItem fishContent = new ContentItem(fishName, title, stringValue, new ArrayList<Category>(),
                                                        authorName, null);

        final FishEntity fishEntity = EntityFactory.createFishEntity(spriteName, fishContent, m_fishWorld);
        return m_fishWorld.addFishEntity(fishEntity);
    }

    public boolean feedFish(String source) {
        return m_fishWorld.feedFish(source);
    }
}
