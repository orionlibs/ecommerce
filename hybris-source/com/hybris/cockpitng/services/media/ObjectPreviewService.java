/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.context.CockpitContext;

/**
 * Service which allows to display preview of the media files
 */
public interface ObjectPreviewService
{
    String PREFERRED_PREVIEW_SUFFIX = "preferredPreviewSuffix";


    /**
     * Gets preview for the given mime
     *
     * @param mime type for which a default preview URL should be returned
     * @return the preview object or null if no URL matches the given mime type
     */
    ObjectPreview getPreview(String mime);


    /**
     * Gets preview for the given mime
     *
     * @param mime type for which a default preview URL should be returned
     * @param context context to resolve the preview
     * @return the preview object or null if no URL matches the given mime type
     */
    default ObjectPreview getPreview(final String mime, final CockpitContext context)
    {
        return getPreview(mime);
    }


    /**
     * Gets preview for the given object
     *
     * @param data object for which preview URL should be returned
     * @param configuration Base configuration for the type
     * @return the preview object
     */
    ObjectPreview getPreview(Object data, Base configuration);


    /**
     * Gets preview for the given object
     *
     * @param data object for which preview URL should be returned
     * @param configuration Base configuration for the type
     * @param context context to resolve the preview
     * @return the preview object
     */
    default ObjectPreview getPreview(final Object data, final Base configuration, final CockpitContext context)
    {
        return getPreview(data, configuration);
    }
}
