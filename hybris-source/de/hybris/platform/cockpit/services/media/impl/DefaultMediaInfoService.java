package de.hybris.platform.cockpit.services.media.impl;

import de.hybris.platform.cockpit.services.media.MediaInfo;
import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.core.model.media.MediaModel;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class DefaultMediaInfoService implements MediaInfoService
{
    private String fallbackIcon;
    private Set<MediaInfo> nonWebMedias;
    private Set<MediaInfo> webMedias;


    public String getFallbackIcon()
    {
        return this.fallbackIcon;
    }


    public MediaInfo getNonWebMediaInfo(String mime)
    {
        return (MediaInfo)CollectionUtils.find(getNonWebMediaInfos(), (Predicate)new Object(this, mime));
    }


    public Set<MediaInfo> getNonWebMediaInfos()
    {
        return this.nonWebMedias;
    }


    public Set<MediaInfo> getWebMediaInfos()
    {
        return this.webMedias;
    }


    public Boolean isWebMedia(MediaModel media)
    {
        return Boolean.valueOf(CollectionUtils.exists(getWebMediaInfos(), (Predicate)new Object(this, media)));
    }


    public Boolean isWebMedia(String mime)
    {
        return Boolean.valueOf(CollectionUtils.exists(getWebMediaInfos(), (Predicate)new Object(this, mime)));
    }


    public void setFallbackIcon(String fallbackIcon)
    {
        this.fallbackIcon = fallbackIcon;
    }


    public void setNonWebMedias(Set<MediaInfo> nonWebMedias)
    {
        this.nonWebMedias = nonWebMedias;
    }


    public void setWebMedias(Set<MediaInfo> webMedias)
    {
        this.webMedias = webMedias;
    }
}
