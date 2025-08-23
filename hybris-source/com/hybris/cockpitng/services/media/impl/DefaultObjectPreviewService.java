/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.services.media.PreviewResolutionStrategy;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Service which allows to display preview of the media files
 */
public class DefaultObjectPreviewService implements ObjectPreviewService
{
    public static final Logger LOG = LoggerFactory.getLogger(DefaultObjectPreviewService.class);
    public static final String IMAGE_PNG_MIME = "image/png";
    protected static final String DEFAULT_PREVIEW_ICON = "icon_filetype_unknown.png";
    protected static final String MIME_PREVIEW_ICONS_PATH = "cng/img/mime-icons/";
    protected static final String EMPTY_PREVIEW_ICON = "cng/img/mime-icons/icon_filetype_unknown.png";
    private PropertyValueService propertyValueService;
    private PermissionFacade permissionFacade;
    private List<PreviewResolutionStrategy> urlResolutionStrategies;
    private Properties mediaGroups;
    private Properties mediaIcons;
    private Properties webMimes;


    @Override
    public ObjectPreview getPreview(final String mime)
    {
        return getPreview(mime, new DefaultCockpitContext());
    }


    @Override
    public ObjectPreview getPreview(final String mime, final CockpitContext context)
    {
        final String mediaGroup = StringUtils.defaultIfBlank(mediaGroups.getProperty(mime), ObjectPreview.GROUP_UNKNOWN);
        if(StringUtils.isNotBlank(mediaGroup))
        {
            final Object suffix = context.getParameter(ObjectPreviewService.PREFERRED_PREVIEW_SUFFIX);
            String mediaIcon = null;
            if(suffix instanceof String && StringUtils.isNotBlank((CharSequence)suffix))
            {
                mediaIcon = mediaIcons.getProperty(mediaGroup + '.' + suffix);
            }
            if(StringUtils.isBlank(mediaIcon))
            {
                mediaIcon = mediaIcons.getProperty(mediaGroup);
            }
            if(StringUtils.isNotBlank(mediaIcon))
            {
                return new ObjectPreview(MIME_PREVIEW_ICONS_PATH + mediaIcon, IMAGE_PNG_MIME, true, mediaGroup);
            }
        }
        return new ObjectPreview(MIME_PREVIEW_ICONS_PATH + DEFAULT_PREVIEW_ICON, IMAGE_PNG_MIME, true, mediaGroup);
    }


    @Override
    public ObjectPreview getPreview(final Object data, final Base configuration)
    {
        return getPreview(data, configuration, new DefaultCockpitContext());
    }


    @Override
    public ObjectPreview getPreview(final Object target, final Base configuration, final CockpitContext context)
    {
        ObjectPreview objectPreview = null;
        if(configuration != null && configuration.getPreview() != null
                        && StringUtils.isNotBlank(configuration.getPreview().getUrlQualifier()))
        {
            final Object imageObject = propertyValueService.readValue(target, configuration.getPreview().getUrlQualifier());
            if(canReadPreview(imageObject))
            {
                objectPreview = getObjectPreviewByStrategy(imageObject, context);
            }
            if(objectPreview == null && configuration.getPreview().isFallbackToIcon())
            {
                objectPreview = getObjectPreviewFallbackByIcon(configuration, context);
            }
            if(objectPreview == null)
            {
                objectPreview = getPreview(StringUtils.EMPTY, context);
            }
        }
        return objectPreview;
    }


    private boolean canReadPreview(final Object imageObject)
    {
        return imageObject != null && permissionFacade.canReadInstance(imageObject);
    }


    private ObjectPreview getObjectPreviewByStrategy(final Object imageObject, final CockpitContext context)
    {
        for(final PreviewResolutionStrategy strategy : urlResolutionStrategies)
        {
            if(strategy.canResolve(imageObject))
            {
                final String url = strategy.resolvePreviewUrl(imageObject);
                final String mime = strategy.resolveMimeType(imageObject);
                if(StringUtils.isNotBlank(url) && isWebMime(mime))
                {
                    return new ObjectPreview(url, mime, false);
                }
                return getPreview(mime, context);
            }
        }
        return null;
    }


    private ObjectPreview getObjectPreviewFallbackByIcon(final Base configuration, final CockpitContext context)
    {
        if(configuration.getPreview().isFallbackToIcon() && configuration.getLabels() != null)
        {
            final String iconPath = configuration.getLabels().getIconPath();
            if(StringUtils.isNotBlank(iconPath))
            {
                for(final PreviewResolutionStrategy strategy : urlResolutionStrategies)
                {
                    if(strategy.canResolve(iconPath))
                    {
                        final String mime = strategy.resolveMimeType(iconPath);
                        if(isWebMime(mime))
                        {
                            return new ObjectPreview(iconPath, mime, true);
                        }
                    }
                }
            }
        }
        return null;
    }


    private boolean isWebMime(final String mime)
    {
        return StringUtils.isNotBlank(mime) && webMimes.containsKey(mime);
    }


    @Required
    public void setMediaIcons(final Properties mediaIcons)
    {
        this.mediaIcons = mediaIcons;
    }


    @Required
    public void setMediaGroups(final Properties mediaGroups)
    {
        this.mediaGroups = mediaGroups;
    }


    @Required
    public void setWebMimes(final Properties webMimes)
    {
        this.webMimes = webMimes;
    }


    @Required
    public void setPropertyValueService(final PropertyValueService propertyValueService)
    {
        this.propertyValueService = propertyValueService;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public List<PreviewResolutionStrategy> getUrlResolutionStrategies()
    {
        return urlResolutionStrategies;
    }


    @Required
    public void setUrlResolutionStrategies(final List<PreviewResolutionStrategy> urlResolutionStrategies)
    {
        this.urlResolutionStrategies = urlResolutionStrategies;
    }
}
