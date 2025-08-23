/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.selenium.remote;

import com.hybris.backoffice.config.impl.TestingBackofficeCockpitConfigurationService;
import com.hybris.backoffice.config.impl.TestingBackofficeWidgetPersistenceService;
import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.util.WidgetUtils;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.validation.services.ValidationService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class TestBackofficeConfigurationTestingService implements BackofficeConfigurationTestingService
{
    private static final Logger LOG = LoggerFactory.getLogger(TestBackofficeConfigurationTestingService.class);
    protected TestingBackofficeWidgetPersistenceService widgetPersistenceService;
    protected TestingBackofficeCockpitConfigurationService cockpitConfigurationService;
    protected WidgetUtils widgetUtils;
    protected SessionService sessionService;
    protected UserService userService;
    protected CockpitAdminService cockpitAdminService;
    protected ImportService importService;
    protected ValidationService validationService;


    @Override
    public void resetCockpitConfig()
    {
        sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                cockpitConfigurationService.resetToDefaults();
                return super.execute();
            }
        }, userService.getAdminUser());
    }


    @Override
    public void resetConfigurationCache()
    {
        sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                cockpitConfigurationService.clearCustomConfiguration();
                return super.execute();
            }
        }, userService.getAdminUser());
    }


    @Override
    public void applyTestConfigurationToConfigurationCache(final String configXML, final String moduleName) throws IOException
    {
        sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                cockpitConfigurationService.setCustomConfiguration(configXML, moduleName);
                return super.execute();
            }
        }, userService.getAdminUser());
    }


    @Override
    public void applyTestWidgetConfig(final String configXML) throws IOException
    {
        sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                widgetPersistenceService.setAdditionalWidgetConfig(configXML);
                return super.execute();
            }
        }, userService.getAdminUser());
    }


    @Override
    public void clearAdditionalWidgetConfig()
    {
        sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                widgetPersistenceService.clearAdditionalWidgetConfig();
                return super.execute();
            }
        }, userService.getAdminUser());
    }


    @Override
    public void resetWidgetConfig()
    {
        sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                widgetPersistenceService.resetToDefaults();
                return super.execute();
            }
        }, userService.getAdminUser());
    }


    @Override
    public void importImpex(final String content)
    {
        sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                final ImportResult importResult = importService.importData(getImportConfig(content));
                handleImportResult(importResult);
                return super.execute();
            }
        }, userService.getAdminUser());
    }


    protected ImportConfig getImportConfig(final String content)
    {
        final ImportConfig config = new ImportConfig();
        config.setScript(content);
        config.setDumpingEnabled(false);
        config.setRemoveOnSuccess(true);
        config.setEnableCodeExecution(Boolean.TRUE);
        config.setMaxThreads(1);
        return config;
    }


    protected void handleImportResult(final ImportResult importResult)
    {
        if(importResult.hasUnresolvedLines())
        {
            LOG.warn("There are unresolved lines in impex.");
        }
        if(importResult.getCronJob() != null)
        {
            switch(importResult.getCronJob().getResult())
            {
                case FAILURE:
                case ERROR:
                case UNKNOWN:
                {
                    LOG.error("Failed to import impex. For more details see impExImportCronJob object with code {}", importResult.getCronJob().getCode());
                }
            }
        }
    }


    @Override
    public void reloadValidationEngine()
    {
        validationService.reloadValidationEngine();
    }


    @Required
    public void setWidgetPersistenceService(final TestingBackofficeWidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    @Required
    public void setCockpitConfigurationService(final TestingBackofficeCockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    @Required
    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }


    @Required
    public void setImportService(final ImportService importService)
    {
        this.importService = importService;
    }


    @Required
    public void setValidationService(final ValidationService validationService)
    {
        this.validationService = validationService;
    }
}
