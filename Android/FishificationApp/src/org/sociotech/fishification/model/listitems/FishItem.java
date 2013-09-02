/*
 * FishItem.java
 *
 * Copyright (c) 2013 Martin Burkhard.
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
 *     Date: 8/30/13 7:46 PM
 */

package org.sociotech.fishification.model.listitems;

/**
 * Single fish specifying name, type and image.
 */
public class FishItem {

    private final String m_fishName;
    private final String m_fishType;
    private final int    m_fishImageResourceId;

    public FishItem(String fishName, String fishType, int fishImageResourceId) {
        m_fishName = fishName;
        m_fishType = fishType;
        m_fishImageResourceId = fishImageResourceId;
    }

    public String getFishName() {
        return m_fishName;
    }

    public String getFishType() {
        return m_fishType;
    }

    public int getImageResourceId() {
        return m_fishImageResourceId;
    }

    @Override
    public String toString() {
        return m_fishName;
    }
}
