/*
 * CreateFishFragment.java
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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import org.sociotech.fishification.R;
import org.sociotech.fishification.controller.listener.OnFishSelectedListener;
import org.sociotech.fishification.model.listitems.FishItems;
import org.sociotech.fishification.ui.MainActivity;

/**
 * Fragment showing creation form and list of fish.
 */
class CreateFishFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Callback handler
    private OnFishSelectedListener m_fishSelectedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an array adapter for visualizing the fish items
        setListAdapter(new FishListAdapter(getActivity(), FishItems.getFishItems()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_fish, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Assign listener to activity's callback implementation
        m_fishSelectedListener = (OnFishSelectedListener) activity;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Restore preferences
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        String authorName = settings.getString("authorName", null);
        String fishName = settings.getString("fishName", null);
        int fishTypeIndex = settings.getInt("fishTypeIndex", -1);

        // Set Author name
        if (authorName != null && !authorName.isEmpty()) {
            setEditText(R.id.authorNameText, authorName);
        } else {
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
        }

        // Set Fish Name
        if (fishName != null && !fishName.isEmpty()) {
            setEditText(R.id.fishNameText, fishName);
        }

        ListView lv = getListView();
        lv.setCacheColorHint(Color.TRANSPARENT); // Improves scrolling performance
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Set Fish Type
        if (fishTypeIndex != -1) {
            lv.setItemChecked(fishTypeIndex, true);
            m_fishSelectedListener.onFishSelected(fishTypeIndex);
        }
    }

    private void setEditText(int editTextResId, String text) {
        final EditText editText = (EditText) getView().findViewById(editTextResId);
        if (editText != null) {
            editText.setText(text);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        // Notify the parent activity of selected fish item
        m_fishSelectedListener.onFishSelected(position);
        v.setSelected(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        // Retrieve data rows for the device user's 'profile' contact.
        String[] projection = ProfileQuery.EMAIL_ADDRESS_PROJECTION;
        if (id == 0) {
            projection = ProfileQuery.DISPLAY_NAME_PROJECTION;
        }

        // Get Cursor for primary E-Mail address
        return new CursorLoader(getActivity(),

                                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                                                     ContactsContract.Contacts.Data.CONTENT_DIRECTORY), projection,
                                ContactsContract.Contacts.Data.MIMETYPE + " = ?",

                                // Select e-mail address item type
                                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                                // Show primary name first
                                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        String userName = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {

            if (cursorLoader.getId() == 0) {

                userName = cursor.getString(ProfileQuery.DISPLAY_NAME);

            } else {
                userName = cursor.getString(ProfileQuery.ADDRESS);
            }
        }

        if (userName != null && !userName.isEmpty()) {

            // Set UserName
            setEditText(R.id.authorNameText, userName);

        } else if (cursorLoader.getId() == 0) {

            // Select primary e-mail address
            getActivity().getSupportLoaderManager().initLoader(1, null, this);

        } else {

            // Parse accounts for user name or e-mail address
            AccountManager am = AccountManager.get(getActivity());
            Account[] accounts = am.getAccountsByType("XING");
            if (accounts != null && accounts.length > 0) {
                setEditText(R.id.authorNameText, accounts[0].name);
            } else {
                accounts = am.getAccounts();
                if (accounts != null && accounts.length > 0) {
                    setEditText(R.id.authorNameText, accounts[0].name);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] DISPLAY_NAME_PROJECTION  = {ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, ContactsContract.CommonDataKinds.StructuredName.IS_PRIMARY};
        String[] EMAIL_ADDRESS_PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY};
        int      DISPLAY_NAME             = 0;
        int      ADDRESS                  = 0;
        int      IS_PRIMARY               = 1;
    }
}
