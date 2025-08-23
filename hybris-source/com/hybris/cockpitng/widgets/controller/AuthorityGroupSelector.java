/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.logo.LogoService;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.WidgetUtils;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

/**
 * Widget for change the active authority group for the current user.
 */
public class AuthorityGroupSelector extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityGroupSelector.class);
    public static final String SOCKET_OUT_AUTHORITYGROUP = "authoritygroup";
    private static final String SETTING_SHOW_FALLBACK_BRACKETS = "showFallbackBrackets";
    private static final String SETTING_SEND_REDIRECT = "sendRedirect";
    private static final String SELECTOR_LISTITEM_CSS = "yw-selector-listitem";
    private static final String SELECTOR_LISTCELL_CSS = "yw-selector-listcell";
    private static final String SELECTOR_IMAGE_CSS = "yw-selector-image";
    private static final String EVENT_NAME = "onAuthorityGroupChangeEvent";
    @WireVariable
    private transient CockpitUserService cockpitUserService;
    @WireVariable
    private transient AuthorityGroupService authorityGroupService;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient WidgetUtils widgetUtils;
    @WireVariable
    private transient LogoService logoService;
    @Wire
    private Listbox authorityGroupSelectorListbox;
    @Wire
    private Button selectorButton;
    @Wire
    private Image logoImage;
    private boolean displayBrackets = true;


    /**
     * Displaying all available authority groups for the current user in a list box. The active authority group will be
     * checked initially. By clicking the proceed button the checked authority group will be send out and the page will be
     * reloaded.
     */
    @Override
    public void initialize(final Component comp)
    {
        final String currentUser = cockpitUserService.getCurrentUser();
        if(currentUser == null)
        {
            LOG.warn("Cannot get authority groups, current user is null.");
            return;
        }
        if(authorityGroupService == null)
        {
            LOG.warn("Cannot get authority groups, authorityGroupService is null.");
            return;
        }
        final AuthorityGroup activeAuthorityGroup = authorityGroupService.getActiveAuthorityGroupForUser(currentUser);
        final List<AuthorityGroup> allAuthorityGroups = authorityGroupService.getAllAuthorityGroupsForUser(currentUser);
        if(CollectionUtils.isNotEmpty(allAuthorityGroups))
        {
            displayBrackets = getWidgetSettings().getBoolean(SETTING_SHOW_FALLBACK_BRACKETS);
            for(final AuthorityGroup authorityGroup : allAuthorityGroups)
            {
                final Listitem listItem = new Listitem();
                listItem.setSclass(SELECTOR_LISTITEM_CSS);
                listItem.setValue(authorityGroup.getCode());
                final Listcell listCell1 = new Listcell();
                listCell1.setValue(authorityGroup.getCode());
                listCell1.setLabel(getDisplayLabel(authorityGroup));
                listCell1.setSclass(SELECTOR_LISTCELL_CSS);
                final Listcell listCell2 = new Listcell();
                listCell2.setSclass(SELECTOR_LISTCELL_CSS);
                if(StringUtils.isNotBlank(authorityGroup.getDescription()))
                {
                    final Image image = new Image();
                    image.setSclass(SELECTOR_IMAGE_CSS);
                    image.setTooltiptext(authorityGroup.getDescription());
                    image.setParent(listCell2);
                }
                listCell1.setParent(listItem);
                listCell2.setParent(listItem);
                if(activeAuthorityGroup != null && activeAuthorityGroup.getCode() != null
                                && activeAuthorityGroup.getCode().equals(listItem.getValue()))
                {
                    listItem.setSelected(true);
                }
                authorityGroupSelectorListbox.appendChild(listItem);
            }
            preselectAuthorityGroupFallback();
        }
        selectorButton.addEventListener(Events.ON_CLICK, event -> {
            final Listitem selectedItem = authorityGroupSelectorListbox.getSelectedItem();
            if(selectedItem != null)
            {
                selectAuthorityGroup(String.valueOf(selectedItem.<String>getValue()));
            }
        });
        renderLogo();
    }


    private void renderLogo()
    {
        final String url = getLogoService().getLoginPageLogoUrl();
        if(StringUtils.isNotEmpty(url))
        {
            logoImage.setSrc(url);
        }
    }


    private void preselectAuthorityGroupFallback()
    {
        if(authorityGroupSelectorListbox.getSelectedCount() == 0)
        {
            authorityGroupSelectorListbox.setSelectedIndex(0);
        }
    }


    protected String getDisplayLabel(final AuthorityGroup authorityGroup)
    {
        Validate.notNull("Given group may not be null", authorityGroup);
        return StringUtils.isNotBlank(authorityGroup.getName()) ? authorityGroup.getName()
                        : (displayBrackets ? "[" + authorityGroup.getCode() + "]" : authorityGroup.getCode());
    }


    protected void selectAuthorityGroup(final String code)
    {
        if(StringUtils.isNotBlank(code))
        {
            final AuthorityGroup selectedAuthorityGroup = authorityGroupService.getAuthorityGroup(code);
            authorityGroupService.setActiveAuthorityGroupForUser(selectedAuthorityGroup);
            cockpitEventQueue.publishEvent(new DefaultCockpitEvent(EVENT_NAME, selectedAuthorityGroup, this));
            try
            {
                widgetUtils.dispatchGlobalEvents();
            }
            catch(final Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
            sendOutput(SOCKET_OUT_AUTHORITYGROUP, selectedAuthorityGroup);
            if(getWidgetSettings().getBoolean(SETTING_SEND_REDIRECT))
            {
                Executions.sendRedirect(null);
            }
        }
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    public Listbox getAuthorityGroupSelectorListbox()
    {
        return authorityGroupSelectorListbox;
    }


    public Button getSelectorButton()
    {
        return selectorButton;
    }


    public LogoService getLogoService()
    {
        return logoService;
    }


    public void setLogoService(final LogoService logoService)
    {
        this.logoService = logoService;
    }


    public Image getLogoImage()
    {
        return logoImage;
    }
}
