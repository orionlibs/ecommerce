/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.modules;

import java.util.Collection;

/**
 * Information about single module
 */
public interface ModuleInfo
{
    /**
     * Returns package name with module's contents
     *
     * @return name of module's contents package
     */
    String getWidgetsPackage();


    /**
     * @return module identity
     */
    String getId();


    /**
     * @return url of module's source
     */
    String getLocationUrl();


    /**
     * @return url of module's icon
     */
    String getIconUrl();


    /**
     * @return collection of parent module's urls
     */
    Collection<String> getParentModulesLocationUrls();


    /**
     * Gets an extension of application mesh-up defined by the module
     *
     * @return application mesh-up extension
     */
    String getWidgetsExtension();


    /**
     * Gets URI to an extension of application context defined by module
     *
     * @return application context extension uri
     */
    String getApplicationContextUri();
}
