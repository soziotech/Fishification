/*
 * ResourceManager.java
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

package org.sociotech.unui.javafx.engine2d;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple HashMap-based resource manager for storing resource references in memory.
 *
 * @author Martin Burkhard
 */
public class ResourceManager<T> {

    private final Map<String, T> resources = new HashMap<String, T>();

    public void put(String resourceKey, T value) {
        resources.put(resourceKey.toLowerCase(), value);
    }

    public T get(String resourceKey) {
        return resources.get(resourceKey.toLowerCase());
    }
}
