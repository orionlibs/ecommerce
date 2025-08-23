/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media.impl;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.util.UITools;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class StringToPreviewResolutionStrategy extends AbstractPreviewResolutionStrategy<String>
{
    private Properties extensionsToMime;


    @Override
    public String resolvePreviewUrl(final String target)
    {
        Validate.notBlank("Target URL may not be blank", target);
        return target;
    }


    @Override
    public String resolveMimeType(final String target)
    {
        return resolveMimeByExtension(target);
    }


    protected String resolveMimeByExtension(final String url)
    {
        final String extension = UITools.extractExtension(url);
        if(StringUtils.isNotBlank(extension))
        {
            final String mime = extensionsToMime.getProperty(extension);
            if(StringUtils.isNotBlank(mime))
            {
                return mime;
            }
        }
        return StringUtils.EMPTY;
    }


    @Required
    public void setExtensionsToMime(final Properties extensionsToMime)
    {
        this.extensionsToMime = extensionsToMime;
    }
}
