/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js.impl;

import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;

/**
 * Path to a single resource with information about path type.
 */
public class ResourcePath
{
    public static final String JS_DEPENDENCY_ROOT = "/cng/js";
    public static final byte CLASSPATH_RESOURCE = 0;
    public static final byte JAR_RESOURCE = 1;
    public static final byte REMOTE_RESOURCE = 2;
    private final Byte type;
    private final String path;
    private final String module;


    public ResourcePath(final String path, final String module)
    {
        this.type = null;
        this.path = path;
        this.module = module;
    }


    public ResourcePath(final byte type, final String path, final String module)
    {
        this.type = type;
        this.path = path;
        this.module = module;
    }


    public byte getType()
    {
        return type != null ? type : JAR_RESOURCE;
    }


    public boolean isTypeSet()
    {
        return type != null;
    }


    public String getPath()
    {
        return path;
    }


    public String getModule()
    {
        return module;
    }


    @Override
    public String toString()
    {
        Assert.notNull(path, "Dependency url may not be null");
        final String strippedPath = path.replaceFirst(WidgetLibConstants.RESOURCES_SUBFOLDER, "");
        switch(type)
        {
            case JAR_RESOURCE:
                return FilenameUtils.normalize(WidgetLibConstants.JAR_ROOT_RESOURCE_PATH_PREFIX + "/" + module + JS_DEPENDENCY_ROOT + "/" + strippedPath, true);
            case CLASSPATH_RESOURCE:
                return FilenameUtils.normalize(WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX + "/cng/" + strippedPath, true);
            case REMOTE_RESOURCE:
                return strippedPath;
        }
        return strippedPath;
    }
}
