/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcpiorderexchangeb2b.service.impl;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.sap.sapcpiorderexchangeb2b.service.Sapcpiorderexchangeb2bService;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * DefaultSapcpiorderexchangeb2bService
 */
public class DefaultSapcpiorderexchangeb2bService implements Sapcpiorderexchangeb2bService
{
    private static final Logger LOG = Logger.getLogger(DefaultSapcpiorderexchangeb2bService.class);
    private MediaService mediaService;


    @Override
    public String getHybrisLogoUrl(final String logoCode)
    {
        final MediaModel media = mediaService.getMedia(logoCode);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Found media [code: " + media.getCode() + "]");
        }
        return media.getURL();
    }


    @Override
    public void createLogo(final String logoCode)
    {
        //
    }


    private InputStream getImageStream()
    {
        return DefaultSapcpiorderexchangeb2bService.class.getResourceAsStream("/sapcpiorderexchangeb2b/sap-hybris-platform.png");
    }


    @Required
    public void setMediaService(final MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
