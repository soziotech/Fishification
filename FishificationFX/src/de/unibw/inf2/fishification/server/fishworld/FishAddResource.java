/*
 * FishAddResource.java
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
 * Registers REST operation for adding new fish to the FishWorld.
 *
 * @author Martin Burkhard
 */
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@ThreadSafe
@Path("/add")
public class FishAddResource {

    private final FishWorldFactory m_fishWorldFactory;
    private final FishWorldStats   m_fishWorldStats;

    @Inject
    FishAddResource(FishWorldFactory fishWorldFactory, FishWorldStats fishWorldStats) {
        m_fishWorldFactory = fishWorldFactory;
        m_fishWorldStats = fishWorldStats;
    }

    @GET
    public Result addFish(@QueryParam("fishName") @DefaultValue("Anonyfish") String fishName, @QueryParam(
            "fishType") @DefaultValue("LadyFish") String fishType, @QueryParam("title") @DefaultValue(
            "I'm your new fish") String title, @QueryParam("stringValue") @DefaultValue(
            "Congratulations =)") String stringValue, @QueryParam("authorName") @DefaultValue(
            "anonymous") String authorName) {

        // Add Fish
        boolean success = m_fishWorldFactory.addFish(fishName, fishType, title, stringValue, authorName);
        Fish fish = new Fish(fishName, fishType, title, stringValue, authorName);

        // Add to stats
        Result result = new Result("addFishEntity", success);
        m_fishWorldStats.registerNewFish(fish, result);

        return result;
    }

}
