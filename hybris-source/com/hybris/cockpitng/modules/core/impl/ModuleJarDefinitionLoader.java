/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.core.impl;

import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.impl.JarLibComponentLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetClassLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Set;

/**
 * Component loader that loads {@link CockpitComponentInfo}s from a module jar in a subfolder of the widgetlib
 * directory.
 */
public class ModuleJarDefinitionLoader extends JarLibComponentLoader
{
    public static final String DEPLOYED_SUBFOLDER_NAME = WidgetLibConstants.DEPLOYED_SUBFOLDER_NAME;
    private ClassLoader moduleClassLoader;


    @Override
    protected List<WidgetJarLibInfo> getJarLibInfos()
    {
        return getWidgetLibUtils().loadAllWidgetJarLibInfos(new File(getJarDir()));
    }


    @Override
    public Set<CockpitComponentInfo> load(final ClassLoader classLoader)
    {
        final Set<CockpitComponentInfo> load = super.load(classLoader);
        for(final CockpitComponentInfo cockpitComponentInfo : load)
        {
            final ClassLoader classLdr = cockpitComponentInfo.getClassLoader();
            if(classLdr instanceof WidgetClassLoader)
            {
                cockpitComponentInfo.setExternalLocation(((WidgetClassLoader)classLdr).getWidgetLibInfo().getExternalLocation());
            }
        }
        return load;
    }


    @Override
    protected ClassLoader getClassLoader()
    {
        final PrivilegedAction<ClassLoader> action = () -> {
            if(ModuleJarDefinitionLoader.this.moduleClassLoader == null)
            {
                moduleClassLoader = new WidgetClassLoader(ModuleJarDefinitionLoader.super.getClassLoader(), getJarDir(),
                                getWidgetLibUtils().isResourceCacheEnabled());
            }
            return ModuleJarDefinitionLoader.this.moduleClassLoader;
        };
        return AccessController.doPrivileged(action);
    }


    @Override
    protected String getJarDir()
    {
        return getWidgetLibUtils().getWidgetJarLibDir() + File.separator + DEPLOYED_SUBFOLDER_NAME;
    }


    void clearModuleClassLoader()
    {
        this.moduleClassLoader = null;
    }
}
