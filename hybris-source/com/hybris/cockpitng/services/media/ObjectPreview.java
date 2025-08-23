/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media;

import com.hybris.cockpitng.core.util.Validate;
import org.apache.commons.lang3.StringUtils;

public class ObjectPreview
{
    public static final String GROUP_UNKNOWN = "unknown";
    private final String url;
    private final String mime;
    private final boolean fallback;
    private String group;


    public ObjectPreview(final String url, final String mime, final boolean fallback)
    {
        this(url, mime, fallback, StringUtils.EMPTY);
    }


    public ObjectPreview(final String url, final String mime, final boolean fallback, final String group)
    {
        Validate.notBlank("URL and mime type may not be blank", url, mime);
        this.group = group;
        this.url = url;
        this.mime = mime;
        this.fallback = fallback;
    }


    public boolean isFallback()
    {
        return fallback;
    }


    public String getUrl()
    {
        return url;
    }


    public String getMime()
    {
        return mime;
    }


    public String getGroup()
    {
        return group;
    }
}
