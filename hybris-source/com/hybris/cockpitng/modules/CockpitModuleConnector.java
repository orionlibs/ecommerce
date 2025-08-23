/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import com.hybris.cockpitng.core.modules.DefaultModuleInfo;
import com.hybris.cockpitng.core.modules.ModuleInfo;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.modules.core.impl.CockpitModuleComponentDefinitionService;
import com.hybris.cockpitng.modules.persistence.impl.XmlModuleAwarePersistenceService;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import java.util.List;

/**
 * Connector for cockpit modules, used by {@link CockpitModuleComponentDefinitionService} and
 * {@link XmlModuleAwarePersistenceService} to access remote widgets and mashups.
 */
public interface CockpitModuleConnector
{
    /**
     * @return All module urls currently defined in the system.
     */
    List<String> getCockpitModuleUrls();


    /**
     * @return The module info registered for the give moduleUrl.
     */
    CockpitModuleInfo getModuleInfo(String moduleUrl);


    /**
     * @return The library handler for the given moduleUrl, which is responsible for loading the widget module library.
     */
    LibraryHandler getLibraryHandler(String moduleUrl);


    /**
     * @return The additional widget mashup provided by the the module with the given moduleUrl.
     */
    String getWidgetTreeContent(String moduleUrl);


    /**
     * @return Default cockpit modules. They are usually provided by the host of the cockpit ng webapp.
     *
     * @deprecated since 1808, use {@link CockpitApplicationContext#getLoadedModulesNames()}
     */
    @Deprecated(since = "1808", forRemoval = true)
    List<ModuleEntry> getDefaultModules();


    /**
     * @return URLs for custom cockpit modules. They can be added during runtime by a cockpit admininstrator.
     */
    List<String> getCustomModuleUrls();


    /**
     * @return True, if administrator is allowed to add custom modules during runtime.
     */
    boolean isCustomModulesPermitted();


    /**
     * See {@link #getDefaultModules()}.
     *
     * @deprecated since 1808, not available anymore
     */
    @Deprecated(since = "1808", forRemoval = true)
    void setDefaultModules(List<ModuleEntry> defaultModules);


    /**
     * See {@link #getCustomModuleUrls()}.
     */
    void setCustomModuleUrls(List<String> customModuleUrls);


    /**
     * Updates specified modules widget's mesh-up extension.
     *
     * @param moduleInfo
     *           module info to be updated
     * @param widgetsExtension
     *           widget's mesh-up extension
     * @see ModuleInfo#getWidgetsExtension()
     */
    default void updateWidgetsExtension(final ModuleInfo moduleInfo, final String widgetsExtension)
    {
        if(moduleInfo instanceof DefaultModuleInfo)
        {
            ((DefaultModuleInfo)moduleInfo).setWidgetsExtension(widgetsExtension);
        }
    }


    /**
     * Updates specified modules application context extension uri.
     *
     * @param moduleInfo
     *           module info to be updated
     * @param contextUri
     *           uri to application context extension
     * @see ModuleInfo#getApplicationContextUri()
     */
    default void updateApplicationContextUri(final ModuleInfo moduleInfo, final String contextUri)
    {
        if(moduleInfo instanceof DefaultModuleInfo)
        {
            ((DefaultModuleInfo)moduleInfo).setApplicationContextUri(contextUri);
        }
    }
}
