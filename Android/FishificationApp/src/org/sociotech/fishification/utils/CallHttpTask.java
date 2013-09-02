/*
 * CallHttpTask.java
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

package org.sociotech.fishification.utils;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Asynchronously sends HTTP GET requests to FishificationFx endpoint.
 */
public class CallHttpTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        for (String url : urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {

                client.execute(httpGet);

            } catch (Exception e) {
                Log.e("HTTP Response", "Error executing HTTP GET.");
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        Log.d("HTTP Response", response);
    }
}