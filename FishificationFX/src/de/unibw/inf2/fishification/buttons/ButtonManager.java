/*
 * ButtonManager.java
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
 *     Date: 9/2/13 11:25 AM
 */

package de.unibw.inf2.fishification.buttons;

import de.unibw.inf2.fishification.FishWorld;
import de.unibw.inf2.fishification.categories.CategoryType;
import de.unibw.inf2.fishification.categories.PersonType;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.sociotech.unui.javafx.engine2d.buttons.ButtonPaneFactory;
import org.sociotech.unui.javafx.engine2d.buttons.SyncedToggleButton;
import org.sociotech.unui.javafx.engine2d.buttons.SyncedToggleGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Lays out Fishification Buttons
 */
public class ButtonManager {

    private static final double BORDER_SPACING        = 10;
    private static final double BORDER_SPACING_TOP    = 130;
    private static final double BORDER_SPACING_BOTTOM = 10;
    private static final int    BUTTON_SPACING        = 5;
    private static final String BUTTON_STYLE_FORMAT   = "-fx-font: %d \"%s\"; -fx-base: lightblue;";

    private final FishWorld               m_fishWorld;
    private final AnchorPane m_layoutPane = new AnchorPane();

    // Single Button (default)
    private static String s_buttonStyle;
    private static int s_buttonWidth  = 120;
    private static int s_buttonHeight = 40;

    private ButtonFactory m_buttonFactory;
    private TilePane      m_categoryButtonsLeft;
    private TilePane      m_categoryButtonsRight;
    private CentricButton m_personCentric;
    private CentricButton m_sourceCentric;
    private PauseButton   m_pauseButton;
    private RefreshButton m_refreshButton;

    private final Object m_syncCategoryButtonAccess = new Object();

    public ButtonManager(PropertiesConfiguration configuration, FishWorld fishWorld) {
        PropertiesConfiguration config = configuration;
        m_fishWorld = fishWorld;

        // Init Layout Pane
        m_layoutPane.setPrefSize(m_fishWorld.getWidth(), m_fishWorld.getHeight());
        fishWorld.addNode(m_layoutPane);

        String textFontType = config.getString("fishification.text.font.type");
        int textFontSize = config.getInt("fishification.text.font.size");

        s_buttonStyle = String.format(BUTTON_STYLE_FORMAT, textFontSize, textFontType);
        s_buttonWidth = config.getInt("fishification.button.width");
        s_buttonHeight = config.getInt("fishification.button.height");
    }

    public void initialize() {

        m_buttonFactory = new ButtonFactory(m_fishWorld, s_buttonStyle, s_buttonWidth, s_buttonHeight);

        m_sourceCentric = m_buttonFactory.createCentricButton("source-centric");
        m_personCentric = m_buttonFactory.createCentricButton("person-centric");

        // Init category buttons
        initCategoryButtons();

        // Init utilities Buttons
        initUtilButtons();
    }

