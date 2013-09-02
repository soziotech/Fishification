/*
 * FishWorldStats.java
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

package de.unibw.inf2.fishification.server.fishworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Creates serializable statistics for the FishWorldServletModule.
 *
 * @author Martin Burkhard
 */
@ThreadSafe
@Singleton
public class FishWorldStats {

    // Fish resource counters
    private int m_fishAdded;
    private int m_fishAddedSuccessCounter;
    private int m_fishAddedFailureCounter;

    // Fish food resource counters
    private int m_fishFed;
    private int m_fishFedSuccessCounter;
    private int m_fishFedFailureCounter;

    @Inject
    FishWorldStats(MetricsRegistry metricsRegistry) {
        metricsRegistry.newGauge(FishWorldStats.class, "fishAdded", new Gauge<Integer>() {
            @Override
            public Integer value() {
                return m_fishAdded;
            }
        });
        metricsRegistry.newGauge(FishWorldStats.class, "fishAddedSuccessCounter", new Gauge<Integer>() {
            @Override
            public Integer value() {
                return m_fishAddedSuccessCounter;
            }
        });
        metricsRegistry.newGauge(FishWorldStats.class, "fishAddedFailureCounter", new Gauge<Integer>() {
            @Override
            public Integer value() {
                return m_fishAddedFailureCounter;
            }
        });
        metricsRegistry.newGauge(FishWorldStats.class, "fishFed", new Gauge<Integer>() {
            @Override
            public Integer value() {
                return m_fishFed;
            }
        });
        metricsRegistry.newGauge(FishWorldStats.class, "fishFedSuccessCounter", new Gauge<Integer>() {
            @Override
            public Integer value() {
                return m_fishFedSuccessCounter;
            }
        });
        metricsRegistry.newGauge(FishWorldStats.class, "fishFedFailureCounter", new Gauge<Integer>() {
            @Override
            public Integer value() {
                return m_fishFedFailureCounter;
            }
        });
    }

    /**
     * Increases the fish added counter.
     *
     * @param fish New fish.
     */
    public synchronized void registerNewFish(Fish fish, Result result) {
        if (fish == null || result == null) {
            return;
        }
        m_fishAdded++;
        if (result.getSuccess()) {
            m_fishAddedSuccessCounter++;
        } else {
            m_fishAddedFailureCounter++;
        }
    }

    /**
     * Increases the fish feed counter.
     *
     * @param source The Fish Type to be fed.
     */
    public synchronized void feedFish(String source, Result result) {
        if (source == null || result == null) {
            return;
        }
        m_fishFed++;
        if (result.getSuccess()) {
            m_fishFedSuccessCounter++;
        } else {
            m_fishFedFailureCounter++;
        }
    }

    /**
     * Returns the amount of fishes added.
     *
     * @return Amount of Fish added
     */
    synchronized FishWorldStatsReport getStatsReport() {
        return new FishWorldStatsReport(m_fishAdded, m_fishAddedSuccessCounter, m_fishAddedFailureCounter, m_fishFed,
                                        m_fishFedSuccessCounter, m_fishFedFailureCounter);
    }

    public static class FishWorldStatsReport {

        private final int m_fishAdded;
        private final int m_fishAddedSuccessCounter;
        private final int m_fishAddedFailurecounter;
        private final int m_fishFed;
        private final int m_fishFedSuccessCounter;
        private final int m_fishFedFailurecounter;

        public FishWorldStatsReport(int fishAdded, int fishAddedSuccessCounter, int fishAddedFailureCounter, int fishFed, int fishFedSuccessCounter, int fishFedFailureCounter) {
            m_fishAdded = fishAdded;
            m_fishAddedSuccessCounter = fishAddedSuccessCounter;
            m_fishAddedFailurecounter = fishAddedFailureCounter;
            m_fishFed = fishFed;
            m_fishFedSuccessCounter = fishFedSuccessCounter;
            m_fishFedFailurecounter = fishFedFailureCounter;
        }

        @JsonProperty
        public int getFishAdded() {
            return m_fishAdded;
        }

        @JsonProperty
        public int getFishAddedSuccessCounter() {
            return m_fishAddedSuccessCounter;
        }

        @JsonProperty
        public int getFishAddedFailurecounter() {
            return m_fishAddedFailurecounter;
        }

        @JsonProperty
        public int getFishFed() {
            return m_fishFed;
        }

        @JsonProperty
        public int getFishFedSuccessCounter() {
            return m_fishFedSuccessCounter;
        }

        @JsonProperty
        public int getFishFedFailurecounter() {
            return m_fishFedFailurecounter;
        }
    }

}
