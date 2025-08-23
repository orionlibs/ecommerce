/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.renderers.header.DefaultWidgetCaptionWrapper;
import com.hybris.cockpitng.renderers.header.WidgetCaptionWrapper;
import com.hybris.cockpitng.renderers.header.WidgetVisibilityState;
import com.hybris.cockpitng.renderers.header.WidgetVisibilityStateAware;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetSlotUtils;
import com.hybris.cockpitng.widgets.controller.collapsiblecontainer.CollapsibleContainerState;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.North;
import org.zkoss.zul.South;

/**
 * A two/three section container which slots' widgets can propagate their title to the enclosing container section.
 */
public class CollapsibleContainerController extends DefaultWidgetController
{
    public static final String NORTH_BUTTON = "northButton";
    public static final String OUTER_NORTH_BUTTON = "outerNorthButton";
    public static final String SOUTH_BUTTON = "southButton";
    public static final String CENTER_BUTTON = "centerButton";
    // model related to panel states
    public static final String MODEL_TOP_PANEL_COLLAPSED = "topPanelCollapsed";
    public static final String MODEL_CENTER_PANEL_COLLAPSED = "centerPanelCollapsed";
    public static final String MODEL_BOTTOM_PANEL_COLLAPSED = "bottomPanelCollapsed";
    // sticky buttons
    public static final String NORTH_STICKY_BUTTON = "northStickyButton";
    public static final String OUTER_NORTH_STICKY_BUTTON = "outerNorthStickyButton";
    public static final String SOUTH_STICKY_BUTTON = "southStickyButton";
    public static final String CENTER_STICKY_BUTTON = "centerStickyButton";
    // model related to sticky buttons
    protected static final String MODEL_NORTH_STICKY_BUTTON_ON = "northStickyButtonOn";
    protected static final String MODEL_BOTTOM_STICKY_BUTTON_ON = "bottomStickyButtonOn";
    protected static final String MODEL_CENTER_STICKY_BUTTON_ON = "centerStickyButtonOn";
    protected static final String SETTING_ALLOW_LOCKING_TOP_PANEL = "allowLockingTopPanel";
    protected static final String SETTING_ALLOW_LOCKING_CENTER_PANEL = "allowLockingCenterPanel";
    protected static final String SETTING_ALLOW_LOCKING_BOTTOM_PANEL = "allowLockingBottomPanel";
    protected static final String SETTING_TOP_PANEL_INITIALLY_LOCKED = "topPanelInitiallyLocked";
    protected static final String SETTING_CENTER_PANEL_INITIALLY_LOCKED = "centerPanelInitiallyLocked";
    protected static final String SETTING_BOTTOM_PANEL_INITIALLY_LOCKED = "bottomPanelInitiallyLocked";
    private static final String STYLE_DISPLAY_NONE = "display:none";
    private static final String CSS_YW_COLLAPSED_DOWN = "yw-collapsed-down";
    private static final String SIZE_BEFORE_COLLAPSED_ATTRIBUTE = "sizeBeforeCollapsed";
    private static final long serialVersionUID = 1L;
    /**
     * If third section is disabled, we dispay CENTER and BOTTOM panel
     */
    private static final String THIRD_SECTION_DISABLED_SETTING = "thirdSectionDisabled";
    private static final String TOP_CAPTION_SETTING = "topCaption";
    private static final String CENTER_CAPTION_SETTING = "centerCaption";
    private static final String BOTTOM_CAPTION_SETTING = "bottomCaption";
    private static final String STICKY_BUTTON_CSS_ON = "yw-collapsibleContainer-locked";
    private static final String STICKY_BUTTON_CSS_OFF = "yw-collapsibleContainer-unlocked";
    private static final String TEMPORARY_CONTENT_HOLDER_ATTR = "temporaryContentHolder";
    @Wire
    private Borderlayout borderlayout;
    @Wire
    private Borderlayout outerBorderlayout;
    @Wire
    private North outerNorth;
    @Wire
    private North north;
    @Wire
    private South south;
    @Wire
    private Center center;
    @Wire
    private Div topContainer;
    @Wire
    private Div bottomContainer;
    @Wire
    private Div outerTopContainer;
    @Wire
    private Button centerButton;
    @Wire
    private Button outerNorthButton;
    @Wire
    private Button northButton;
    @Wire
    private Button southButton;
    @Wire
    private Div northCaptionContainer;
    @Wire
    private Div centerCaptionContainer;
    @Wire
    private Div southCaptionContainer;
    @Wire
    private Div outerNorthCaptionContainer;
    @Wire
    private Div centerCaptionContainerInner;
    @Wire
    private Div northCaptionContainerInner;
    @Wire
    private transient DefaultWidgetCaptionWrapper topCaptionWrapper;
    @Wire
    private transient DefaultWidgetCaptionWrapper centerCaptionWrapper;
    @Wire
    private transient DefaultWidgetCaptionWrapper bottomCaptionWrapper;
    @Wire
    private Button outerNorthStickyButton;
    @Wire
    private Button northStickyButton;
    @Wire
    private Button centerStickyButton;
    @Wire
    private Button southStickyButton;
    private boolean thirdAreaDisabled;
    private boolean centerButtonHidden;
    private boolean bottomButtonHidden;


