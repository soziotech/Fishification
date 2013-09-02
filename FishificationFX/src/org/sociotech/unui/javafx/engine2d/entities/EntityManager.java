/*
 * EntityManager.java
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
 *     Date: 9/2/13 8:47 AM
 */

package org.sociotech.unui.javafx.engine2d.entities;

import javafx.scene.Group;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Event handler for button events.
 *
 * @author Martin Burkhard
 */
public class EntityManager {

    private final List<Entity> m_entityList  = new ArrayList<Entity>();
    private final List<Entity> m_destroyList = new ArrayList<Entity>();
    private final AbstractWorld m_world;
    private final Group         m_worldNodes;

    public EntityManager(AbstractWorld world) {
        m_world = world;
        m_worldNodes = world.getWorldNodes();
    }

    public synchronized void addEntity(Entity entity) {

        // Add Entity to internal update list
        m_entityList.add(entity);

        // Add node group to world nodes
        m_worldNodes.getChildren().add(entity);

        // Initialize the entity
        entity.initialize(m_world);
    }

    public synchronized void pauseEntities(boolean isPaused) {
        for (Entity e : getEntities()) {
            e.pause(isPaused);
        }
    }

    public synchronized void removeEntity(Entity entity) {

        if (entity instanceof StaticEntity) {
            return;
        }

        m_entityList.remove(entity);
        m_worldNodes.getChildren().remove(entity);
        entity.setCurrentState(EntityState.DESTROYED);
    }

    public synchronized List<Entity> getEntities() {
        return m_entityList;
    }

    public synchronized void clear() {
        for (Entity e : new ArrayList<Entity>(m_entityList)) {

            removeEntity(e);
        }
    }

    public synchronized void destroy() {
        for (Entity e : new ArrayList<Entity>(m_destroyList)) {
            m_entityList.remove(e);
            e.setCurrentState(EntityState.DESTROYED);
        }
        m_destroyList.clear();
    }
}
