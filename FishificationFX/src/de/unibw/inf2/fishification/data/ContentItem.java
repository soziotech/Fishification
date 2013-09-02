/*
 * ContentItem.java
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
 *     Date: 9/2/13 12:55 AM
 */

package de.unibw.inf2.fishification.data;

import org.sociotech.communitymashup.data.Category;
import org.sociotech.communitymashup.data.Content;
import org.sociotech.communitymashup.data.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Holds a copy from CommunityMashup Content.
 *
 * @author Martin Burkhard
 */
public class ContentItem implements Comparator<ContentItem> {

    private final String m_ident;
    private final String m_flagTitle;
    private final String m_flagContent;
    private       String m_authorName;
    private       String m_authorImageIdent;
    private List<Category> m_categories = new ArrayList<Category>();

    public ContentItem(String ident, String flagTitle, String flagContent, List<Category> categories, String authorName, String authorImageIdent) {
        m_ident = ident;
        m_flagTitle = flagTitle;
        m_flagContent = flagContent;
        m_categories = categories;
        m_authorName = authorName;
        m_authorImageIdent = authorImageIdent;
    }

    public ContentItem(Content content) {
        m_ident = content.getIdent();
        m_flagTitle = content.getName();
        m_flagContent = content.getStringValue();
        m_categories = content.getCategories();
        Person author = content.getAuthor();
        m_authorName = author.getName();
        if (m_authorName == null || m_authorName.isEmpty()) {
            m_authorName = String.format("%s %s", author.getFirstname(), author.getLastname());
        }
        m_authorName = m_authorName.trim();
    }

    String getIdent() {
        return m_ident;
    }

    public String getFlagTitle() {
        return m_flagTitle;
    }

    public String getFlagContent() {
        return m_flagContent;
    }

    public String getAuthorName() {
        return m_authorName;
    }

    public String getAuthorImageIdent() {
        return m_authorImageIdent;
    }

    public List<Category> getCategories() {
        return m_categories;
    }

    @Override
    public int compare(ContentItem contentItem1, ContentItem contentItem2) {

        String ident1 = contentItem1.getIdent();
        String ident2 = contentItem2.getIdent();

        // Ascending order
        return ident1.compareTo(ident2);
    }
}
