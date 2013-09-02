/*
 * ShareActivity.java
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Registers FishificationApp for sharing plain text.
 */
public class ShareActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && "text/plain".equals(type)) {
            handleSendText(intent); // Handle text being sent
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null && !sharedText.isEmpty()) {
            Intent newIntent = new Intent(this, MainActivity.class);
            newIntent.putExtra(MainActivity.EXTRA_SHARED_TEXT, sharedText);
            startActivity(newIntent);
        }
    }
}