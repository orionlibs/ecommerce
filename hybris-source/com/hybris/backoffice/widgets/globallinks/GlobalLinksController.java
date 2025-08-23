/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.globallinks;

import com.hybris.cockpitng.config.links.jaxb.Link;
import com.hybris.cockpitng.config.links.jaxb.Links;
import com.hybris.cockpitng.config.links.jaxb.Target;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;

public class GlobalLinksController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(GlobalLinksController.class);
    protected static final String SETTING_LINKS_COMPONENT_NAME = "linksComponentName";
    protected static final String SETTING_HIDE_IF_EMPTY = "hideIfEmpty";
    public static final String SCLASS_GLOBAL_LINKS_CONTAINER_LINK = "yw-global-links-container-link";
    public static final String SCLASS_GLOBAL_LINKS_CONTAINER_LINK_ICON = "yw-global-links-container-link-icon";
    public static final String SCLASS_GLOBAL_LINKS_CONTAINER_LINK_LABEL = "yw-global-links-container-link-label";
    @Wire
    private Toolbarbutton toolbarbutton;
    @Wire
    private Div globalLinksContainer;
    @Wire
    private Popup globalLinksPopup;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final Links links = loadLinks();
        if(links != null && CollectionUtils.isNotEmpty(links.getLink()))
        {
            links.getLink().stream().map(this::createLink).forEach(getGlobalLinksContainer()::appendChild);
        }
        else if(getWidgetSettings().getBoolean(SETTING_HIDE_IF_EMPTY))
        {
            getWidgetslot().setVisible(false);
        }
    }


    protected Div createLink(final Link link)
    {
        final Div div = new Div();
        if(link != null)
        {
            div.setSclass(SCLASS_GLOBAL_LINKS_CONTAINER_LINK);
            final Image icon = new Image();
            icon.setSrc(link.getIcon());
            icon.setSclass(SCLASS_GLOBAL_LINKS_CONTAINER_LINK_ICON);
            final Label label = new Label(getLinkLabel(link.getLabel()));
            label.setSclass(SCLASS_GLOBAL_LINKS_CONTAINER_LINK_LABEL);
            div.appendChild(icon);
            div.appendChild(label);
            div.addEventListener(Events.ON_CLICK, event -> onOpenLink(link));
        }
        return div;
    }


    protected String getLinkLabel(final String labelKey)
    {
        String label = getLabel(labelKey);
        if(StringUtils.isBlank(label))
        {
            label = Labels.getLabel(labelKey);
        }
        return StringUtils.isNotBlank(label) ? label : labelKey;
    }


    protected void onOpenLink(final Link link)
    {
        getGlobalLinksPopup().close();
        if(link != null && StringUtils.isNotBlank(link.getUrl()))
        {
            final String target = link.getTarget() != null ? link.getTarget().value() : Target.BLANK.value();
            sendRedirect(link.getUrl(), target);
        }
    }


    protected void sendRedirect(final String url, final String target)
    {
        Executions.getCurrent().sendRedirect(url, target);
    }


    protected Links loadLinks()
    {
        final String componentName = getWidgetSettings().getString(SETTING_LINKS_COMPONENT_NAME);
        try
        {
            return getWidgetInstanceManager().loadConfiguration(new DefaultConfigContext(componentName), Links.class);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn(String.format("Cannot load global link configuration for component %s ", componentName), e);
        }
        return null;
    }


    public Div getGlobalLinksContainer()
    {
        return globalLinksContainer;
    }


    public Toolbarbutton getToolbarbutton()
    {
        return toolbarbutton;
    }


    public Popup getGlobalLinksPopup()
    {
        return globalLinksPopup;
    }
}
