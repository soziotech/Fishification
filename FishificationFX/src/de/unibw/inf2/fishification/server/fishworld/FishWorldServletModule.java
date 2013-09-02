/*
 * FishWorldServletModule.java
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

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures and installs a ServletModule for the Fish World.
 *
 * @author Martin Burkhard
 */
class FishWorldServletModule extends ServletModule {

    @Override
    protected void configureServlets() {

        // Bind Resources
        bind(FishAddResource.class);
        bind(FishFeedResource.class);
        bind(FishWorldStatsResource.class);

        // Jersey Injection
        bind(GuiceContainer.class);

        // JSON Serialization
        bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);

        Map<String, String> guiceContainerConfig = new HashMap<String, String>();
        serve("/*").with(GuiceContainer.class, guiceContainerConfig);
    }
}
