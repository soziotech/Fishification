/*
 * XlsLoggerEntry.java
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
 *     Date: 9/2/13 8:45 AM
 */

package de.unibw.inf2.fishification.util;

import de.unibw.inf2.fishification.data.ContentItem;

import java.util.Date;

/**
 * Log entry for XlsLogger.
 */
public class XlsLoggerEntry {
    private final int         m_amountOfFishes;
    private final Date        m_dateTime;
    private final String      m_fishType;
    private final double      m_fishPosX;
    private final double      m_fishPosY;
    private final String      m_intention;
    private final String      m_interaction;
    private final double      m_interactionPosX;
    private final double      m_interactionPosY;
    private final ContentItem m_content;

    public XlsLoggerEntry(int amountOfFishes, Date dateTime, String fishType, double fishPosX, double fishPosY, String intention, String interaction, double interactionPosX, double interactionPosY, ContentItem content) {
        m_amountOfFishes = amountOfFishes;
        m_dateTime = dateTime;
        m_fishType = fishType;
        m_fishPosX = fishPosX;
        m_fishPosY = fishPosY;
        m_intention = intention;
        m_interaction = interaction;
        m_interactionPosX = interactionPosX;
        m_interactionPosY = interactionPosY;
        m_content = content;
    }

    public int getAmountOfFishes() {
        return m_amountOfFishes;
    }

    public Date getDateTime() {
        return new Date(m_dateTime.getTime());
    }

    public String getFishType() {
        return m_fishType;
    }

    public double getFishPosX() {
        return m_fishPosX;
    }

    public double getFishPosY() {
        return m_fishPosY;
    }

    public String getIntention() {
        return m_intention;
    }

    public String getInteraction() {
        return m_interaction;
    }

    public double getInteractionPosX() {
        return m_interactionPosX;
    }

    public double getInteractionPosY() {
        return m_interactionPosY;
    }

    public ContentItem getContent() {
        return m_content;
    }
}
