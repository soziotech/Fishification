/*
 * ContentLoader.java
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
 *     Date: 9/2/13 11:26 PM
 */

package de.unibw.inf2.fishification.data;

import de.unibw.inf2.fishification.FishWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.sociotech.communitymashup.framework.android.common.ConnectionManager;

/**
 * Loads Data
 */
public class ContentLoader {

    private final FishWorld m_fishWorld;

    private final ContentCollectRunnable m_contentCollector;
    private       ContentLoadRunnable    m_contentLoader;

    private Thread m_loaderWorker;
    private Thread m_collectorWorker;
    private static final int    START_SLEEP = 1000;
    private static final int    STOP_SLEEP  = 100;
    private static final Logger m_log       = LogManager.getLogger();

    public ContentLoader(String baseUrl, int basePort, String restEndpoint, FishWorld fishWorld) {
        m_fishWorld = fishWorld;

        // Initialize CommunityMashup
        ConnectionManager.init(baseUrl, basePort, restEndpoint);

        // Initialize ContentCollector
        m_contentCollector = new ContentCollectRunnable();

        // Begin data collection after short delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(START_SLEEP);
                } catch (InterruptedException e) {
                    m_log.warn(MarkerManager.getMarker("EXCEPTION"), "BeginDataCollection sleep interrupted.");
                }
                beginDataCollection();
            }
        }).start();

    }

    private void beginDataCollection() {
        if (m_contentCollector.getState() == RunnableState.RUNNING) {
            m_log.debug(String.format("No data refresh possible. Still collecting data for previous request."));
            return;
        }

        m_log.debug("Started data collection.");

        // Start Loading
        if (m_collectorWorker == null) {
            m_collectorWorker = new Thread(m_contentCollector);
            m_collectorWorker.setName("ContentCollector");
            m_collectorWorker.start();
        }
    }

    public void getContentItems(int amount) {

        m_log.info(String.format("Received request for #%d contents.", amount));

        // Initialize ContentLoader
        m_contentLoader = new ContentLoadRunnable(amount, m_contentCollector, m_fishWorld);

        stopContentLoader();

        m_loaderWorker = new Thread(m_contentLoader);
        m_loaderWorker.setName("ContentLoader");
        m_loaderWorker.start();
    }

    public void pause(boolean isPaused) {
        m_log.debug("Pausing Load Worker ...");
        m_contentLoader.pause(isPaused);
    }

    public void stopContentLoader() {

        if (m_loaderWorker == null || !m_loaderWorker.isAlive()) {
            return;
        }

        m_log.debug("Load Worker is still running. Stopping ...");
        m_contentLoader.stop();

        while (m_loaderWorker.isAlive()) {
            m_log.debug("Load Worker is still running. Interrupting ...");
            try {
                m_loaderWorker.interrupt();
                Thread.sleep(STOP_SLEEP);
            } catch (InterruptedException e) {
                m_log.info("Thread sleep interrupted.");
                return;
            }
        }
        m_log.debug("Load Worker stopped.");
    }

    public void shutdown() {
        if (m_contentCollector != null && m_collectorWorker.isAlive()) {
            m_contentCollector.stop();
        }

        stopContentLoader();
    }

}
