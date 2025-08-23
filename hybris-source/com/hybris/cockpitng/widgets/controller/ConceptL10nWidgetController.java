/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Locale;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * L10 functionality demonstrating widget.
 */
public class ConceptL10nWidgetController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(ConceptL10nWidgetController.class);
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_LOCALE = "en";
    protected static final String LOCALE_LISTBOX_NAME = "localeListbox";
    protected transient Label controllersLabel;
    protected transient Listbox localeListbox;
    protected transient CockpitLocaleService cockpitLocaleService;
    private String currentLocale;


    public ConceptL10nWidgetController()
    {
        controllersLabel = null;
        localeListbox = null;
        cockpitLocaleService = null;
    }


    /**
     * Determines the widget's locale.
     */
    private void setLocale()
    {
        currentLocale = cockpitLocaleService.getCurrentLocale() == null ? DEFAULT_LOCALE : cockpitLocaleService.getCurrentLocale()
                        .getLanguage();
        if(!StringUtils.isBlank(currentLocale))
        {
            final Locale preferredLocale;
            try
            {
                preferredLocale = LocaleUtils.toLocale(currentLocale);
                cockpitLocaleService.setCurrentLocale(preferredLocale);
            }
            catch(final IllegalArgumentException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.warn(String.format("Wrong locale parameter %s, not activating locale for session", currentLocale), e);
                }
                else
                {
                    LOG.warn("Wrong locale parameter {}, not activating locale for session", currentLocale);
                }
            }
        }
    }


    @Override
    public void initialize(final Component comp)
    {
        setLocale();
        controllersLabel.setValue(getLabel("label.value.content"));
        for(final Listitem localeItem : localeListbox.getItems())
        {
            if(localeItem.getValue().toString().equals(currentLocale))
            {
                localeListbox.setSelectedItem(localeItem);
            }
        }
    }


    /**
     * Locale switcher to switch UI language at runtime.
     */
    @ViewEvent(eventName = Events.ON_SELECT, componentID = LOCALE_LISTBOX_NAME)
    public void localeSelected()
    {
        if(localeListbox.getSelectedItem() != null)
        {
            final String localeValue = localeListbox.getSelectedItem().getValue().toString();
            LOG.info(localeValue);
            cockpitLocaleService.setCurrentLocale(new Locale(localeValue));
            getWidgetInstanceManager().getWidgetslot().updateView();
        }
    }
}
