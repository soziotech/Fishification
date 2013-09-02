/*
 * ShareFishFragment.java
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
 *     Date: 8/30/13 7:46 PM
 */

package org.sociotech.fishification.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import org.sociotech.fishification.R;
import org.sociotech.fishification.controller.listener.OnShareFishListener;

/**
 * Fragment showing form for sharing information via fish.
 */
public class ShareFishFragment extends Fragment {

    private OnShareFishListener m_shareFishListener;
    private static String s_initialFlagContent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fish_share, container, false);

        Button sendFishButton = (Button) view.findViewById(R.id.shareFishButton);
        sendFishButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                m_shareFishListener.onShareFish();
            }
        });

        if (s_initialFlagContent != null) {
            final EditText flagContentText = (EditText) view.findViewById(R.id.flagContentText);
            if (flagContentText != null) {
                flagContentText.setText(s_initialFlagContent);  // assign text
            }
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Assign listener to activity's callback implementation
        m_shareFishListener = (OnShareFishListener) activity;
    }

    public static void setInitialFlagContent(String initialFlagContent) {
        ShareFishFragment.s_initialFlagContent = initialFlagContent;
    }
}
