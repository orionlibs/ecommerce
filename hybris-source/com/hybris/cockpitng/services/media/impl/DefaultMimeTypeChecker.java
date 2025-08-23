/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media.impl;

import com.hybris.cockpitng.services.media.MimeTypeChecker;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.media.Media;

/**
 * Default implementation of {@link MimeTypeChecker}
 */
public class DefaultMimeTypeChecker implements MimeTypeChecker
{
    @Override
    public boolean isMediaAcceptable(final Media media, final String acceptParam)
    {
        return StringUtils.isEmpty(acceptParam) || matchContentType(acceptParam, media.getContentType())
                        || StringUtils.containsIgnoreCase(acceptParam, media.getFormat());
    }


    private boolean matchContentType(final String accept, final String contentType)
    {
        if(StringUtils.isEmpty(accept) || StringUtils.isEmpty(contentType))
        {
            return false;
        }
        final String regexp = StringUtils.replace(accept, "*", ".*");
        return Pattern.matches(regexp, contentType);
    }
}
