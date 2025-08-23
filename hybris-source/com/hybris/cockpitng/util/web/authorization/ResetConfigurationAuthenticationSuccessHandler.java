/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.web.authorization;

import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ResetConfigurationStrategy;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;

/**
 * Strategy that resets cockpit-config.xml and widgets.xml.
 */
public class ResetConfigurationAuthenticationSuccessHandler extends BackofficeAuthenticationSuccessHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ResetConfigurationAuthenticationSuccessHandler.class);
    private static final String RESET_TRIGGER_LOGIN = "login";
    private static final String RESET_TRIGGER_START = "start";
    private static final String RESET_SCOPE_COCKPIT_CONFIG = "cockpitConfig";
    private static final String RESET_SCOPE_WIDGETS = "widgets";
    private CockpitConfigurationService cockpitConfigurationService;
    private WidgetPersistenceService widgetPersistenceService;
    private ResetConfigurationStrategy resetStrategy;


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    @Required
    public void setResetStrategy(final ResetConfigurationStrategy resetStrategy)
    {
        this.resetStrategy = resetStrategy;
    }


    protected void resetIfConfigured(final String trigger)
    {
        if(resetStrategy.isTriggerConfigured(trigger))
        {
            for(final String scope : resetStrategy.getResetScopes(trigger))
            {
                switch(scope)
                {
                    case RESET_SCOPE_WIDGETS:
                        LOG.warn("Automatic widgets.xml reset is triggered by: {}", trigger);
                        resetWidgetsXml();
                        break;
                    case RESET_SCOPE_COCKPIT_CONFIG:
                        LOG.warn("Automatic cockpit-config.xml reset is triggered by: {}", trigger);
                        resetCockpitConfigXml();
                        break;
                    default:
                        LOG.error("Unknown reset scope configured for trigger {}: {}", trigger, scope);
                }
            }
        }
    }


    @PostConstruct
    public void resetIfConfigured()
    {
        resetIfConfigured(RESET_TRIGGER_START);
    }


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws ServletException, IOException
    {
        resetIfConfigured(RESET_TRIGGER_LOGIN);
        super.onAuthenticationSuccess(request, response, authentication);
    }


    public void resetCockpitConfigXml()
    {
        cockpitConfigurationService.resetToDefaults();
    }


    public void resetWidgetsXml()
    {
        widgetPersistenceService.resetToDefaults();
    }
}
