/*
 * Fish.java
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
 *     Date: 9/2/13 12:55 AM
 */

package de.unibw.inf2.fishification.server.fishworld;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Specifies JSON properties of a Fish for de/serialization using Jackson.
 *
 * @author Martin Burkhard
 */
@NotThreadSafe
class Fish {

    private final String m_name;
    private final String m_fishType;
    private final String m_authorName;
    private final String m_flagTitle;
    private final String m_flagText;

    public Fish(final String fishName, final String fishType, final String authorName, final String flagTitle, final String flagText) {
        m_name = fishName;
        m_fishType = fishType;
        m_authorName = authorName;
        m_flagTitle = flagTitle;
        m_flagText = flagText;
    }

    @JsonProperty
    public String getName() {
        return m_name;
    }

    @JsonProperty
    public String getFishType() {
        return m_fishType;
    }

    @JsonProperty
    public String getAuthorName() {
        return m_authorName;
    }

    @JsonProperty
    public String getFlagTitle() {
        return m_flagTitle;
    }

    @JsonProperty
    public String getFlagText() {
        return m_flagText;
    }
}
