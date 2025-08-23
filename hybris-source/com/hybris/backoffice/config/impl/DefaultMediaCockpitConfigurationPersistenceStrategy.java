/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationPersistenceStrategy;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.NoDataAvailableException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.tx.Transaction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaCockpitConfigurationPersistenceStrategy extends DefaultCockpitConfigurationPersistenceStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMediaCockpitConfigurationPersistenceStrategy.class);
    private static final String COCKPITNG_CONFIG = "cockpitng-config";
    private static final String PROPERTY_COCKPIT_CONFIG_REFRESH_DELAY = "cockpitng.configuration.refresh.delay";
    private static final String PROPERTY_COCKPIT_CONFIG_REFRESH_MAX_NUMBER_OF_REPETITIONS = "cockpitng.configuration.refresh.max.number.of.repetitions";
    private static final long PROPERTY_DEFAULT_COCKPIT_CONFIG_REFRESH_DELAY = 100;
    private static final long PROPERTY_DEFAULT_COCKPIT_CONFIG_REFRESH_MAX_NUMBER_OF_REPETITIONS = 3;
    private String configurationMediaCode = COCKPITNG_CONFIG;
    private static final String MIME_TYPE = "text/xml";
    private MediaService mediaService;
    private SessionService sessionService;
    private UserService userService;
    private BackofficeConfigurationMediaHelper backofficeConfigurationMediaHelper;
    private ModelService modelService;


    @Override
    public InputStream getConfigurationInputStream()
    {
        InputStream inputStream = null;
        try
        {
            long delay = getCockpitProperties().getInteger(PROPERTY_COCKPIT_CONFIG_REFRESH_DELAY);
            delay = delay > 0 ? delay : PROPERTY_DEFAULT_COCKPIT_CONFIG_REFRESH_DELAY;
            long maxNumberOfRepetitions = getCockpitProperties().getInteger(PROPERTY_COCKPIT_CONFIG_REFRESH_MAX_NUMBER_OF_REPETITIONS);
            maxNumberOfRepetitions = maxNumberOfRepetitions > 0 ? maxNumberOfRepetitions
                            : PROPERTY_DEFAULT_COCKPIT_CONFIG_REFRESH_MAX_NUMBER_OF_REPETITIONS;
            int numberOfRepetitions = 0;
            do
            {
                final MediaModel media = getCockpitNGConfig();
                LOG.debug("Loaded cockpit config from location {} with size={}, modified time {}, thread={}",
                                media.getLocation(),
                                media.getSize(),
                                media.getModifiedtime(),
                                Thread.currentThread().getId());
                inputStream = getInputStreamForMedia(media);
                numberOfRepetitions++;
                if(inputStream == null)
                {
                    LOG.debug("Cannot load cockpit config from location {} with size={}, modified time {}, thread={}, retry={}",
                                    media.getLocation(),
                                    media.getSize(),
                                    media.getModifiedtime(),
                                    Thread.currentThread().getId(),
                                    numberOfRepetitions);
                }
                Thread.sleep(delay);
            }
            while(inputStream == null && numberOfRepetitions < maxNumberOfRepetitions);
        }
        catch(final CockpitConfigurationException ex)
        {
            LOG.error("Error while enquiring cockpit-config media", ex);
        }
        catch(final InterruptedException ex)
        {
            LOG.error("Interrupted while loading cockpit config", ex);
            Thread.currentThread().interrupt();
        }
        if(inputStream == null)
        {
            LOG.debug("Returns an empty stream object after an attempt to load cockpit config failed.");
            inputStream = new ByteArrayInputStream(new byte[0]);
        }
        return inputStream;
    }


    @Override
    public OutputStream getConfigurationOutputStream()
    {
        return new ByteArrayOutputStream()
        {
            @Override
            public void close() throws IOException
            {
                super.close();
                saveConfig(this);
            }
        };
    }


    private void saveConfig(final ByteArrayOutputStream configBuffer)
    {
        final byte[] data = configBuffer.toByteArray();
        if(data.length > 0)
        {
            getSessionService().executeInLocalView(new SessionExecutionBody()
            {
                @Override
                public void executeWithoutResult()
                {
                    final Transaction tx = Transaction.current();
                    boolean success = false;
                    try
                    {
                        tx.begin();
                        final MediaModel cockpitNGConfig = getCockpitNGConfig();
                        LOG.debug("Current cockpit config media before saving: location {}, size={}, modified time {}",
                                        cockpitNGConfig.getLocation(),
                                        cockpitNGConfig.getSize(),
                                        cockpitNGConfig.getModifiedtime());
                        getMediaService().setDataForMedia(cockpitNGConfig, data);
                        success = true;
                        LOG.info("Save cockpit config data successfully: location {}, size={}",
                                        cockpitNGConfig.getLocation(),
                                        cockpitNGConfig.getSize());
                    }
                    catch(final ModelSavingException e)
                    {
                        LOG.error("Could not save configuration", e);
                    }
                    catch(final CockpitConfigurationException e)
                    {
                        LOG.error("Error while enquiring config media", e);
                    }
                    finally
                    {
                        if(success)
                        {
                            tx.commit();
                        }
                        else
                        {
                            tx.rollback();
                        }
                    }
                }
            }, getUserService().getAdminUser());
        }
        else
        {
            LOG.info("Cockpit configuration is empty, there it will not be stored");
        }
    }


    protected InputStream getInputStreamForMedia(final MediaModel media)
    {
        try
        {
            return getSessionService().executeInLocalView(new SessionExecutionBody()
            {
                @Override
                public Object execute()
                {
                    return getMediaService().getStreamFromMedia(media);
                }
            }, getUserService().getAdminUser());
        }
        catch(final NoDataAvailableException e)
        {
            LOG.info("No data for media: {}", media.getCode());
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Error getting data for media: %s", media.getCode()), e);
            }
        }
        return null;
    }


    /*
     * Get the media by code. If there is no media createConfigFile() will be called.
     */
    protected MediaModel getCockpitNGConfig() throws CockpitConfigurationException
    {
        final MediaModel config = getBackofficeConfigurationMediaHelper().findOrCreateWidgetsConfigMedia(getConfigurationMediaCode(),
                        MIME_TYPE);
        if(config == null)
        {
            throw new CockpitConfigurationException();
        }
        getModelService().refresh(config);
        return config;
    }


    @Override
    public long getLastModification()
    {
        final MediaModel mediaModel;
        try
        {
            mediaModel = getCockpitNGConfig();
            if(mediaModel == null || mediaModel.getModifiedtime() == null)
            {
                return 0L;
            }
            else
            {
                return mediaModel.getModifiedtime().getTime();
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error("Error while enquiring config media", e);
            return 0;
        }
    }


    public MediaService getMediaService()
    {
        return mediaService;
    }


    @Required
    public void setMediaService(final MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public SessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public UserService getUserService()
    {
        return userService;
    }


    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    public BackofficeConfigurationMediaHelper getBackofficeConfigurationMediaHelper()
    {
        return backofficeConfigurationMediaHelper;
    }


    @Required
    public void setBackofficeConfigurationMediaHelper(final BackofficeConfigurationMediaHelper backofficeConfigurationMediaHelper)
    {
        this.backofficeConfigurationMediaHelper = backofficeConfigurationMediaHelper;
    }


    public String getConfigurationMediaCode()
    {
        return configurationMediaCode;
    }


    public void setConfigurationMediaCode(final String configurationMediaCode)
    {
        this.configurationMediaCode = configurationMediaCode;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
