package de.hybris.deltadetection;

import com.google.common.base.MoreObjects;
import de.hybris.deltadetection.enums.ChangeType;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ItemChangeDTO implements Serializable
{
    private final Long itemPK;
    private final String itemComposedType;
    private final Date version;
    private final ChangeType changeType;
    private final String info;
    private final String streamId;
    private String versionValue;


    public ItemChangeDTO(Long itemPK, Date version, ChangeType changeType, String info, String itemComposedType, String streamId)
    {
        this.itemPK = Objects.<Long>requireNonNull(itemPK, "itemPK is required");
        this.version = Objects.<Date>requireNonNull(version, "version is required");
        this.changeType = Objects.<ChangeType>requireNonNull(changeType, "changeType is required");
        this.info = info;
        this.itemComposedType = Objects.<String>requireNonNull(itemComposedType, "itemComposedType is required");
        this.streamId = Objects.<String>requireNonNull(streamId, "streamId is required");
    }


    public ItemChangeDTO withVersionValue(String versionValue)
    {
        this.versionValue = versionValue;
        return this;
    }


    public String getItemComposedType()
    {
        return this.itemComposedType;
    }


    public Long getItemPK()
    {
        return this.itemPK;
    }


    public Date getVersion()
    {
        return this.version;
    }


    public ChangeType getChangeType()
    {
        return this.changeType;
    }


    public String getInfo()
    {
        return this.info;
    }


    public String getStreamId()
    {
        return this.streamId;
    }


    public String getVersionValue()
    {
        return this.versionValue;
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(ItemChangeDTO.class)
                        .add("itemPK", this.itemPK)
                        .add("itemComposedType", this.itemComposedType)
                        .add("version", this.version)
                        .add("versionValue", this.versionValue)
                        .add("changeType", this.changeType)
                        .add("info", this.info)
                        .add(this.streamId, this.streamId)
                        .toString();
    }
}
