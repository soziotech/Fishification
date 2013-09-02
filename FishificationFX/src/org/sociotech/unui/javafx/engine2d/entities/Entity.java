/*
 * Entity.java
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
 *     Date: 9/2/13 9:02 AM
 */

package org.sociotech.unui.javafx.engine2d.entities;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity extends Group {

    private AbstractWorld m_world;

    private EntityState m_currentState;
    private EntityState m_beforePauseState;
    private boolean m_initialized = false;

    private final List<Entity> m_childEntities = new ArrayList<Entity>();

    protected Entity() {

    }

    public void initialize(AbstractWorld world) {

        if (m_initialized) {
            return;
        }

        // Initialize Entity
        setCurrentState(EntityState.IDLE);

        // Assign world
        setWorld(world);

        // Call onInit of Entity
        onInit(world);

        // Init event handlers
        initEventHandlers();

        m_initialized = true;
    }

    public void update(Scene worldCanvas, EntityState entityState, double framesPerSecond) {

        onUpdate(worldCanvas, entityState, framesPerSecond);
        for (Entity childEntity : m_childEntities) {
            childEntity.update(worldCanvas, entityState, framesPerSecond);
        }
    }

    protected synchronized void destroy() {

        // Set destroy state
        setCurrentState(EntityState.DESTROYED);

        // Call destroy
        onDestroy();

        // Destroy child entities
        for (Entity childEntity : m_childEntities) {
            childEntity.destroy();
        }

        // Remove node group
        getWorld().getEntityManager().removeEntity(this);
    }

    // Entity phases
    protected void onInit(AbstractWorld world) {

    }

    protected void onUpdate(Scene worldCanvas, EntityState entityState, double framesPerSecond) {

    }

    protected void onPause(boolean isPaused, EntityState beforePauseState) {
    }

    protected void onDestroy() {
    }

    // Visibility
    protected void onShow() {
    }

    protected void onHide() {
    }

    // Input events
    protected boolean onMouseClickedEvent(MouseEvent mouseEvent) {
        return false;
    }

    protected boolean onMousePressedEvent(MouseEvent mouseEvent) {
        return false;
    }

    protected boolean onMouseReleasedEvent(MouseEvent mouseEvent) {
        return false;
    }

    protected boolean onMouseMovedEvent(MouseEvent mouseEvent) {
        return false;
    }

    protected boolean onMouseDraggedEvent(MouseEvent mouseEvent) {
        return false;
    }

    protected boolean onTouchPressedEvent(TouchEvent touchEvent) {
        return false;
    }

    protected boolean onTouchReleasedEvent(TouchEvent touchEvent) {
        return false;
    }

    protected boolean onTouchMovedEvent(TouchEvent touchEvent) {
        return false;
    }

    // Collision support
    public void onCollisionWith(Entity e) {
        setCurrentState(EntityState.COLLIDE);
    }

    public boolean isCollidable() {
        return false;
    }

    protected AbstractWorld getWorld() {
        return m_world;
    }

    private void setWorld(AbstractWorld world) {
        this.m_world = world;
    }

    public List<Entity> getChildEntities() {
        return m_childEntities;
    }

    private void initEventHandlers() {

        // Set MouseEvent Handler
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseClickedEvent(event);
            }
        });

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMousePressedEvent(event);
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseReleasedEvent(event);
            }
        });

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseDraggedEvent(event);
            }
        });

        setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseMovedEvent(event);
            }
        });

        // Set TouchEvent Handler
        setOnTouchPressed(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                onTouchPressedEvent(event);
            }
        });

        setOnTouchReleased(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                onTouchReleasedEvent(event);
            }
        });

        setOnTouchMoved(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                onTouchMovedEvent(event);
            }
        });
    }

    protected void addChildEntity(Entity childEntity) {

        childEntity.initialize(m_world);
        m_childEntities.add(childEntity);
        addNode(childEntity);
    }

    protected void addNode(Node node) {

        getChildren().add(node);
    }

    protected void removeNode(Node node) {
        getChildren().remove(node);
    }

    public EntityState getCurrentState() {
        return m_currentState;
    }

    public void setCurrentState(EntityState currentState) {
        setCurrentState(currentState, false);
    }

    protected void setCurrentState(EntityState currentState, boolean updateChildren) {
        m_currentState = currentState;
        if (!updateChildren) {
            return;
        }
        for (Entity childEntity : m_childEntities) {
            childEntity.setCurrentState(currentState);
        }
    }

    protected Point2D getPosition() {
        return new Point2D(getTranslateX(), getTranslateY());
    }

    protected void setPosition(Point2D position) {
        setTranslateX(position.getX());
        setTranslateY(position.getY());
    }

    protected void setPosition(double posX, double posY) {
        setTranslateX(posX);
        setTranslateY(posY);
    }

    public void pause(boolean isPaused) {

        if (isPaused) { // pauseAnimation
            m_beforePauseState = m_currentState; // save old state
            onPause(isPaused, m_currentState);
            m_currentState = EntityState.PAUSE;
        } else if (m_beforePauseState != null) { // resume
            onPause(isPaused, m_currentState);
            m_currentState = m_beforePauseState; // restore state
        }
    }

    public void hide() {
        setVisible(false);
        onHide();
    }

    public void show() {
        setVisible(true);
        onShow();
    }

    public void toggleVisibility() {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }
}
