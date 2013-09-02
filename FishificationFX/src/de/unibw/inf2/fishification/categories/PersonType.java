/*
 * PersonType.java
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
 *     Date: 9/2/13 8:51 AM
 */

package de.unibw.inf2.fishification.categories;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a list of person names.
 *
 * @author Sonja Maier
 */
public abstract class PersonType {

    private static final List<String> PERSONS     = new ArrayList<String>();
    private static final Object       SYNC_ACCESS = new Object();

    public static int indexOf(String s) {
        return PERSONS.indexOf(s);
    }

    public static List<String> getPersons() {
        synchronized (SYNC_ACCESS) {
            return new ArrayList<String>(PERSONS);
        }
    }

    public static void clear() {
        synchronized (SYNC_ACCESS) {
            PERSONS.clear();
        }
    }

    public static int numberOfPersons() {
        synchronized (SYNC_ACCESS) {
            return PERSONS.size();
        }
    }

    public static void addPerson(String s) {
        synchronized (SYNC_ACCESS) {
            PERSONS.add(s);
        }
    }
}
