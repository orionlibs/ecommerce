/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationPersistenceStrategy;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default cockpit configuration strategy using file system to store it.
 */
public class DefaultCockpitConfigurationPersistenceStrategy implements CockpitConfigurationPersistenceStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitConfigurationPersistenceStrategy.class);
    /**
     * Configuration key for default cockpit config resource.
     */
    public static final String COCKPITNG_CONFIG_DEFAULT = "cockpitng.config.default";
    private WidgetLibUtils widgetLibUtils;
    private CockpitProperties cockpitProperties;


    @Override
    public long getLastModification()
    {
        return getConfigFile().lastModified();
    }


    @Override
    public InputStream getConfigurationInputStream() throws IOException
    {
        return new FileInputStream(getConfigFile());
    }


    @Override
    public OutputStream getConfigurationOutputStream() throws IOException
    {
        return new FileOutputStream(getConfigFile());
    }


    private File getConfigFile()
    {
        final File dir = new File(getRootDir(), "config");
        try
        {
            dir.mkdirs();
        }
        catch(final SecurityException e)
        {
            LOG.error("Write access denied creating cockpit configuration directory.", e);
        }
        final File file = new File(dir, "cockpit-config.xml");
        try
        {
            final boolean fileCreated = file.createNewFile();
            if(!fileCreated)
            {
                return file;
            }
            InputStream defaultConfigAsStream = null;
            FileOutputStream outputStream = null;
            try
            {
                defaultConfigAsStream = getDefaultConfigurationInputStream();
                if(defaultConfigAsStream != null)
                {
                    outputStream = new FileOutputStream(file);
                    IOUtils.copy(getDefaultConfigurationInputStream(), outputStream);
                }
            }
            finally
            {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(defaultConfigAsStream);
            }
        }
        catch(final IOException e)
        {
            LOG.error("could not create cockpit configuration file", e);
        }
        catch(final SecurityException e)
        {
            LOG.error("Write access denied creating cockpit configuration file.", e);
        }
        return file;
    }


    @Override
    public InputStream getDefaultConfigurationInputStream()
    {
        final String defaultConfigPath = this.cockpitProperties.getProperty(COCKPITNG_CONFIG_DEFAULT);
        if(StringUtils.isNotBlank(defaultConfigPath))
        {
            return getClass().getResourceAsStream(defaultConfigPath);
        }
        return null;
    }


    protected File getRootDir()
    {
        return widgetLibUtils.getRootDir();
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public CockpitProperties getCockpitProperties()
    {
        return this.cockpitProperties;
    }
}