    @Override
    public void initialize(final Component comp)
    {
        final Widgetslot topSlot = getWidgetSlotByContainer(outerTopContainer);
        final Widgetslot centerSlot = getWidgetSlotByContainer(topContainer);
        final Widgetslot bottomSlot = getWidgetSlotByContainer(bottomContainer);
        this.thirdAreaDisabled = getWidgetSettings().getBoolean(THIRD_SECTION_DISABLED_SETTING);
        final boolean topPanelCollapsed = initializePanelState(MODEL_TOP_PANEL_COLLAPSED);
        final boolean centerPanelCollapsed = initializePanelState(MODEL_CENTER_PANEL_COLLAPSED);
        final boolean bottomPanelCollapsed = initializePanelState(MODEL_BOTTOM_PANEL_COLLAPSED);
        initializeStickyButtonsState();
        addPanelStateModelObservers();
        topCaptionWrapper = new DefaultWidgetCaptionWrapper((eventName, wrapper) -> {
            if(WidgetCaptionWrapper.ON_WIDGET_COLLAPSE.equals(eventName))
            {
                onOuterNorthButtonClickedInternal(false);
            }
        }, false, false, false, topPanelCollapsed);
        topCaptionWrapper.setCollapsible(true);
        centerCaptionWrapper = new DefaultWidgetCaptionWrapper((eventName, wrapper) -> {
            if(WidgetCaptionWrapper.ON_WIDGET_COLLAPSE.equals(eventName))
            {
                if(south.isVisible())
                {
                    onCenterButtonClickedInternal(false);
                }
                else
                {
                    onNorthButtonClickedInternal(false);
                }
            }
        }, false, false, false, centerPanelCollapsed);
        centerCaptionWrapper.setCollapsible(north.isVisible());
        bottomCaptionWrapper = new DefaultWidgetCaptionWrapper((eventName, wrapper) -> {
            if(WidgetCaptionWrapper.ON_WIDGET_COLLAPSE.equals(eventName))
            {
                if(south.isVisible())
                {
                    onSouthButtonClickedInternal(false);
                }
                else
                {
                    onCenterButtonClickedInternal(false);
                }
            }
        }, false, false, false, bottomPanelCollapsed);
        bottomCaptionWrapper
                        .setCollapsible(BooleanUtils.isNotTrue(getModel().getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class)));
        if(thirdAreaDisabled)
        {
            borderlayout.setParent(outerBorderlayout.getParent());
            outerBorderlayout.detach();
            northCaptionContainerInner
                            .appendChild(WidgetSlotUtils.createCaption(centerSlot, getCaption(TOP_CAPTION_SETTING), centerCaptionWrapper));
        }
        else
        {
            outerNorthCaptionContainer
                            .appendChild(WidgetSlotUtils.createCaption(topSlot, getCaption(TOP_CAPTION_SETTING), topCaptionWrapper));
            outerNorthButton.setVisible(!((WidgetCaptionWrapper.CONTROL_COLLAPSE
                            & topCaptionWrapper.getHiddenContainerControls()) == WidgetCaptionWrapper.CONTROL_COLLAPSE));
            northCaptionContainerInner
                            .appendChild(WidgetSlotUtils.createCaption(centerSlot, getCaption(CENTER_CAPTION_SETTING), centerCaptionWrapper));
        }
        centerCaptionContainerInner
                        .appendChild(WidgetSlotUtils.createCaption(bottomSlot, getCaption(BOTTOM_CAPTION_SETTING), bottomCaptionWrapper));
        centerButtonHidden = ((WidgetCaptionWrapper.CONTROL_COLLAPSE
                        & centerCaptionWrapper.getHiddenContainerControls()) == WidgetCaptionWrapper.CONTROL_COLLAPSE);
        northButton.setVisible(!centerButtonHidden);
        updateNorthBtnSclass();
        bottomButtonHidden = ((WidgetCaptionWrapper.CONTROL_COLLAPSE
                        & bottomCaptionWrapper.getHiddenContainerControls()) == WidgetCaptionWrapper.CONTROL_COLLAPSE);
        centerButton.setVisible(!bottomButtonHidden);
        southButton.setVisible(!bottomButtonHidden);
        appendTemporaryContentHolder(comp);
        restoreLayout(topPanelCollapsed, centerPanelCollapsed, bottomPanelCollapsed);
    }


    private Widgetslot getWidgetSlotByContainer(final Div container)
    {
        Widgetslot result = null;
        if(container != null)
        {
            final Component firstChild = container.getFirstChild();
            if(firstChild instanceof Widgetslot)
            {
                result = (Widgetslot)firstChild;
            }
        }
        return result;
    }


    @SocketEvent(socketId = "collapseState")
    public void changePanelCollapseState(final CollapsibleContainerState collapsibleContainerState)
    {
        boolean changedTop = false;
        boolean changedCenter = false;
        boolean changedBottom = false;
        if(collapsibleContainerState != null)
        {
            changedTop = isChangeCollapse(collapsibleContainerState.getTopPanelCollapsed(), MODEL_NORTH_STICKY_BUTTON_ON, MODEL_TOP_PANEL_COLLAPSED);
            changedCenter = isChangeCollapse(collapsibleContainerState.getCenterPanelCollapsed(), MODEL_CENTER_STICKY_BUTTON_ON, MODEL_CENTER_PANEL_COLLAPSED);
            changedBottom = isChangeCollapse(collapsibleContainerState.getBottomPanelCollapsed(), MODEL_BOTTOM_STICKY_BUTTON_ON, MODEL_BOTTOM_PANEL_COLLAPSED);
            if(areAllSectionsCollapsed())
            {
                // suppress locking if all collapsed
                if(collapsibleContainerState.getTopPanelCollapsed() != null && ObjectUtils
                                .notEqual(collapsibleContainerState.getTopPanelCollapsed(), getValue(MODEL_TOP_PANEL_COLLAPSED, Boolean.class)))
                {
                    setValue(MODEL_TOP_PANEL_COLLAPSED, collapsibleContainerState.getTopPanelCollapsed());
                    changedTop = true;
                }
                if(collapsibleContainerState.getCenterPanelCollapsed() != null && ObjectUtils.notEqual(
                                collapsibleContainerState.getCenterPanelCollapsed(), getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class)))
                {
                    setValue(MODEL_CENTER_PANEL_COLLAPSED, collapsibleContainerState.getCenterPanelCollapsed());
                    changedCenter = true;
                }
                if(collapsibleContainerState.getBottomPanelCollapsed() != null && ObjectUtils.notEqual(
                                collapsibleContainerState.getBottomPanelCollapsed(), getValue(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.class)))
                {
                    setValue(MODEL_BOTTOM_PANEL_COLLAPSED, collapsibleContainerState.getBottomPanelCollapsed());
                    changedBottom = true;
                }
            }
            handleChangeCollapse(changedTop, changedCenter, changedBottom);
        }
    }


    private boolean isChangeCollapse(Boolean panelCollapsedRequest, String stickyButtonOnKey, String panelCollapsedKey)
    {
        if(panelCollapsedRequest != null)
        {
            final Boolean topPanelIsLocked = getValue(stickyButtonOnKey, Boolean.class);
            if(!topPanelIsLocked
                            && !panelCollapsedRequest.equals(getValue(panelCollapsedKey, Boolean.class)))
            {
                setValue(panelCollapsedKey, panelCollapsedRequest);
                return true;
            }
        }
        return false;
    }


    private void handleChangeCollapse(boolean changedTop, boolean changedCenter, boolean changedBottom)
    {
        if(changedTop || changedCenter || changedBottom)
        {
            restoreLayout(getValue(MODEL_TOP_PANEL_COLLAPSED, Boolean.class),
                            getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class), getValue(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.class));
        }
        if(changedTop)
        {
            notifyCaptionRenderers(topCaptionWrapper, getValue(MODEL_TOP_PANEL_COLLAPSED, Boolean.class), true);
        }
        if(changedCenter)
        {
            notifyCaptionRenderers(centerCaptionWrapper, getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class), true);
        }
        if(changedBottom)
        {
            notifyCaptionRenderers(bottomCaptionWrapper, getValue(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.class), true);
        }
    }


    protected boolean areAllSectionsCollapsed()
    {
        return BooleanUtils.isTrue(getValue(MODEL_TOP_PANEL_COLLAPSED, Boolean.class))
                        && BooleanUtils.isTrue(getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class))
                        && BooleanUtils.isTrue(getValue(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.class));
    }


    private boolean initializePanelState(final String key)
    {
        final Boolean panelCollapsed = getModel().getValue(key, Boolean.class);
        if(panelCollapsed == null)
        {
            setValue(key, getWidgetSettings().getBoolean(key));
        }
        return Boolean.TRUE.equals(getModel().getValue(key, Boolean.class));
    }


    @ViewEvent(componentID = OUTER_NORTH_BUTTON, eventName = Events.ON_CLICK)
    public void onOuterNorthButtonClicked()
    {
        onOuterNorthButtonClickedInternal(true);
    }


    private void onOuterNorthButtonClickedInternal(final boolean sendToggleNotification)
    {
        final boolean topCollapsed = !outerTopContainer.isVisible();
        if(topCollapsed)
        {
            showTopPanel();
            getModel().put(MODEL_TOP_PANEL_COLLAPSED, Boolean.FALSE);
        }
        else
        {
            hideTopPanel();
            getModel().put(MODEL_TOP_PANEL_COLLAPSED, Boolean.TRUE);
        }
        outerNorth.setSplittable(topCollapsed);
        UITools.modifySClass(outerNorth, CSS_YW_COLLAPSED_DOWN, !topCollapsed);
        notifyCaptionRenderers(topCaptionWrapper, !topCollapsed, sendToggleNotification);
    }


    @ViewEvent(componentID = NORTH_BUTTON, eventName = Events.ON_CLICK)
    public void onNorthButtonClicked()
    {
        onNorthButtonClickedInternal(true);
    }


    private void onNorthButtonClickedInternal(final boolean sendToggleNotification)
    {
        final boolean centerPanelCollapsed = isCenterPanelCollapsed();
        if(centerPanelCollapsed)
        {
            getModel().put(MODEL_CENTER_PANEL_COLLAPSED, Boolean.FALSE);
            showCenterPanel();
        }
        else
        {
            getModel().put(MODEL_CENTER_PANEL_COLLAPSED, Boolean.TRUE);
            north.setAttribute(SIZE_BEFORE_COLLAPSED_ATTRIBUTE, north.getSize());
            hideCenterPanel();
        }
        updateNorthBtnSclass();
        UITools.modifySClass(north, CSS_YW_COLLAPSED_DOWN, !centerPanelCollapsed);
        north.setSplittable(centerPanelCollapsed);
        notifyCaptionRenderers(centerCaptionWrapper, !centerPanelCollapsed, sendToggleNotification);
    }


    private boolean isCenterPanelCollapsed()
    {
        return Boolean.TRUE.equals(getModel().getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class));
    }


    private void updateNorthBtnSclass()
    {
        if(isCenterPanelCollapsed())
        {
            northButton.addSclass("yw-expandCollapse-icon-expand");
            northButton.removeSclass("yw-expandCollapse-icon-collapse");
        }
        else
        {
            northButton.addSclass("yw-expandCollapse-icon-collapse");
            northButton.removeSclass("yw-expandCollapse-icon-expand");
        }
    }


    @ViewEvent(componentID = CENTER_BUTTON, eventName = Events.ON_CLICK)
    public void onCenterButtonClicked()
    {
        onCenterButtonClickedInternal(true);
    }


    private void onCenterButtonClickedInternal(final boolean sendToggleNotification)
    {
        getModel().put(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.TRUE); // OK
        hideSouthPanel();
        updateNorthBtnSclass();
        notifyCaptionRenderers(bottomCaptionWrapper, true, sendToggleNotification);
    }


    @ViewEvent(componentID = SOUTH_BUTTON, eventName = Events.ON_CLICK)
    public void onSouthButtonClicked()
    {
        onSouthButtonClickedInternal(true);
    }


    private void onSouthButtonClickedInternal(final boolean sendToggleNotification)
    {
        showSouthPanel();
        getModel().put(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.FALSE);
        updateNorthBtnSclass();
        notifyCaptionRenderers(bottomCaptionWrapper, false, sendToggleNotification);
    }


    private void notifyCaptionRenderers(final DefaultWidgetCaptionWrapper collapseWrapper, final boolean state,
                    final boolean sendCollapse)
    {
        if(sendCollapse)
        {
            collapseWrapper.sendEvent(WidgetCaptionWrapper.ON_WIDGET_COLLAPSE, state);
        }
        topCaptionWrapper.sendEvent(WidgetCaptionWrapper.ON_CONTROL_STATE_CHANGE, true);
        centerCaptionWrapper.sendEvent(WidgetCaptionWrapper.ON_CONTROL_STATE_CHANGE, true);
        bottomCaptionWrapper.sendEvent(WidgetCaptionWrapper.ON_CONTROL_STATE_CHANGE, true);
    }


    public boolean isThirdAreaDisabled()
    {
        return thirdAreaDisabled;
    }


    public void setThirdAreaDisabled(final boolean thirdAreaDisabled)
    {
        this.thirdAreaDisabled = thirdAreaDisabled;
    }


    private void restoreLayout(final boolean topPanelCollapsed, final boolean centerPanelCollapsed,
                    final boolean bottomPanelCollapsed)
    {
        if(topPanelCollapsed)
        {
            hideTopPanel();
        }
        else
        {
            showTopPanel();
        }
        UITools.modifySClass(outerNorth, CSS_YW_COLLAPSED_DOWN, !outerTopContainer.isVisible());
        outerNorth.setSplittable(!topPanelCollapsed);
        if(centerPanelCollapsed)
        {
            hideCenterPanel();
        }
        else
        {
            showCenterPanel();
        }
        UITools.modifySClass(north, CSS_YW_COLLAPSED_DOWN, centerPanelCollapsed);
        north.setSplittable(!centerPanelCollapsed);
        if(bottomPanelCollapsed)
        {
            hideSouthPanel();
        }
        else
        {
            showSouthPanel();
        }
        notifyCaptionRenderers(null, true, false);
    }


    private void setTopCollapsed(final boolean collapsed)
    {
        // can't use setVisible() because of some client side rendering issues
        topContainer.setStyle(collapsed ? STYLE_DISPLAY_NONE : null);
    }


    private void showTopPanel()
    {
        getOuterTopContainer().setVisible(true);
        String size = (String)outerNorth.getAttribute(SIZE_BEFORE_COLLAPSED_ATTRIBUTE);
        if(size == null)
        {
            size = getWidgetSettings().getString("topHeight");
        }
        outerNorth.setSize(size);
        notifyVisibilityState(getOuterTopContainer(), WidgetVisibilityState.VISIBLE);
    }


    private void hideTopPanel()
    {
        outerNorth.setAttribute(SIZE_BEFORE_COLLAPSED_ATTRIBUTE, outerNorth.getSize());
        outerTopContainer.setVisible(false);
        outerNorth.setSize(null);
        outerBorderlayout.invalidate();
        notifyVisibilityState(getOuterTopContainer(), WidgetVisibilityState.HIDDEN);
    }


    private void hideCenterPanel()
    {
        setTopCollapsed(true);
        north.setSize(null);
        north.invalidate();
        centerButton.setDisabled(true);
        bottomCaptionWrapper.setCollapsible(false);
        notifyVisibilityState(getTopContainer(), WidgetVisibilityState.HIDDEN);
    }


    private void showCenterPanel()
    {
        setTopCollapsed(false);
        String size = (String)north.getAttribute(SIZE_BEFORE_COLLAPSED_ATTRIBUTE);
        if(size == null)
        {
            size = getWidgetSettings().getString("centerHeight");
        }
        north.setSize(size);
        centerButton.setDisabled(false);
        bottomCaptionWrapper.setCollapsible(true);
        notifyVisibilityState(getTopContainer(), WidgetVisibilityState.VISIBLE);
    }


    private void showSouthPanel()
    {
        // shift content up
        north.setVisible(true);
        south.setVisible(false);
        getBottomContainer().setParent(getTemporaryContentHolder());
        getTopContainer().setParent(north);
        getBottomContainer().setParent(center);
        getBottomContainer().setVisible(true);
        northCaptionContainerInner.setParent(northCaptionContainer);
        centerCaptionContainerInner.setParent(centerCaptionContainer);
        UITools.modifySClass(center, CSS_YW_COLLAPSED_DOWN, true);
        centerButton.setDisabled(getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class));
        centerCaptionWrapper.setCollapsible(true);
        centerButton.setVisible(!bottomButtonHidden);
        northButton.setVisible(!centerButtonHidden);
        notifyVisibilityState(getBottomContainer(), WidgetVisibilityState.VISIBLE);
    }


    private void hideSouthPanel()
    {
        north.setVisible(false);
        south.setVisible(true);
        getBottomContainer().setParent(getTemporaryContentHolder());
        getTopContainer().setParent(center);
        getBottomContainer().setParent(south);
        getBottomContainer().setVisible(false);
        northCaptionContainerInner.setParent(centerCaptionContainer);
        centerCaptionContainerInner.setParent(southCaptionContainer);
        UITools.modifySClass(center, CSS_YW_COLLAPSED_DOWN, false);
        centerButton.setDisabled(true);
        centerCaptionWrapper.setCollapsible(false);
        centerButton.setVisible(!centerButtonHidden);
        notifyVisibilityState(getBottomContainer(), WidgetVisibilityState.HIDDEN);
    }


    private void notifyVisibilityState(final Div container, final WidgetVisibilityState visibilityState)
    {
        final Widgetslot widgetSlot = getWidgetSlotByContainer(container);
        final WidgetController widgetController = widgetSlot != null ? widgetSlot.getViewController() : null;
        if(widgetController instanceof WidgetVisibilityStateAware)
        {
            ((WidgetVisibilityStateAware)widgetController).handleVisibilityState(visibilityState);
        }
    }


    private String getCaption(final String captionSettingKey)
    {
        final String settingValue = getWidgetSettings().getString(captionSettingKey);
        if(settingValue == null)
        {
            return null;
        }
        final String localizedValue = Labels.getLabel(settingValue);
        if(localizedValue != null)
        {
            return localizedValue;
        }
        return settingValue;
    }


    private void initializeStickyButtonsState()
    {
        if(getValue(MODEL_NORTH_STICKY_BUTTON_ON, Boolean.class) == null)
        {
            setValue(MODEL_NORTH_STICKY_BUTTON_ON, getWidgetSettings().getBoolean(SETTING_TOP_PANEL_INITIALLY_LOCKED));
        }
        if(getValue(MODEL_CENTER_STICKY_BUTTON_ON, Boolean.class) == null)
        {
            setValue(MODEL_CENTER_STICKY_BUTTON_ON, getWidgetSettings().getBoolean(SETTING_CENTER_PANEL_INITIALLY_LOCKED));
        }
        if(getValue(MODEL_BOTTOM_STICKY_BUTTON_ON, Boolean.class) == null)
        {
            setValue(MODEL_BOTTOM_STICKY_BUTTON_ON, getWidgetSettings().getBoolean(SETTING_BOTTOM_PANEL_INITIALLY_LOCKED));
        }
        updateStickyButtonState();
    }


    private void updateStickyButtonVisibility()
    {
        if(!getWidgetSettings().getBoolean(SETTING_ALLOW_LOCKING_TOP_PANEL))
        {
            if(getNorthStickyButton() != null)
            {
                getNorthStickyButton().setVisible(false);
            }
        }
        else
        {
            if(getNorthStickyButton() != null)
            {
                getNorthStickyButton().setVisible(true);
            }
        }
        if(!getWidgetSettings().getBoolean(SETTING_ALLOW_LOCKING_CENTER_PANEL))
        {
            getCenterStickyButton().setVisible(false);
        }
        else
        {
            getCenterStickyButton().setVisible(true);
        }
        if(!getWidgetSettings().getBoolean(SETTING_ALLOW_LOCKING_BOTTOM_PANEL))
        {
            getBottomStickyButton().setVisible(false);
        }
        else
        {
            getBottomStickyButton().setVisible(true);
        }
    }


    protected Button getNorthStickyButton()
    {
        if(getWidgetSettings().getBoolean(THIRD_SECTION_DISABLED_SETTING))
        {
            return null;
        }
        else
        {
            return outerNorthStickyButton;
        }
    }


    protected Button getCenterStickyButton()
    {
        if(!getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class) && getValue(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.class))
        {
            return centerStickyButton;
        }
        else
        {
            return northStickyButton;
        }
    }


    protected Button getBottomStickyButton()
    {
        if(!getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class) && getValue(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.class))
        {
            return southStickyButton;
        }
        else
        {
            return centerStickyButton;
        }
    }


    private void updateStickyButtonState()
    {
        updateStickyButtonVisibility();
        final Button tempNorthButton = getNorthStickyButton();
        final Button tempCenterButton = getCenterStickyButton();
        final Button tempBottomButton = getBottomStickyButton();
        if(tempNorthButton != null && tempNorthButton.isVisible())
        {
            if(getValue(MODEL_NORTH_STICKY_BUTTON_ON, Boolean.class))
            {
                UITools.modifySClass(tempNorthButton, STICKY_BUTTON_CSS_ON, true);
                UITools.modifySClass(tempNorthButton, STICKY_BUTTON_CSS_OFF, false);
            }
            else
            {
                UITools.modifySClass(tempNorthButton, STICKY_BUTTON_CSS_ON, false);
                UITools.modifySClass(tempNorthButton, STICKY_BUTTON_CSS_OFF, true);
            }
        }
        if(tempCenterButton.isVisible())
        {
            if(getValue(MODEL_CENTER_STICKY_BUTTON_ON, Boolean.class))
            {
                UITools.modifySClass(tempCenterButton, STICKY_BUTTON_CSS_ON, true);
                UITools.modifySClass(tempCenterButton, STICKY_BUTTON_CSS_OFF, false);
            }
            else
            {
                UITools.modifySClass(tempCenterButton, STICKY_BUTTON_CSS_ON, false);
                UITools.modifySClass(tempCenterButton, STICKY_BUTTON_CSS_OFF, true);
            }
        }
        if(tempBottomButton.isVisible())
        {
            if(getValue(MODEL_BOTTOM_STICKY_BUTTON_ON, Boolean.class))
            {
                UITools.modifySClass(tempBottomButton, STICKY_BUTTON_CSS_ON, true);
                UITools.modifySClass(tempBottomButton, STICKY_BUTTON_CSS_OFF, false);
            }
            else
            {
                UITools.modifySClass(tempBottomButton, STICKY_BUTTON_CSS_ON, false);
                UITools.modifySClass(tempBottomButton, STICKY_BUTTON_CSS_OFF, true);
            }
        }
    }


    @ViewEvent(componentID = NORTH_STICKY_BUTTON, eventName = Events.ON_CLICK)
    public void onNorthStickyButtonClick()
    {
        setValue(MODEL_CENTER_STICKY_BUTTON_ON, !getValue(MODEL_CENTER_STICKY_BUTTON_ON, Boolean.class));
        updateStickyButtonState();
    }


    @ViewEvent(componentID = OUTER_NORTH_STICKY_BUTTON, eventName = Events.ON_CLICK)
    public void onOuterNorthStickyButtonClick()
    {
        setValue(MODEL_NORTH_STICKY_BUTTON_ON, !getValue(MODEL_NORTH_STICKY_BUTTON_ON, Boolean.class));
        updateStickyButtonState();
    }


    @ViewEvent(componentID = CENTER_STICKY_BUTTON, eventName = Events.ON_CLICK)
    public void onCenterStickyButtonClick()
    {
        if(!getValue(MODEL_CENTER_PANEL_COLLAPSED, Boolean.class) && getValue(MODEL_BOTTOM_PANEL_COLLAPSED, Boolean.class))
        {
            setValue(MODEL_CENTER_STICKY_BUTTON_ON, !getValue(MODEL_CENTER_STICKY_BUTTON_ON, Boolean.class));
        }
        else
        {
            setValue(MODEL_BOTTOM_STICKY_BUTTON_ON, !getValue(MODEL_BOTTOM_STICKY_BUTTON_ON, Boolean.class));
        }
        updateStickyButtonState();
    }


    @ViewEvent(componentID = SOUTH_STICKY_BUTTON, eventName = Events.ON_CLICK)
    public void onBottomStickyButtonClick()
    {
        setValue(MODEL_BOTTOM_STICKY_BUTTON_ON, !getValue(MODEL_BOTTOM_STICKY_BUTTON_ON, Boolean.class));
        updateStickyButtonState();
    }


    private void addPanelStateModelObservers()
    {
        this.getModel().addObserver(MODEL_TOP_PANEL_COLLAPSED, this::updateStickyButtonState);
        this.getModel().addObserver(MODEL_CENTER_PANEL_COLLAPSED, this::updateStickyButtonState);
        this.getModel().addObserver(MODEL_BOTTOM_PANEL_COLLAPSED, this::updateStickyButtonState);
    }


    private void appendTemporaryContentHolder(final Component comp)
    {
        final Component component = comp.getFellowIfAny(TEMPORARY_CONTENT_HOLDER_ATTR);
        if(component == null)
        {
            final Div temporaryContentHolder = new Div();
            temporaryContentHolder.setVisible(false);
            temporaryContentHolder.setId(TEMPORARY_CONTENT_HOLDER_ATTR);
            comp.appendChild(temporaryContentHolder);
        }
    }


    private Component getTemporaryContentHolder()
    {
        Component component = null;
        final Widgetslot widgetslot = getWidgetInstanceManager().getWidgetslot();
        if(widgetslot != null)
        {
            component = widgetslot.getFellowIfAny(TEMPORARY_CONTENT_HOLDER_ATTR);
        }
        return component;
    }


    public Borderlayout getBorderlayout()
    {
        return borderlayout;
    }


    public Borderlayout getOuterBorderlayout()
    {
        return outerBorderlayout;
    }


    public North getOuterNorth()
    {
        return outerNorth;
    }


    public North getNorth()
    {
        return north;
    }


    public South getSouth()
    {
        return south;
    }


    public Center getCenter()
    {
        return center;
    }


    public Div getTopContainer()
    {
        return topContainer;
    }


    public Div getBottomContainer()
    {
        return bottomContainer;
    }


    public Div getOuterTopContainer()
    {
        return outerTopContainer;
    }


    public Button getCenterButton()
    {
        return centerButton;
    }


    public Button getOuterNorthButton()
    {
        return outerNorthButton;
    }


    public Button getNorthButton()
    {
        return northButton;
    }


    public Button getSouthButton()
    {
        return southButton;
    }


    public Div getNorthCaptionContainer()
    {
        return northCaptionContainer;
    }


    public Div getCenterCaptionContainer()
    {
        return centerCaptionContainer;
    }


    public Div getSouthCaptionContainer()
    {
        return southCaptionContainer;
    }


    public Div getOuterNorthCaptionContainer()
    {
        return outerNorthCaptionContainer;
    }


    public Div getCenterCaptionContainerInner()
    {
        return centerCaptionContainerInner;
    }


    public Div getNorthCaptionContainerInner()
    {
        return northCaptionContainerInner;
    }


    public DefaultWidgetCaptionWrapper getTopCaptionWrapper()
    {
        return topCaptionWrapper;
    }


    public DefaultWidgetCaptionWrapper getCenterCaptionWrapper()
    {
        return centerCaptionWrapper;
    }


    public DefaultWidgetCaptionWrapper getBottomCaptionWrapper()
    {
        return bottomCaptionWrapper;
    }


    public Button getOuterNorthStickyButton()
    {
        return outerNorthStickyButton;
    }


    public Button getSouthStickyButton()
    {
        return southStickyButton;
    }
}
