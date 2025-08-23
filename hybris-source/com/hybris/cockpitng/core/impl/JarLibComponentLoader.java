/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetClassLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Loads components from jars located in the widget library directory.
 */
public class JarLibComponentLoader extends ResourceCockpitComponentLoader
{
    private WidgetClassLoader jarClassLoader;


    @Override
    public Set<CockpitComponentInfo> load(final ClassLoader classLoader)
    {
        final Set<CockpitComponentInfo> infos = new HashSet<>();
        for(final WidgetJarLibInfo libInfo : getJarLibInfos())
        {
            final WidgetClassLoader widgetClassLoader = createWidgetClassLoader(classLoader, libInfo,
                            getWidgetLibUtils().isResourceCacheEnabled());
            final CockpitComponentInfo info = new CockpitComponentInfo(libInfo.getProperties(), libInfo.getPrefix(),
                            widgetClassLoader);
            infos.add(info);
        }
        return infos;
    }


    private WidgetClassLoader createWidgetClassLoader(final ClassLoader classLoader, final WidgetJarLibInfo libInfo,
                    final boolean isResourceCacheEnabled)
    {
        final PrivilegedAction<WidgetClassLoader> action = () -> new WidgetClassLoader(
                        classLoader == null ? JarLibComponentLoader.this.getClassLoader() : classLoader, libInfo, null,
                        isResourceCacheEnabled);
        return AccessController.doPrivileged(action);
    }


    @Override
    protected ClassLoader getClassLoader()
    {
        final PrivilegedAction<ClassLoader> action = () -> {
            if(JarLibComponentLoader.this.jarClassLoader == null)
            {
                JarLibComponentLoader.this.jarClassLoader = new WidgetClassLoader(JarLibComponentLoader.super.getClassLoader(),
                                getJarDir(), getWidgetLibUtils().isResourceCacheEnabled());
            }
            return JarLibComponentLoader.this.jarClassLoader;
        };
        return AccessController.doPrivileged(action);
    }


    protected List<WidgetJarLibInfo> getJarLibInfos()
    {
        return getWidgetLibUtils().loadAllWidgetJarLibInfos();
    }


    /**
     * @return
     */
    protected String getJarDir()
    {
        return String.valueOf(getWidgetLibUtils().getWidgetJarLibDir());
    }
}