    /**
     * Initializes source- or person-centric category buttons.
     */
    public void initCategoryButtons() {

        // Create Buttons
        List<SyncedToggleButton> buttonList1;
        List<SyncedToggleButton> buttonList2;

        // Create Source- or Person-Centric Button Lists
        if (m_fishWorld.isPersonCentric()) {
            buttonList1 = createPersonCategoryButtons();
            buttonList2 = createPersonCategoryButtons();
        } else {
            buttonList1 = createSourceCategoryButtons();
            buttonList2 = createSourceCategoryButtons();
        }

        // Sync all buttons
        for (int i = 0; i < buttonList1.size(); i++) {
            SyncedToggleButton button1 = buttonList1.get(i);
            SyncedToggleButton button2 = buttonList2.get(i);
            SyncedToggleGroup syncedToggleGroup = new SyncedToggleGroup();
            button1.setSyncedToggleGroup(syncedToggleGroup);
            button2.setSyncedToggleGroup(syncedToggleGroup);
        }

        synchronized (m_syncCategoryButtonAccess) {
            m_categoryButtonsLeft = ButtonPaneFactory.createTilePane(BUTTON_SPACING, buttonList1, Orientation.VERTICAL);
            m_categoryButtonsRight = ButtonPaneFactory.createTilePane(BUTTON_SPACING, buttonList2,
                                                                      Orientation.VERTICAL);

            // Assign to Layout Pane
            AnchorPane.setTopAnchor(m_categoryButtonsLeft, BORDER_SPACING_TOP);
            AnchorPane.setTopAnchor(m_categoryButtonsRight, BORDER_SPACING_TOP);
            AnchorPane.setLeftAnchor(m_categoryButtonsLeft, BORDER_SPACING);
            AnchorPane.setRightAnchor(m_categoryButtonsRight, BORDER_SPACING);
            AnchorPane.setBottomAnchor(m_categoryButtonsLeft, BORDER_SPACING);
            AnchorPane.setBottomAnchor(m_categoryButtonsRight, BORDER_SPACING);
            m_layoutPane.getChildren().addAll(m_categoryButtonsLeft, m_categoryButtonsRight);
        }
    }

    /**
     * Initializes utilities buttons.
     */
    private void initUtilButtons() {

        // Create Buttons and Pane
        createCentricButtons();
        createPauseButton();
        createRefreshButton();
        TilePane utilButtonsPane = createUtilPane();

        // Align and assign to Layout Pane
        AnchorPane.setLeftAnchor(utilButtonsPane, BORDER_SPACING);
        AnchorPane.setRightAnchor(utilButtonsPane, BORDER_SPACING);
        AnchorPane.setBottomAnchor(utilButtonsPane, BORDER_SPACING_BOTTOM);
        utilButtonsPane.setAlignment(Pos.BOTTOM_CENTER);
        m_layoutPane.getChildren().add(utilButtonsPane);

    }

    /**
     * Removes source- or person-centric category buttons.
     */
    public void removeCategoryButtons() {
        synchronized (m_syncCategoryButtonAccess) {
            clearRemovePane(m_categoryButtonsLeft);
            clearRemovePane(m_categoryButtonsRight);
        }
    }

    private void clearRemovePane(Pane pane) {
        pane.getChildren().clear();
        m_fishWorld.removeNode(pane);
    }

    private void createCentricButtons() {

        ToggleGroup group = new ToggleGroup();
        m_sourceCentric.setToggleGroup(group);
        m_personCentric.setToggleGroup(group);

        boolean isPersonCentric = m_fishWorld.isPersonCentric();
        m_sourceCentric.setSelected(!isPersonCentric);
        m_sourceCentric.setDisable(!isPersonCentric);
        m_personCentric.setSelected(isPersonCentric);
        m_personCentric.setDisable(isPersonCentric);

    }

    private void createPauseButton() {
        List<Control> controlList = new ArrayList<Control>();
        controlList.add(m_sourceCentric);
        controlList.add(m_personCentric);
        m_pauseButton = m_buttonFactory.createPauseButton("pause", "resume", controlList);
    }

    private void createRefreshButton() {
        m_refreshButton = m_buttonFactory.createRefreshButton("refresh data");
    }

    private TilePane createUtilPane() {

        List<ButtonBase> buttonList = new ArrayList<ButtonBase>();
        buttonList.add(m_sourceCentric);
        buttonList.add(m_personCentric);
        buttonList.add(m_pauseButton);
        buttonList.add(m_refreshButton);
        return ButtonPaneFactory.createTilePane(BUTTON_SPACING, buttonList, Orientation.HORIZONTAL);
    }

