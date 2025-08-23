/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import com.hybris.cockpitng.core.persistence.packaging.SimpleHybrisWidgetResourceLoader;
import com.hybris.cockpitng.engine.SessionWidgetInstanceRegistry;
import com.hybris.cockpitng.modules.core.impl.CockpitModuleComponentDefinitionService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.WidgetUtils;

/**
 * Service for hot deployment of components
 */
public class HotDeploymentService
{
    /**
     * Force clearing, reloading and refreshing, clear css cache also.
     */
    public void hotDeploy()
    {
        final CockpitModuleComponentDefinitionService cockpitComponentDefinitionService = getCockpitComponentDefinitionService();
        cockpitComponentDefinitionService.setInitialized(false);
        cockpitComponentDefinitionService.clearModuleClassLoader();
        getSessionWidgetInstanceRegistry().clear();
        SimpleHybrisWidgetResourceLoader.clearCssCache();
        BackofficeSpringUtil.getApplicationContext().refresh();
        getWidgetUtils().refreshWidgetLibrary();
    }


    protected CockpitModuleComponentDefinitionService getCockpitComponentDefinitionService()
    {
        return BackofficeSpringUtil.getBean("cockpitComponentDefinitionService");
    }


    protected WidgetUtils getWidgetUtils()
    {
        return BackofficeSpringUtil.getBean("widgetUtils");
    }


    protected SessionWidgetInstanceRegistry getSessionWidgetInstanceRegistry()
    {
        return BackofficeSpringUtil.getBean("sessionWidgetInstanceRegistry");
    }
}
