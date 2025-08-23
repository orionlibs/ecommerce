/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicktogglelocale.controller;

import com.hybris.backoffice.widgets.quicktogglelocale.event.CurrentlyActiveLocalesChangedEvent;
import com.hybris.backoffice.widgets.quicktogglelocale.renderer.DefaultLocaleListitemRenderer;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.i18n.exception.AvailableLocaleException;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

public class QuickToggleLocaleController extends DefaultWidgetController
{
    protected static final String LOCALE_LISTBOX = "localesList";
    protected static final String UI_LOCALE_LISTBOX = "uiLocalesList";
    private static final Logger LOG = LoggerFactory.getLogger(QuickToggleLocaleController.class);
    private static final long serialVersionUID = 4308822839190406478L;
    public static final String ON_REDIRECT_LATER = "onRedirectLater";
    private static final String NOTIFICATION_EVENT_TYPE_SESSION_LANGUAGE_NOT_INDEXED = "sessionLanguageNotIndexed";
    @Wire
    private Listbox localesList;
    @Wire
    private Listbox uiLocalesList;
    private transient CockpitEventQueue cockpitEventQueue;
    private transient CockpitUserService cockpitUserService;
    private transient CockpitLocaleService cockpitLocaleService;
    private transient IndexedLanguagesResolver indexedLanguagesResolver;
    private transient NotificationService notificationService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        refreshModel();
        refreshUILocaleModel();
        getLocalesList().setItemRenderer(createRenderer());
        final Locale currentLocale = getCockpitLocaleService().getCurrentLocale();
        getUiLocalesList().setItemRenderer(createUILocaleRenderer(currentLocale));
        final String isoCode = currentLocale.toString();
        triggerLanguageNotIndexedNotification(isoCode, true, comp);
        displayCurrentLocale();
    }


    protected void triggerLanguageNotIndexedNotification(final String languageCode,
                    final Boolean isPostponeMode,
                    final Component comp)
    {
        final Map<String, IndexedLanguagesResolver> indexedLanguagesResolverMap =
                        BackofficeSpringUtil.getAllBeans(IndexedLanguagesResolver.class);
        if(null != indexedLanguagesResolverMap && !indexedLanguagesResolverMap.isEmpty())
        {
            indexedLanguagesResolverMap.values().stream().forEach(languagesResolver -> {
                if(!languagesResolver.isIndexed(languageCode))
                {
                    if(Boolean.TRUE.equals(isPostponeMode))
                    {
                        UITools.postponeExecution(comp, this::sendSessionLanguageNotIndexedNotification);
                    }
                    else
                    {
                        sendSessionLanguageNotIndexedNotification();
                    }
                }
            });
        }
    }


    protected void sendSessionLanguageNotIndexedNotification()
    {
        getNotificationService().notifyUser(getWidgetInstanceManager(), NOTIFICATION_EVENT_TYPE_SESSION_LANGUAGE_NOT_INDEXED,
                        NotificationEvent.Level.WARNING);
    }


    /**
     * Show the currently active locale in the language of the locale.
     *
     * @deprecated since 1905, the method is not used anymore
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected void displayCurrentLocale()
    {
        //NOOP
    }


    @ViewEvent(componentID = LOCALE_LISTBOX, eventName = Events.ON_SELECT)
    public void onSelectionChanged(final SelectEvent<Listitem, Locale> event)
    {
        final Listitem selectionChangedItem = event.getReference();
        final Locale cockpitLocale = selectionChangedItem.getValue();
        try
        {
            getCockpitLocaleService().toggleDataLocale(cockpitLocale, cockpitUserService.getCurrentUser());
            cockpitEventQueue.publishEvent(new CurrentlyActiveLocalesChangedEvent(cockpitLocale));
            final String isoCode = getCockpitLocaleService().getCurrentLocale().toString();
            triggerLanguageNotIndexedNotification(isoCode, false, null);
        }
        catch(final AvailableLocaleException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while toggle locale ", e);
            }
            Messagebox.show(getLabel("saveError"), "Error", Messagebox.OK, Messagebox.ERROR);
            refreshModel();
        }
    }


    @ViewEvent(componentID = UI_LOCALE_LISTBOX, eventName = Events.ON_SELECT)
    public void onUILocaleSelectionChanged(final SelectEvent<Listitem, Locale> event)
    {
        final Listitem selectionChangedItem = event.getReference();
        final Locale cockpitLocale = selectionChangedItem.getValue();
        cockpitLocaleService.setCurrentLocale(cockpitLocale);
        doRefreshTheUI();
    }


    @InextensibleMethod
    void doRefreshTheUI()
    {
        Clients.showBusy(null);
        Executions.sendRedirect("/");
    }


    public boolean isLocaleEnabled(final Locale locale)
    {
        return cockpitLocaleService.isDataLocaleEnabled(locale, cockpitUserService.getCurrentUser());
    }


    protected void refreshModel()
    {
        ListModelList<Locale> model;
        try
        {
            final List<Locale> availableLocales = sortByCurrentLocale(
                            cockpitLocaleService.getAvailableDataLocales(cockpitUserService.getCurrentUser()));
            model = new ListModelList<>(availableLocales);
        }
        catch(final AvailableLocaleException e)
        {
            if(LOG.isWarnEnabled())
            {
                LOG.warn("No locales available.", e);
            }
            model = new ListModelList<>(Collections.emptyList());
        }
        model.setMultiple(true);
        localesList.setModel(model);
    }


    protected void refreshUILocaleModel()
    {
        ListModelList<Locale> model;
        try
        {
            model = new ListModelList<>(sortByCurrentLocale(cockpitLocaleService.getAllUILocales()));
        }
        catch(final AvailableLocaleException e)
        {
            if(LOG.isWarnEnabled())
            {
                LOG.warn("No UI locales available.", e);
            }
            model = new ListModelList<>(Collections.emptyList());
        }
        model.setMultiple(false);
        uiLocalesList.setModel(model);
    }


    protected List<Locale> sortByCurrentLocale(final Collection<Locale> locales)
    {
        final Locale currentLocale = cockpitLocaleService.getCurrentLocale();
        final LinkedList<Locale> result = new LinkedList<>(locales);
        result.sort((o1, o2) -> StringUtils.compare(getLocaleLabel(currentLocale, o1), getLocaleLabel(currentLocale, o2)));
        return result;
    }


    /**
     * Creates the list item renderer. Override this method to create your own tree item renderer.
     *
     * @return new list item renderer
     */
    protected ListitemRenderer<Locale> createRenderer()
    {
        return new DefaultLocaleListitemRenderer(this, cockpitLocaleService.getCurrentLocale());
    }


    /**
     * Creates the list item renderer. Override this method to create your own tree item renderer.
     *
     * @return new list item renderer for UI locales
     */
    protected ListitemRenderer<Locale> createUILocaleRenderer(final Locale displayLocale)
    {
        return (Listitem listItem, Locale locale, int index) -> {
            listItem.setValue(locale);
            listItem.setLabel(getLocaleLabel(displayLocale, locale));
            listItem.setSelected(Objects.equals(cockpitLocaleService.getCurrentLocale(), locale));
        };
    }


    protected String getLocaleLabel(Locale displayLocale, Locale locale)
    {
        return StringUtils.defaultIfBlank(getLabel(locale.toString()), locale.getDisplayName(displayLocale));
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    public Listbox getLocalesList()
    {
        return localesList;
    }


    public Listbox getUiLocalesList()
    {
        return uiLocalesList;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    protected IndexedLanguagesResolver getIndexedLanguagesResolver()
    {
        return indexedLanguagesResolver;
    }
}
