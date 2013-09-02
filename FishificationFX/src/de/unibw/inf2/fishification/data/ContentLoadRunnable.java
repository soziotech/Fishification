/*
 * ContentLoadRunnable.java
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
 *     Date: 9/2/13 11:43 PM
 */

package de.unibw.inf2.fishification.data;

import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.entities.EntityFactory;
import de.unibw.inf2.fishification.entities.FishEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

class ContentLoadRunnable implements Runnable {

    private final int                    m_amount;
    private final ContentCollectRunnable m_collector;
    private final FishWorld              m_fishWorld;
    private long m_lastUpdate = 0;

    private       RunnableState m_state             = RunnableState.INIT;
    private final Object        m_runnableStateSync = new Object();

    private static final int    LOAD_SLEEP         = 2500;
    final static         int    UPDATE_DELAY       = 1000;
    final static         int    UPDATE_DELAY_SLEEP = 500;
    private static final Logger m_log              = LogManager.getLogger();

    public ContentLoadRunnable(final int amount, final ContentCollectRunnable collector, final FishWorld fishWorld) {

        m_amount = amount;
        m_collector = collector;
        m_fishWorld = fishWorld;
    }

    @Override
    public void run() {

        if (m_amount <= 0) {
            return;
        }

        m_log.debug("ContentLoad thread started.");

        setState(RunnableState.RUNNING);

        // Load as many Contents as required
        for (int i = 0; i < m_amount; i++) {

            if (getState() == RunnableState.STOPPED) {
                return;
            }

            // Get ContentItem from Collector
            ContentItem contentItem = m_collector.getNextContentItem();

            RunnableState state = getState();
            while (contentItem == null && (state == RunnableState.RUNNING || state == RunnableState.PAUSED)) {

                m_log.debug("Waiting for collector ...");

                try {
                    // Wait
                    Thread.sleep(LOAD_SLEEP);

                    // Ask for next ContentItem again
                    contentItem = m_collector.getNextContentItem();

                    state = m_collector.getState();

                } catch (InterruptedException e) {
                    m_log.debug("Thread sleep interrupted while waiting for collector.");
                    return;
                }
            }

            // Perform Pause
            while (getState() == RunnableState.PAUSED) {
                try {
                    // Wait
                    m_log.debug("Pause ...");
                    Thread.sleep(LOAD_SLEEP);

                } catch (InterruptedException e) {
                    m_log.debug("Thread sleep interrupted duringe pause.");
                    return;
                }
            }

            // Prevents that too many ContentItems appear at the same time
            while(new Date().getTime() - m_lastUpdate < UPDATE_DELAY) {
                try {
                    // Wait
                    m_log.debug("Update delay. Waiting ...");
                    Thread.sleep(UPDATE_DELAY_SLEEP);

                } catch (InterruptedException e) {
                    m_log.debug("Thread sleep interrupted duringe update delay.");
                    return;
                }
            }

            // Add Content Item to FishWorld
            if (contentItem != null) {
                m_log.debug(String.format("Loaded ContentItem #%d of %d.", i + 1, m_amount));

                FishEntity fishEntity = EntityFactory.createFishEntity(contentItem, m_fishWorld);
                m_fishWorld.addFishEntity(fishEntity);

                m_lastUpdate = new Date().getTime();
            }
        }

        m_state = RunnableState.FINISHED;
        m_log.debug("ContentLoad thread ended.");
        m_log.info(String.format("The request for #%d contents was successfully processed.", m_amount));
    }

    RunnableState getState() {
        synchronized (m_runnableStateSync) {
            return m_state;
        }
    }

    private void setState(RunnableState state) {
        synchronized (m_runnableStateSync) {
            m_state = state;
        }
    }

    public void pause(boolean isPaused) {
        if (isPaused) {
            setState(RunnableState.PAUSED);
        } else {
            setState(RunnableState.RUNNING);
        }

    }

    public void stop() {
        setState(RunnableState.STOPPED);
    }

}
