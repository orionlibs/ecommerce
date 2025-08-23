/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js;

import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.web.js.impl.DependencyDefinition;
import com.hybris.cockpitng.web.js.impl.ResolvedDependency;
import java.util.Map;
import org.zkoss.zk.ui.Component;

/**
 * Defines a bean that is able to somehow inject library dependencies into webpage.
 */
public interface DependenciesManager
{
    /**
     * Injects dependencies into page defined by provided component
     *
     * @param dependencies
     *           dependencies to be injected
     * @param comp
     *           main page view
     */
    void manageScriptDependencies(final Map<DependencyDefinition, ResolvedDependency> dependencies, final Component comp);


    /**
     * Looks for all needed dependencies
     *
     * @return dependencies found
     */
    Map<DependencyDefinition, ResolvedDependency> getScriptDependencies();


    /**
     * Looks for all needed dependencies for a single widget
     *
     * @return dependencies found
     */
    Map<DependencyDefinition, ResolvedDependency> getScriptDependencies(WidgetJarLibInfo widget);
}
