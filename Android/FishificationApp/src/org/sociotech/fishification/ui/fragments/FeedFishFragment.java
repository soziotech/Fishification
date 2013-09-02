/*
 * FeedFishFragment.java
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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import org.sociotech.fishification.R;
import org.sociotech.fishification.controller.listener.OnFeedFishListener;

class FeedFishFragment extends Fragment implements SensorEventListener {

    private View               m_view;
    private Drawable           m_defaultBackground;
    private OnFeedFishListener m_feedFishListener;

    private SensorManager m_sensorManager;
    private boolean m_turnedUpsideDown = false;
    private long m_lastUpdate;

    private static final int SENSOR_TRESHOLD  = 5;
    private static final int UPDATE_FREQUENCY = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        m_view = inflater.inflate(R.layout.fragment_fish_feed, container, false);
        m_defaultBackground = m_view.getBackground();

        // Init Gallery
        Gallery gallery = (Gallery) m_view.findViewById(R.id.fishBoxGallery);
        gallery.setAdapter(new FishFoodBoxAdapter(m_view.getContext()));

        // Init Sensor
        Context context = m_view.getContext();
        m_sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        m_lastUpdate = System.currentTimeMillis();

        return m_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Assign listener to activity's callback implementation
        m_feedFishListener = (OnFeedFishListener) activity;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float y = sensorEvent.values[1];

            // Check for upside down movement
            if (y > SENSOR_TRESHOLD) {
                m_turnedUpsideDown = false;
            }
            if (y < -SENSOR_TRESHOLD) {
                m_turnedUpsideDown = true;
            }

            // Ensure that a reasonable period of time elapses before the device can turned upside down again
            long actualTime = System.currentTimeMillis();
            int upsideDownFrequency = UPDATE_FREQUENCY;
            if (actualTime - m_lastUpdate < upsideDownFrequency) {
                return;
            }
            m_lastUpdate = actualTime;

            // Check if device has been turned upside down
            if (m_turnedUpsideDown) {

                // Change background color to green
                m_view.setBackgroundColor(Color.GREEN);

                // Trigger fish feed
                m_feedFishListener.onFeedFish();
            } else {
                // reset background to initial state
                m_view.setBackgroundDrawable(m_defaultBackground);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for Sensor changes
        if (m_sensorManager != null) {
            m_sensorManager.registerListener(this, m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                                             SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {

        // Unregister Sensor listener
        super.onPause();
        if (m_sensorManager != null) {
            m_sensorManager.unregisterListener(this);
        }
    }
}
