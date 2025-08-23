/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media;

import org.zkoss.util.media.Media;

/**
 * Allows to check if given Media's content type is valid
 */
public interface MimeTypeChecker
{
    /**
     * Checks if given Media's content type is valid
     *
     * @param media
     *           to check
     * @param acceptParam
     *           specifies the MIME types of files that the server accepts e.g "audio/|video/|image/*"
     * @return true if it's valid, false otherwise
     */
    boolean isMediaAcceptable(final Media media, final String acceptParam);
}
