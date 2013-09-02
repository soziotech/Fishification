/*
 * FishificationServer.java
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

package de.unibw.inf2.fishification.server;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricsRegistry;
import de.unibw.inf2.fishification.server.fishworld.FishWorldModule;
import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.sociotech.unui.javafx.engine2d.util.Log;

import java.util.EnumSet;

/**
 * Main entry point to configure, install and launch Fishification Server.
 *
 * @author Martin Burkhard
 */
public final class FishificationServer {

    private static Server s_server;

    private FishificationServer() {

    }

    /**
     * Starts Server
     *
     * @param port        Server Port
     * @param contextPath Endpoint name
     */
    public static void launch(int port, String endpoint) {

        // Initialize Guice Injector
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {

                binder().requireExplicitBindings();

                install(new FishWorldModule());

                bind(GuiceFilter.class);

                bind(MetricsRegistry.class).toInstance(Metrics.defaultRegistry());
            }
        });

        // Create Server
        s_server = new Server(port);
        String contextPath = endpoint;

        // Add default Servlet
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(contextPath);
        handler.addServlet(new ServletHolder(new BadRequestServlet()), "/*");

        // Add Guice & Jersey Filter
        FilterHolder guiceFilter = new FilterHolder(injector.getInstance(GuiceFilter.class));
        handler.addFilter(guiceFilter, "/*", EnumSet.allOf(DispatcherType.class));
        s_server.setHandler(handler);

        // Start Server
        try {

            s_server.start();

        } catch (Exception e) {
            Log.e("FishificationServer", "Error while starting server.", e);
        }
    }

    public static void shutdown() {

        if (s_server == null) {
            return;
        }

        try {
            s_server.stop();
        } catch (Exception e) {
            Log.e("FishificationServer", "Error while stopping server.", e);
        }
    }

}
