/*
 * ContentCollectRunnable.java
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
 *     Date: 9/3/13 12:08 AM
 */

package de.unibw.inf2.fishification.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.eclipse.emf.common.util.EList;
import org.sociotech.communitymashup.data.Content;
import org.sociotech.communitymashup.data.DataSet;
import org.sociotech.communitymashup.framework.android.util.DataSetHandler;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class ContentCollectRunnable implements Runnable {

    private RunnableState     m_state             = RunnableState.INIT;
    private List<ContentItem> m_contentItems      = new ArrayList<ContentItem>();
    private int               m_contentItemsIndex = 0;
    private boolean           m_loadingFinished   = false;

    private final Object m_contentItemsSync  = new Object();
    private final Object m_runnableStateSync = new Object();

    private static final int    REQUEST_SLEEP = 1000;
    private static final Logger m_log         = LogManager.getLogger();

    public ContentCollectRunnable() {
    }

    private void addContentItem(ContentItem contentItem) {
        synchronized (m_contentItemsSync) {

            for (ContentItem c : m_contentItems) {
                if (c.compare(c, contentItem) == 0) {
                    m_log.debug("Same ContentItem already in list. Skipping.");
                    return;
                }
            }

            m_contentItems.add(contentItem);
            m_log.debug("Added new ContentItem.");
        }
    }

    public ContentItem getNextContentItem() {
        synchronized (m_contentItemsSync) {

            // Check if ContentItems are loaded
            if (m_contentItems == null || m_contentItems.size() == 0) {
                m_log.debug("No ContentItems available.");
                return null;
            }

            // Reload contents in case we displayed all items
            if (m_contentItemsIndex >= m_contentItems.size()) {

                if (!m_loadingFinished) {
                    m_log.debug("Not enough ContentItems available.");
                    return null;
                } else {
                    m_log.debug("Restarting from the beginning.");
                    m_contentItemsIndex = 0;
                }
            }

            return m_contentItems.get(m_contentItemsIndex++);
        }
    }

    @Override
    public void run() {

        m_log.debug( "ContentCollection thread started.");
        setState(RunnableState.RUNNING);

        // Reset
        m_contentItems = new ArrayList<ContentItem>();
        m_contentItemsIndex = 0;
        m_loadingFinished = false;

        // Get CommunityMashup DataSet
        DataSet dataSet = DataSetHandler.getDataSet();
        if(dataSet == null) {
            m_log.warn( "CommunityMashup DataSet was empty.");
            return;
        }

        // Always collect new contents
        while (getState() == RunnableState.RUNNING) {

            try {

                // Load Contents from CommunityMashup
                EList<Content> contents = null;
                try {
                    contents = dataSet.getContents();
                } catch (Exception e) {
                    m_log.warn(MarkerManager.getMarker("EXCEPTION"), "CommunityMashup requesting contents failed.", e);
                }

                if(contents == null) {
                    m_log.warn( "CommunityMashup DataSet did not return any contents. Waiting ...");
                    Thread.sleep(REQUEST_SLEEP);
                    continue;
                }
                m_log.info("CommunityMashup successfully returned contents.");

                // Select and convert Contents
                for (Content content : contents) {

                    // Check for stop state
                    if (getState() == RunnableState.STOPPED) {
                        break;
                    }

                    // Convert Content to ContentItem
                    ContentItem item = new ContentItem(content);

                    // Cache ContentItems
                    addContentItem(item);
                }

            } catch (Exception e) {
                m_log.warn(MarkerManager.getMarker("EXCEPTION"), "Error while trying to get data from CommunityMashup.", e);
            }

            m_log.info( "CommunityMashup contents successfully loaded.");
            m_loadingFinished = true;
        }

        m_state = RunnableState.FINISHED;
        m_log.debug( "Data collection thread ended.");
    }

    public RunnableState getState() {
        synchronized (m_runnableStateSync) {
            return m_state;
        }
    }

    private void setState(RunnableState state) {
        synchronized (m_runnableStateSync) {
            m_state = state;
        }
    }

    public void stop() {
        setState(RunnableState.STOPPED);
    }

}


