package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.core.model.media.AbstractMediaModel;
import de.hybris.platform.core.model.media.DerivedMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.media.MediaSource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ModelMediaSource implements MediaSource<AbstractMediaModel>
{
    private final AbstractMediaModel media;


    public ModelMediaSource(AbstractMediaModel media)
    {
        this.media = media;
    }


    public Long getDataPk()
    {
        return this.media.getDataPK();
    }


    public Long getMediaPk()
    {
        return (this.media.getPk() != null) ? this.media.getPk().getLong() : null;
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
        if(this.media instanceof MediaModel)
        {
            return ((MediaModel)this.media).getInternalURL();
        }
        if(this.media instanceof DerivedMediaModel)
        {
            return ((DerivedMediaModel)this.media).getMedia().getInternalURL();
        }
        return "";
    }


    public String getRealFileName()
    {
        return this.media.getRealFileName();
    }


    public String getFolderQualifier()
    {
        if(this.media instanceof MediaModel)
        {
            return ((MediaModel)this.media).getFolder().getQualifier();
        }
        if(this.media instanceof DerivedMediaModel)
        {
            return ((DerivedMediaModel)this.media).getMedia().getFolder().getQualifier();
        }
        return "";
    }


    public Long getSize()
    {
        return this.media.getSize();
    }


    public AbstractMediaModel getSource()
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
