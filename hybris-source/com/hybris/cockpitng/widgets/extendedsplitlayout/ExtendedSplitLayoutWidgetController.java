/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.extendedsplitlayout;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.config.extendedsplitlayout.jaxb.ExtendedSplitLayout;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;

public class ExtendedSplitLayoutWidgetController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(ExtendedSplitLayoutWidgetController.class);
    public static final String SOCKET_IN_CHOOSE_LAYOUT = "chooseLayout";
    public static final String SOCKET_IN_PARENT_LAYOUT = "parentLayout";
    public static final String SOCKET_OUT_SELECTED_LAYOUT = "selectedLayout";
    public static final String SOCKET_OUT_REFRESH_DISPLAY_DATA = "refreshDisplayData";
    public static final String LAYOUT_SINGLE = "single";
    public static final String LAYOUT_TWIXE = "twixe";
    public static final String LAYOUT_TWIXS = "twixs";
    public static final String LAYOUT_TRIPLE = "triple";
    public static final String LAYOUT_MIXLEFT = "mixleft";
    public static final String LAYOUT_MIXRIGHT = "mixright";
    public static final String CHOSEN_LAYOUT_DATA_ATTRIBUTE = "chosen-layout";
    public static final String MODEL_CHOSEN_LAYOUT = "chosenLayout";
    public static final String MAKE_RESIZABLE_DATA_ATTRIBUTE = "make-resizable";
    public static final String SPLIT_LAYOUT_ID_DATA_ATTRIBUTE = "split-layout-id";
    public static final String SETTING_CONFIG_CTX_NAME = "splitLayoutConfigCtx";
    public static final String DEFAULT_CONFIG_CTX_NAME = "extended-split-layout";
    public static final String SETTING_DEFAULT_LAYOUT = "defaultLayout";
    public static final String EVENT_ON_INIT_LAYOUT = "onInitLayout";
    @Wire
    private Div firstVessel;
    @Wire
    private Div secondVessel;
    @Wire
    private Div thirdVessel;
    @Wire
    private Div vesselContainer;


    @Override
    public void initialize(final Component comp)
    {
        Validate.notNull("Component may not be null", comp);
        super.initialize(comp);
        rewriteUuidToDataAttribute(comp);
        comp.setClientDataAttribute(MAKE_RESIZABLE_DATA_ATTRIBUTE, "true");
        rewriteUuidToDataAttribute(firstVessel);
        rewriteUuidToDataAttribute(secondVessel);
        rewriteUuidToDataAttribute(thirdVessel);
        selectInitialLayoutInEchoEvent();
        Clients.evalJavaScript("extendedSplitLayout.makeResizableAll()");
    }


    protected void selectInitialLayoutInEchoEvent()
    {
        getWidgetslot().addEventListener(EVENT_ON_INIT_LAYOUT, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                getWidgetslot().removeEventListener(EVENT_ON_INIT_LAYOUT, this);
                prepareLayout(findSelectedLayout());
            }
        });
        Events.echoEvent(EVENT_ON_INIT_LAYOUT, getWidgetslot(), EVENT_ON_INIT_LAYOUT);
    }


    protected String getDefaultLayout()
    {
        final ExtendedSplitLayout extendedSplitLayout = loadConfiguration();
        if(extendedSplitLayout != null && StringUtils.isNotBlank(extendedSplitLayout.getDefaultLayout()))
        {
            return extendedSplitLayout.getDefaultLayout();
        }
        final String defaultFromSetting = (String)getWidgetSettings().getOrDefault(SETTING_DEFAULT_LAYOUT, LAYOUT_TRIPLE);
        if(StringUtils.isNotBlank(defaultFromSetting))
        {
            return defaultFromSetting;
        }
        return LAYOUT_TRIPLE;
    }


    protected String findSelectedLayout()
    {
        final String chosenLayout = getValue(MODEL_CHOSEN_LAYOUT, String.class);
        return StringUtils.isNotBlank(chosenLayout) ? chosenLayout : getDefaultLayout();
    }


    protected void rewriteUuidToDataAttribute(final Component component)
    {
        component.setClientDataAttribute(SPLIT_LAYOUT_ID_DATA_ATTRIBUTE, component.getUuid());
    }


    @SocketEvent(socketId = SOCKET_IN_PARENT_LAYOUT)
    public void onParentLayoutChange(final String parentLayout)
    {
        if(StringUtils.isBlank(parentLayout))
        {
            return;
        }
        final ExtendedSplitLayout splitLayout = loadConfiguration();
        if(splitLayout == null)
        {
            return;
        }
        splitLayout.getLayoutMapping().stream()
                        .filter(mapping -> parentLayout.equals(mapping.getParentLayout()) && StringUtils.isNotBlank(mapping.getSelfLayout()))
                        .findAny().ifPresent(layoutMapping -> chooseLayout(layoutMapping.getSelfLayout()));
    }


    protected ExtendedSplitLayout loadConfiguration()
    {
        final String ctxNameFromSetting = getWidgetSettings().getString(SETTING_CONFIG_CTX_NAME);
        final String ctxName = StringUtils.isNotBlank(ctxNameFromSetting) ? ctxNameFromSetting : DEFAULT_CONFIG_CTX_NAME;
        try
        {
            return getWidgetInstanceManager().loadConfiguration(new DefaultConfigContext(ctxName), ExtendedSplitLayout.class);
        }
        catch(final CockpitConfigurationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn(String.format("Cannot find config for %s", ctxName), e);
            }
            else
            {
                LOG.warn("Cannot find config for {}", ctxName);
            }
            return null;
        }
    }


    @SocketEvent(socketId = SOCKET_IN_CHOOSE_LAYOUT)
    public void chooseLayout(final String layout)
    {
        Validate.notNull("Chosen layout may not be null", layout);
        vesselContainer.setSclass("yw-extendedsplitlayout-holder");
        prepareLayout(layout);
    }


    protected void prepareLayout(final String layout)
    {
        switch(layout)
        {
            case LAYOUT_SINGLE:
                prepareSingleLayout();
                break;
            case LAYOUT_TWIXS:
                prepareTwixsLayout();
                break;
            case LAYOUT_TWIXE:
                prepareTwixeLayout();
                break;
            case LAYOUT_MIXLEFT:
                prepareMixLeftLayout();
                break;
            case LAYOUT_MIXRIGHT:
                prepareMixRightLayout();
                break;
            case LAYOUT_TRIPLE:
            default:
                prepareDefaultLayout();
                break;
        }
        final String current = getValue(MODEL_CHOSEN_LAYOUT, String.class);
        if(!StringUtils.equals(current, layout))
        {
            setValue(MODEL_CHOSEN_LAYOUT, layout);
            sendOutput(SOCKET_OUT_SELECTED_LAYOUT, layout);
        }
        vesselContainer.getParent().setClientDataAttribute(CHOSEN_LAYOUT_DATA_ATTRIBUTE, layout);
        if(!layout.equals(LAYOUT_SINGLE))
        {
            sendOutput(SOCKET_OUT_REFRESH_DISPLAY_DATA, null);
        }
    }


    @InextensibleMethod
    private void prepareDefaultLayout()
    {
        Components.removeAllChildren(vesselContainer);
        secondVessel.setVisible(true);
        thirdVessel.setVisible(true);
        vesselContainer.appendChild(firstVessel);
        vesselContainer.appendChild(secondVessel);
        vesselContainer.appendChild(thirdVessel);
    }


    @InextensibleMethod
    private void prepareMixRightLayout()
    {
        Components.removeAllChildren(vesselContainer);
        UITools.modifySClass(vesselContainer, "yw-extendedsplitlayout-twinmix-right", true);
        secondVessel.setVisible(true);
        thirdVessel.setVisible(true);
        final Div vesselBoxRight = new Div();
        vesselBoxRight.setSclass("yw-extendedsplitlayout-box-wrapper");
        vesselBoxRight.appendChild(secondVessel);
        vesselBoxRight.appendChild(thirdVessel);
        vesselContainer.appendChild(firstVessel);
        vesselContainer.appendChild(vesselBoxRight);
    }


    @InextensibleMethod
    private void prepareMixLeftLayout()
    {
        Components.removeAllChildren(vesselContainer);
        UITools.modifySClass(vesselContainer, "yw-extendedsplitlayout-twinmix-left", true);
        secondVessel.setVisible(true);
        thirdVessel.setVisible(true);
        final Div vesselBox = new Div();
        vesselBox.setSclass("yw-extendedsplitlayout-box-wrapper");
        vesselBox.appendChild(firstVessel);
        vesselBox.appendChild(secondVessel);
        vesselContainer.appendChild(vesselBox);
        vesselContainer.appendChild(thirdVessel);
    }


    @InextensibleMethod
    private void prepareTwixeLayout()
    {
        Components.removeAllChildren(vesselContainer);
        UITools.modifySClass(vesselContainer, "yw-extendedsplitlayout-twin-east", true);
        secondVessel.setVisible(true);
        thirdVessel.setVisible(false);
        vesselContainer.appendChild(firstVessel);
        vesselContainer.appendChild(secondVessel);
        vesselContainer.appendChild(thirdVessel);
    }


    @InextensibleMethod
    private void prepareTwixsLayout()
    {
        Components.removeAllChildren(vesselContainer);
        UITools.modifySClass(vesselContainer, "yw-extendedsplitlayout-twin-south", true);
        secondVessel.setVisible(true);
        thirdVessel.setVisible(false);
        vesselContainer.appendChild(firstVessel);
        vesselContainer.appendChild(secondVessel);
        vesselContainer.appendChild(thirdVessel);
    }


    @InextensibleMethod
    private void prepareSingleLayout()
    {
        Components.removeAllChildren(vesselContainer);
        UITools.modifySClass(vesselContainer, "yw-extendedsplitlayout-single", true);
        secondVessel.setVisible(false);
        thirdVessel.setVisible(false);
        vesselContainer.appendChild(firstVessel);
    }


    public Div getFirstVessel()
    {
        return firstVessel;
    }


    public Div getSecondVessel()
    {
        return secondVessel;
    }


    public Div getThirdVessel()
    {
        return thirdVessel;
    }


    public Div getVesselContainer()
    {
        return vesselContainer;
    }
}
