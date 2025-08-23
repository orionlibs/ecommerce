package de.hybris.platform.media.impl;

import de.hybris.platform.jalo.media.AbstractMedia;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.media.MediaSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JaloMediaSource implements MediaSource<AbstractMedia>
{
    private final AbstractMedia media;


    public JaloMediaSource(AbstractMedia media)
    {
        this.media = media;
    }


    public Long getDataPk()
    {
        return this.media.getDataPK();
    }


    public Long getMediaPk()
    {
        return this.media.getPK().getLong();
    }


    public String getLocation()
    {
        return this.media.getLocation();
    }


    public String getLocationHash()
    {
        return this.media.getLocationHash();
    }


    public String getMime()
    {
        return this.media.getMime();
    }


    public String getInternalUrl()
    {
        return (this.media instanceof Media) ? (String)this.media.getProperty("internalURL") : "";
    }


    public String getRealFileName()
    {
        return this.media.getRealFileName();
    }


    public String getFolderQualifier()
    {
        return (this.media instanceof Media) ? ((Media)this.media).getFolder().getQualifier() : "";
    }


    public Long getSize()
    {
        return this.media.getSize();
    }


    public AbstractMedia getSource()
    {
        return this.media;
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("dataPk", getDataPk())
                        .append("mediaPk", getMediaPk())
                        .append("location", getLocation())
                        .append("locationHash", getLocationHash())
                        .append("mime", getMime())
                        .append("internalUrl", getInternalUrl())
                        .append("realFileName", getRealFileName())
                        .append("folderQualifier", getFolderQualifier())
                        .append("size", getSize())
                        .append("source", getSource())
                        .toString();
    }
}
