/*
 * Log.java
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

package org.sociotech.unui.javafx.engine2d.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Log {

    private static final String FORMAT = "[%s] %s";
    private static Logger s_logger;

    private Log() {

    }

    private static Logger getLogger() {
        if (s_logger == null) {
            s_logger = LogManager.getLogger();
        }
        return s_logger;
    }

    public static void d(String tag, String message) {
        getLogger().debug(String.format(FORMAT, tag, message));
    }

    public static void i(String tag, String message) {
        getLogger().info(String.format(FORMAT, tag, message));
    }

    public static void w(String tag, String message) {
        getLogger().warn(String.format(FORMAT, tag, message));
    }

    public static void w(String tag, String message, Exception e) {
        getLogger().warn(String.format(FORMAT, tag, message), e);
    }

    public static void e(String tag, String message, Exception e) {
        getLogger().error(String.format(FORMAT, tag, message), e);
    }
}
