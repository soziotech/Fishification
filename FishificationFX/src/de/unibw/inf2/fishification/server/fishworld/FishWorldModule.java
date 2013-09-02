/*
 * FishWorldModule.java
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

import com.google.inject.AbstractModule;
import de.unibw.inf2.fishification.FishWorld;

/**
 * Binds and configures FishWorld classes.
 *
 * @author Martin Burkhard
 */
public class FishWorldModule extends AbstractModule {

    @Override
    protected void configure() {

        // Bind
        bind(FishWorld.class);
        bind(FishWorldFactory.class);
        bind(FishWorldStats.class);

        // Install Module
        install(new FishWorldServletModule());
    }
}
