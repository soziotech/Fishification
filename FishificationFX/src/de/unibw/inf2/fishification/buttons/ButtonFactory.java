/*
 * ButtonFactory.java
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
 *     Date: 9/2/13 8:16 AM
 */

package de.unibw.inf2.fishification.buttons;

import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.categories.CategoryType;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

class ButtonFactory {

    private final FishWorld m_fishWorld;
    private final String    m_buttonStyle;

    // Toggle Buttons
    private final double m_buttonWidth;
    private final double m_buttonHeight;

    public ButtonFactory(FishWorld fishWorld, String buttonStyle, double buttonWidth, double buttonHeight) {
        m_fishWorld = fishWorld;
        m_buttonStyle = buttonStyle;
        m_buttonWidth = buttonWidth;
        m_buttonHeight = buttonHeight;
    }

    public CategoryButton createCategoryButton(CategoryType categoryType, String title, String imageName) {

        // Button Image
        Image image = m_fishWorld.getImageResourceManager().get(imageName);
        ImageView imageView = new ImageView(image);
        imageView.setSmooth(true);

        return new CategoryButton(categoryType, title, imageView, m_buttonStyle, m_fishWorld);
    }

    public CentricButton createCentricButton(String title) {
        return new CentricButton(title, m_buttonStyle, m_buttonWidth, m_buttonHeight, m_fishWorld);
    }

    public PauseButton createPauseButton(String title, String toggleTitle, List<Control> controlList) {
        return new PauseButton(title, toggleTitle, m_buttonStyle, m_buttonWidth, m_buttonHeight, controlList,
                               m_fishWorld);
    }

    public RefreshButton createRefreshButton(String title) {
        return new RefreshButton(title, m_buttonStyle, m_buttonWidth, m_buttonHeight, m_fishWorld);
    }
}
