/*
 * MainActivity.java
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

package org.sociotech.fishification.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import org.sociotech.fishification.R;
import org.sociotech.fishification.controller.listener.OnFeedFishListener;
import org.sociotech.fishification.controller.listener.OnFishSelectedListener;
import org.sociotech.fishification.controller.listener.OnShareFishListener;
import org.sociotech.fishification.model.listitems.FishFoodBoxes;
import org.sociotech.fishification.model.listitems.FishItems;
import org.sociotech.fishification.ui.fragments.ShareFishFragment;
import org.sociotech.fishification.ui.fragments.ViewPagerAdapter;
import org.sociotech.fishification.utils.CallHttpTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener, OnFishSelectedListener, OnShareFishListener, OnFeedFishListener {

    public static final String EXTRA_SHARED_TEXT = "org.sociotech.fishification.ui.SHARED_TEXT";
    public static final String PREFS_NAME        = "Fishfication.prefs";

    private String m_restFishAddUrl;
    private String m_restFishFeedUrl;

    private boolean m_wifiCheck = false;
    private String[] m_wifiSSIDs;

    private ViewPager m_ViewPager;
    private static int s_fishTypeIndex = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Init properties
        initProperties();

        // Initialize View Pager and Tabs
        initViewPager();

        // Extract information shared by external Apps
        extractSharedInformation();
    }

    private void initProperties() {

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.fishification);
            if (inputStream == null) {
                Log.e(getString(R.string.MainActivityTag), "Failed to open fishification property file");
                return;
            }
            Properties properties = new Properties();
            properties.load(inputStream);

            String restBaseUrl = properties.getProperty("fishification.rest.base.url");
            String restBasePort = properties.getProperty("fishification.rest.base.port");
            String restEndpoint = properties.getProperty("fishification.rest.endpoint");
            String restFishAdd = properties.getProperty("fishification.rest.operation.fishadd");
            String restFishFeed = properties.getProperty("fishification.rest.operation.fishfeed");
            m_restFishAddUrl = String.format("%s:%s/%s/%s?", restBaseUrl, restBasePort, restEndpoint, restFishAdd);
            m_restFishFeedUrl = String.format("%s:%s/%s/%s?", restBaseUrl, restBasePort, restEndpoint, restFishFeed);

            m_wifiCheck = Boolean.parseBoolean(properties.getProperty("fishification.wifi.check"));
            String wifiSsidsParam = properties.getProperty("fishification.wifi.allowed.ssids");
            m_wifiSSIDs = wifiSsidsParam.split(",");

        } catch (IOException e) {
            Log.e(getString(R.string.MainActivityTag), "Failed to open fishification property file");
        }
    }

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        m_ViewPager = (ViewPager) findViewById(R.id.viewPager);
        if (m_ViewPager != null) {
            m_ViewPager.setAdapter(viewPagerAdapter);
            m_ViewPager.setOnPageChangeListener(this);

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            addTab(actionBar, 0, R.string.tab_title_yourFish);
            addTab(actionBar, 1, R.string.tab_title_shareFish);
            addTab(actionBar, 2, R.string.tab_title_feedFish);
        }
        getActionBar().setHomeButtonEnabled(false);
    }

    private void addTab(ActionBar actionBar, int pos, int labelRes) {
        actionBar.addTab(actionBar.newTab().setText(getString(labelRes)).setTabListener(this), pos);
    }

    private void extractSharedInformation() {
        Intent intent = getIntent();
        if (intent != null) {
            String sharedText = intent.getStringExtra(MainActivity.EXTRA_SHARED_TEXT);
            if (sharedText != null && !sharedText.isEmpty()) {

                // show tab with flag content
                m_ViewPager.setCurrentItem(1);
                getActionBar().setSelectedNavigationItem(1);

                ShareFishFragment.setInitialFlagContent(sharedText);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        final TextView authorNameText = (TextView) findViewById(R.id.authorNameText);
        final TextView fishNameText = (TextView) findViewById(R.id.fishNameText);

        String authorName = getText(authorNameText);
        String fishName = getText(fishNameText);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (authorName != null && !authorName.isEmpty()) {
            editor.putString("authorName", authorName);
        }
        if (fishName != null && !fishName.isEmpty()) {
            editor.putString("fishName", fishName);
        }
        if (s_fishTypeIndex != ListView.INVALID_POSITION) {
            editor.putInt("fishTypeIndex", s_fishTypeIndex);
        }

        // Commit the edits!
        editor.commit();
    }

    private String getText(TextView textView) {
        if (textView == null) {
            return "";
        }
        return textView.getText().toString().trim();
    }

    @Override
    public void onFishSelected(int position) {

        s_fishTypeIndex = position;
        enableShareFishButton(s_fishTypeIndex != ListView.INVALID_POSITION);
    }

    private void enableShareFishButton(boolean enabled) {
        final Button shareFishButton = (Button) findViewById(R.id.shareFishButton);
        if (shareFishButton != null) {
            shareFishButton.setEnabled(enabled);
        }
    }

    /**
     * Creates fish with shared contents by calling FishificationFx endpoint.
     */
    @Override
    public void onShareFish() {

        // Check for Wifi connection and allowed SSIDs
        if (!checkWifiConnection()) {

            return;
        }

        try {

            // Prepare request URL
            StringBuilder requestUrlBuilder = new StringBuilder(m_restFishAddUrl);
            appendParamText(requestUrlBuilder, "authorName", R.id.authorNameText);
            appendParamText(requestUrlBuilder, "fishName", R.id.fishNameText);
            appendParamText(requestUrlBuilder, "title", R.id.flagTitleText);
            appendParamText(requestUrlBuilder, "stringValue", R.id.flagContentText);
            appendParamText(requestUrlBuilder, "fishType", FishItems.getFishTypeNameByIndex(s_fishTypeIndex));
            String requestUrl = requestUrlBuilder.toString();

            // Execute HTTP request
            CallHttpTask task = new CallHttpTask();
            task.execute(requestUrl);

        } catch (Exception e) {
            Log.e(getString(R.string.MainActivityTag), "Error while calling rest interface to add fish.", e);
        }
    }

    /**
     * Feeds fish by calling FishificationFx endpoint.
     */
    @Override
    public void onFeedFish() {

        // Check for Wifi connection and allowed SSIDs
        if (!checkWifiConnection()) {
            return;
        }

        try {
            Gallery fishBoxGallery = (Gallery) findViewById(R.id.fishBoxGallery);
            if (fishBoxGallery == null) {
                return;
            }
            int index = fishBoxGallery.getSelectedItemPosition();
            String source = FishFoodBoxes.getFoodBoxNameByIndex(index);

            // Prepare request URL
            StringBuilder requestUrlBuilder = new StringBuilder(m_restFishFeedUrl);
            if (source != null) {
                appendParamText(requestUrlBuilder, "source", source);
            }
            String requestUrl = requestUrlBuilder.toString();

            // Execute HTTP request
            CallHttpTask task = new CallHttpTask();
            task.execute(requestUrl);

        } catch (Exception e) {
            Log.e(getString(R.string.MainActivityTag), "Error while calling rest interface to feed fish.", e);
        }
    }

    /**
     * Checks for WiFi connection and if SSID is allowed.
     *
     * @return If Wifi connection check was successful.
     */
    private boolean checkWifiConnection() {
        if (!m_wifiCheck) {
            return true;
        }

        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == NetworkInfo.State.CONNECTED) {
            if (m_wifiSSIDs.length == 0) {
                return true;
            }

            WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            String ssid = wifiInfo.getSSID().replace("\"", "").trim();
            for (String s : m_wifiSSIDs) {
                if (ssid.equalsIgnoreCase(s.trim())) {
                    return true;
                }
            }

            Toast.makeText(this, "Not connected to allowed WiFi zones.", Toast.LENGTH_LONG);
            return false;
        }

        Toast.makeText(this, "No WiFi connection available.", Toast.LENGTH_LONG);
        return false;
    }

    private void appendParamText(StringBuilder sb, String paramName, int paramTextViewResource) throws UnsupportedEncodingException {
        if (paramTextViewResource <= 0) {
            return;
        }
        final TextView paramTextView = (TextView) findViewById(paramTextViewResource);
        CharSequence sequence = paramTextView.getText();
        if (sequence == null || sequence.length() == 0) {
            return;
        }
        String paramValue = sequence.toString();
        appendParamText(sb, paramName, paramValue);
    }

    private void appendParamText(StringBuilder sb, String paramName, String paramValue) throws UnsupportedEncodingException {
        if (sb == null || paramName == null) {
            return;
        }
        if (paramName.isEmpty() || paramValue.isEmpty()) {
            return;
        }
        sb.append(String.format("%s=%s&", paramName, URLEncoder.encode(paramValue, "utf-8")));
    }

    @Override
    public void onPageScrolled(int position, float v, int i2) {

    }

    @Override
    public void onPageSelected(int position) {
        getActionBar().setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        m_ViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
