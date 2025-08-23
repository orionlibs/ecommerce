/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.spring.core;

import com.hybris.cockpitng.core.impl.ResourceCockpitComponentLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleTestWidgetDefinitionLoader extends ResourceCockpitComponentLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(ModuleTestWidgetDefinitionLoader.class);
    private String basePath = "backoffice/resources/widgets";


    @Override
    protected Set<String> getWidgetInfoFiles()
    {
        final Set<String> urls = new HashSet<>();
        final File root = new File(getBasePath());
        if(!root.exists())
        {
            return Collections.emptySet();
        }
        final File[] rootFiles = root.listFiles();
        if(rootFiles == null)
        {
            LOG.error("Could not list widget info root files.");
        }
        else
        {
            for(final File widgetDir : root.listFiles())
            {
                final File[] widgetFiles = widgetDir.listFiles();
                if(widgetFiles == null)
                {
                    LOG.error("Could not list widget info files.");
                }
                else
                {
                    for(final File file : widgetFiles)
                    {
                        if(file != null)
                        {
                            final String url = file.toString().replaceAll("\\\\", "/");
                            if(url.endsWith(DEFINITION_XML))
                            {
                                urls.add(url);
                            }
                        }
                    }
                }
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(urls.toString());
        }
        return urls;
    }


    @Override
    protected InputStream getDefintionAsStream(final String widgetFile)
    {
        try
        {
            return new FileInputStream(new File(widgetFile));
        }
        catch(final FileNotFoundException e)
        {
            LOG.warn("", e);
            return null;
        }
    }


    public String getBasePath()
    {
        return basePath;
    }


    public void setBasePath(final String basePath)
    {
        this.basePath = basePath;
    }
}
