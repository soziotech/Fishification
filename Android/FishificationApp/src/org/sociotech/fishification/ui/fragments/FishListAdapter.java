/*
 * FishListAdapter.java
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.sociotech.fishification.R;
import org.sociotech.fishification.model.listitems.FishItem;

import java.util.List;

/**
 * List adapter for fish items assigning name and image.
 */
class FishListAdapter extends ArrayAdapter<FishItem> {

    static class ViewHolder {
        private final ImageView m_imageView;
        private final TextView  m_textView;

        public ViewHolder(ImageView image, TextView text) {
            m_imageView = image;
            m_textView = text;
        }

        public ImageView getImageView() {
            return m_imageView;
        }

        public TextView getTextView() {
            return m_textView;
        }
    }

    public FishListAdapter(Context context, List<FishItem> fishItems) {
        super(context, org.sociotech.fishification.R.layout.fragment_fish_item, fishItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Extract FishItem
        FishItem fishItem = getItem(position);

        // if the array item is null, nothing to display, just return null
        if (fishItem == null) {
            return null;
        }

        View rowView = convertView;
        if (rowView == null) {
            // We need the LayoutInflater to pick up the view from xml
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.fragment_fish_item, null);

            ViewHolder viewHolder = new ViewHolder((ImageView) rowView.findViewById(R.id.fish_image),
                                                   (TextView) rowView.findViewById(R.id.fish_name));
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        if (holder != null) {
            // Set fish image
            holder.getImageView().setImageDrawable(
                    getContext().getResources().getDrawable(fishItem.getImageResourceId()));

            // Set fish text
            holder.getTextView().setText(fishItem.getFishName());
        }

        return rowView;
    }

}
