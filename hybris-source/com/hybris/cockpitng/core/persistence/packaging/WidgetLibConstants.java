/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

/**
 * Constants for handling widget data.
 */
// Could be declared as an interface?
public final class WidgetLibConstants
{
    /**
     * Name of a root folder for resources in module library
     */
    public static final String ROOT_NAME_COCKPIT_RESOURCES = "cockpitng";
    /**
     * @deprecated since 6.7, use {@link #ROOT_NAME_COCKPIT_RESOURCES} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String RESOURCES_SUBFOLDER = "/" + ROOT_NAME_COCKPIT_RESOURCES;
    /**
     * Web path prefix for resources with path read as relative to widget root
     */
    public static final String JAR_RESOURCES_PATH_PREFIX = "widgetJarResource";
    /**
     * Web path prefix for resources with path read as absolute path in widget loader's class loader
     */
    public static final String CLASSPATH_RESOURCES_PATH_PREFIX = "widgetClasspathResource";
    /**
     * Web path prefix for all resource files related to widget jar, yet relative to jar's root (not to widget's root)
     */
    public static final String JAR_ROOT_RESOURCE_PATH_PREFIX = "jarResource";
    public static final String DEPLOYED_SUBFOLDER_NAME = "deployed";
    public static final String WIDGET_JAR_LIB_DIR = "widgetlib";
    public static final String FILE_LIBRARY_INFO = "library.info";
    public static final String CONSTANT_USER_HOME = "${user.home}";
    public static final String CONSTANT_TEMP_DIR = "${java.io.tmpdir}";


    private WidgetLibConstants()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
