/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media;

/**
 * Interface which enables to resolve mime type in cockpitng
 */
public interface MimeTypeResolver
{
    /**
     * Gets mime from first bytes of <code>data</code> or <code>fallback</code> if resolving mime from data fails
     *
     * @param data
     *           data to check mime type
     * @param fallback
     *           mime that is returned if resolving mime from data fails
     * @return mime from data or <code>fallback</code>
     */
    String getMimeFromFirstBytes(byte[] data, String fallback);
}