    private List<SyncedToggleButton> createSourceCategoryButtons() {

        List<SyncedToggleButton> buttonList = new ArrayList<SyncedToggleButton>();

        buttonList.add(m_buttonFactory.createCategoryButton(CategoryType.CSCM, "CSCM", "CSCM_Fish_Button.png"));
        buttonList.add(
                m_buttonFactory.createCategoryButton(CategoryType.MENDELEY, "MENDELEY", "Mendeley_Fish_Button.png"));
        buttonList.add(
                m_buttonFactory.createCategoryButton(CategoryType.SOCIOTECH, "SOCIOTECH", "Sociotech_Fish_Button.png"));
        buttonList.add(m_buttonFactory.createCategoryButton(CategoryType.STUDIENDEKAN, "STUDIENDEKAN",
                                                            "Studiendekan_Fish_Button.png"));
        buttonList.add(
                m_buttonFactory.createCategoryButton(CategoryType.TWITTER, "TWITTER", "Twitter_Fish_Button.png"));
        buttonList.add(m_buttonFactory.createCategoryButton(CategoryType.UNIBWM, "UNIBWM", "UniBwM_Fish_Button.png"));

        return buttonList;
    }

    List<SyncedToggleButton> createPersonCategoryButtons() {

        List<SyncedToggleButton> buttonList = new ArrayList<SyncedToggleButton>();

        for (String s : PersonType.getPersons()) {
            SyncedToggleButton b;
            if (PersonType.indexOf(s) == 0) {
                b = m_buttonFactory.createCategoryButton(CategoryType.LADY, s, "Lady_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 1) {
                b = m_buttonFactory.createCategoryButton(CategoryType.GLASSES, s, "Glasses_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 2) {
                b = m_buttonFactory.createCategoryButton(CategoryType.MARKTHESHARK, s, "Mark_the_Shark_Button.png");
            } else if (PersonType.indexOf(s) == 3) {
                b = m_buttonFactory.createCategoryButton(CategoryType.OCTOPUS, s, "Octopus_Button.png");
            } else if (PersonType.indexOf(s) == 4) {
                b = m_buttonFactory.createCategoryButton(CategoryType.PINKY, s, "Pinky_Button.png");
            } else if (PersonType.indexOf(s) == 5) {
                b = m_buttonFactory.createCategoryButton(CategoryType.SEAHORSE, s, "Seahorse_Button.png");
            } else if (PersonType.indexOf(s) == 6) {
                b = m_buttonFactory.createCategoryButton(CategoryType.RED, s, "Red_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 7) {
                b = m_buttonFactory.createCategoryButton(CategoryType.KOFFER, s, "Koffer_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 8) {
                b = m_buttonFactory.createCategoryButton(CategoryType.BURBOT, s, "Burbot_Button.png");
            } else if (PersonType.indexOf(s) == 9) {
                b = m_buttonFactory.createCategoryButton(CategoryType.UNIBWM, s, "UniBwM_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 10) {
                b = m_buttonFactory.createCategoryButton(CategoryType.CSCM, s, "Cscm_Fish.png");
            } else if (PersonType.indexOf(s) == 11) {
                b = m_buttonFactory.createCategoryButton(CategoryType.STUDIENDEKAN, s, "Studiendekan_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 12) {
                b = m_buttonFactory.createCategoryButton(CategoryType.SOCIOTECH, s, "Sociotech_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 13) {
                b = m_buttonFactory.createCategoryButton(CategoryType.TWITTER, s, "Twitter_Fish_Button.png");
            } else if (PersonType.indexOf(s) == 14) {
                b = m_buttonFactory.createCategoryButton(CategoryType.MENDELEY, s, "Mendeley_Fish_Button.png");
            } else {
                b = m_buttonFactory.createCategoryButton(CategoryType.CHRISTMAS, s, "Christmas_Fish_Button.png");
            }
            if (b != null) {
                buttonList.add(b);
            }
        }

        return buttonList;
    }

    public void toggleSourcePersonCentric() {
        boolean personCentric = m_fishWorld.toggleSourcePersonCentric();

        m_personCentric.setDisable(personCentric);
        m_personCentric.setSelected(false);
        m_sourceCentric.setDisable(!personCentric);
        m_sourceCentric.setSelected(false);
    }
}
