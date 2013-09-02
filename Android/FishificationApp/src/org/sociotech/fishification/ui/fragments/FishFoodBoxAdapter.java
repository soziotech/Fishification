/*
 * FishFoodBoxAdapter.java
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
 *     Date: 9/2/13 12:55 AM
 */

package org.sociotech.fishification.ui.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.sociotech.fishification.model.listitems.FishFoodBoxes;

/**
 * List adapter for fish food box images.
 */
class FishFoodBoxAdapter extends BaseAdapter {

    private final Context m_context;

    public FishFoodBoxAdapter(Context context) {
        m_context = context;
    }

    @Override
    public int getCount() {

        return FishFoodBoxes.getImageList().length;
    }

    @Override
    public Object getItem(int item) {

        return item;
    }

    @Override
    public long getItemId(int itemId) {

        return itemId;
    }

    @Override
    public View getView(int item, View view, ViewGroup viewGroup) {
        ImageView iv = new ImageView(m_context);
        iv.setImageResource(FishFoodBoxes.getImageList()[item]);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        return iv;
    }

}
