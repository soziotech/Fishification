/*
 * Sprite.java
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
 *     Date: 9/2/13 12:57 AM
 */

package org.sociotech.unui.javafx.engine2d.sprites;

import de.unibw.inf2.fishification.categories.CategoryType;
import javafx.animation.Animation;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.sociotech.unui.javafx.engine2d.AbstractWorld;

/**
 * Definition of a sprite represented by a 2D image.
 *
 * @author Martin Burkhard
 */
public class Sprite {

    private final String          m_spriteImageName;
    private final Image           m_spriteImage;
    private final ImageView       m_spriteView;
    private       SpriteAnimation m_spriteAnimation;

    private double m_spriteWidth;
    private double m_spriteHeight;
    private int    m_spriteCount;
    private int    m_imageColumns;
    private boolean m_autoReverse = false;

    private CategoryType m_type = CategoryType.NONE;

    private Sprite(String spriteImageName, AbstractWorld world) {
        m_spriteImageName = spriteImageName;
        m_spriteImage = world.getImageResourceManager().get(spriteImageName);

        m_spriteView = new ImageView();
        m_spriteView.setImage(m_spriteImage);
        m_spriteView.setCache(true);
        m_spriteView.setCacheHint(CacheHint.SPEED);
        m_spriteView.setManaged(false);

        m_imageColumns = 1;
        m_spriteCount = 1;
        m_spriteWidth = m_spriteImage.getWidth();
        m_spriteHeight = m_spriteImage.getHeight();
        m_spriteAnimation = null;
    }

    protected Sprite(String spriteImageName, AbstractWorld world, CategoryType type, int spriteWidth, int spriteHeight, int spriteCount, int imageColumns) {

        this(spriteImageName, world);

        m_type = type;
        m_spriteCount = spriteCount;
        m_imageColumns = imageColumns;

        if (m_spriteCount < 1) {
            m_spriteCount = 1;
        }
        if (m_imageColumns < 1) {
            m_imageColumns = 1;
        }

        int imageLines = Math.round(m_spriteCount / m_imageColumns);
        m_spriteWidth = m_spriteImage.getWidth() / m_imageColumns;
        m_spriteHeight = m_spriteImage.getHeight() / imageLines;
        m_spriteAnimation = new SpriteAnimation(m_spriteView, spriteWidth, spriteHeight, m_spriteCount, m_imageColumns);

    }

    protected Sprite(String spriteImageName, AbstractWorld world, CategoryType type, int spriteWidth, int spriteHeight, int spriteCount, int imageColumns, boolean autoReverse) {
        this(spriteImageName, world, type, spriteWidth, spriteHeight, spriteCount, imageColumns);
        m_autoReverse = autoReverse;
    }

    public void startAnimation(Duration animationSpeed) {
        if (m_spriteAnimation == null) {
            return;
        }
        m_spriteAnimation.setAnimationSpeed(animationSpeed);
        m_spriteAnimation.setCycleCount(Animation.INDEFINITE);
        m_spriteAnimation.setAutoReverse(m_autoReverse);
        m_spriteAnimation.play();
    }

    public void pauseAnimation(boolean isPaused) {
        if (m_spriteAnimation == null) {
            return;
        }
        if (isPaused) {
            m_spriteAnimation.pause();
        } else {
            m_spriteAnimation.play();
        }
    }

    public String getSpriteImageName() {
        return m_spriteImageName;
    }

    public double getWidth() {
        return m_spriteWidth;
    }

    public double getHeight() {
        return m_spriteHeight;
    }

    public ImageView getSpriteView() {
        return m_spriteView;
    }

    public double getRotate() {
        return m_spriteView.getRotate();
    }

    public void setRotate(double d) {
        m_spriteView.setRotate(d);
    }

    // TODO check, if it does work correctly
    public void flipX() {
        m_spriteView.setScaleX(m_spriteView.getScaleX() * -1);
    }

    // TODO check, if it does work correctly
    public void flipY() {
        m_spriteView.setScaleY(m_spriteView.getScaleY() * -1);
    }

    public void setVisible(boolean b) {
        m_spriteView.setVisible(b);
    }

    public CategoryType getType() {
        return m_type;
    }
}
