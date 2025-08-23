/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media.impl;

import com.hybris.cockpitng.services.media.MimeTypeResolver;

/**
 * Default implementation of <code>MimeTypeResolver</code>
 */
public class DefaultMimeTypeResolver implements MimeTypeResolver
{
    @Override
    public String getMimeFromFirstBytes(final byte[] data, final String fallback)
    {
        return fallback;
    }
}
