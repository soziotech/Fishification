/*
 * FishFeedResource.java
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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.concurrent.ThreadSafe;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Registers REST operation for adding new fish food to the FishWorld.
 *
 * @author Martin Burkhard
 */
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@ThreadSafe
@Path("/feed")
public class FishFeedResource {

    private final FishWorldFactory m_fishWorldFactory;
    private final FishWorldStats   m_fishWorldStats;

    @Inject
    FishFeedResource(FishWorldFactory fishWorldFactory, FishWorldStats fishWorldStats) {
        m_fishWorldFactory = fishWorldFactory;
        m_fishWorldStats = fishWorldStats;
    }

    @GET
    public Result feedFish(@QueryParam("source") @DefaultValue("all") String source) {

        // Feed fish
        boolean success = m_fishWorldFactory.feedFish(source);
        Result result = new Result("feedFish", success);

        // Add to stats
        m_fishWorldStats.feedFish(source, result);
        return result;
    }

}
