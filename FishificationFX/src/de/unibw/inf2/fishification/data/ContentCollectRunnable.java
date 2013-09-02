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
 *     Date: 9/2/13 8:48 AM
 */

package de.unibw.inf2.fishification.data;

import org.eclipse.emf.common.util.EList;
import org.sociotech.communitymashup.data.Content;
import org.sociotech.communitymashup.data.DataSet;
import org.sociotech.communitymashup.framework.android.util.DataSetHandler;
import org.sociotech.unui.javafx.engine2d.util.Log;

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

    private static final String TAG = "ContentCollectRunnable";

    public ContentCollectRunnable() {
    }

    private void addContentItem(ContentItem contentItem) {
        synchronized (m_contentItemsSync) {

            for (ContentItem c : m_contentItems) {
                if (c.compare(c, contentItem) == 0) {
                    Log.d(TAG, "Same ContentItem already in list. Skipping.");
                    return;
                }
            }

            m_contentItems.add(contentItem);
            Log.d(TAG, "Added new ContentItem.");
        }
    }

    public ContentItem getNextContentItem() {
        synchronized (m_contentItemsSync) {

            // Check if ContentItems are loaded
            if (m_contentItems == null || m_contentItems.size() == 0) {
                Log.d(TAG, "No ContentItems available.");
                return null;
            }

            // Reload contents in case we displayed all items
            if (m_contentItemsIndex >= m_contentItems.size()) {

                if (!m_loadingFinished) {
                    Log.d(TAG, "Not enough ContentItems available.");
                    return null;
                } else {
                    Log.d(TAG, "Restarting from the beginning.");
                    m_contentItemsIndex = 0;
                }
            }

            return m_contentItems.get(m_contentItemsIndex++);
        }
    }

    @Override
    public void run() {

        Log.i(TAG, "Data collection thread started.");
        setState(RunnableState.RUNNING);

        // Reset
        m_contentItems = new ArrayList<ContentItem>();
        m_contentItemsIndex = 0;
        m_loadingFinished = false;

        // Get CommunityMashup DataSet
        DataSet dataSet = DataSetHandler.getDataSet();

        // Always collect new contents
        while (getState() == RunnableState.RUNNING) {

            try {

                // Load Contents from CommunityMashup
                EList<Content> contents = dataSet.getContents();

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

            } catch (Exception ex) {
                Log.w("ContentLoader", "Error while trying to get data from CommunityMashup.", ex);
            }

            Log.i(TAG, "Data collection loaded.");
            m_loadingFinished = true;
        }

        m_state = RunnableState.FINISHED;
        Log.i(TAG, "Data collection thread ended.");
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


