/*
 * SpriteAnimation.java
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

package org.sociotech.unui.javafx.engine2d.sprites;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * An animated sprite using JavaFx's Transition based animation.
 *
 * @author Martin Burkhard
 *         Source: http://www.javacodegeeks.com/2012/03/javafx-creating-sprite-animation.html
 */
class SpriteAnimation extends Transition {

    private final ImageView m_spriteView;
    private final int       m_spriteCount;
    private final int       m_imageColumns;
    private final int       m_spriteWidth;
    private final int       m_spriteHeight;

    public SpriteAnimation(ImageView imageView, int spriteWidth, int spriteHeight, int spriteCount, int imageColumns) {

        this.m_spriteView = imageView;
        this.m_spriteWidth = spriteWidth;
        this.m_spriteHeight = spriteHeight;
        this.m_spriteCount = spriteCount;
        this.m_imageColumns = imageColumns;

        setInterpolator(Interpolator.LINEAR);
    }

    public void setAnimationSpeed(Duration animationSpeed) {
        setCycleDuration(animationSpeed);
    }

    protected void interpolate(double k) {

        final int index = Math.min((int) Math.floor(k * m_spriteCount), m_spriteCount - 1);
        int x = (index % m_imageColumns) * m_spriteWidth;
        int y = (index / m_imageColumns) * m_spriteHeight;
        if (x > 0) {
            x++;
        }
        if (y > 0) {
            y++;
        }

        // Sets the viewport to the next sprite image tile at x-y-position
        m_spriteView.setViewport(new Rectangle2D(x, y, m_spriteWidth, m_spriteHeight));
    }
}

