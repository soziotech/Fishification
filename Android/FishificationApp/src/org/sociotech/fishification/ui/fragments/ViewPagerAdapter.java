/*
 * ViewPagerAdapter.java
 *
 * Copyright (c) 2013 Martin Burkhard.
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
 *     Date: 8/30/13 8:29 PM
 */

package org.sociotech.fishification.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Pageable Fragments with tab navigation.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int                TAB_COUNT            = 3;
    private final        CreateFishFragment s_createFishFragment = new CreateFishFragment();
    private final        ShareFishFragment  s_shareFishFragment  = new ShareFishFragment();
    private final        FeedFishFragment   s_feedFishFragment   = new FeedFishFragment();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return s_createFishFragment;
            case 1:
                return s_shareFishFragment;
            case 2:
                return s_feedFishFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
