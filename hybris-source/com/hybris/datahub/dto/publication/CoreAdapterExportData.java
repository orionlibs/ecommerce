package com.hybris.datahub.dto.publication;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class CoreAdapterExportData
{
    private final long targetSystemPublicationId;
    private final String itemType;
    private final Locale locale;
    private final String fields;
    private final long lastProcessedId;
    private final int pageSize;
    private String targetName;


    public CoreAdapterExportData(long pubId, String targetName, String itemType, String loc, String fields, Long lastId, int pageSize)
    {
        this.targetSystemPublicationId = pubId;
        this.targetName = targetName;
        this.itemType = itemType;
        this.locale = StringUtils.isEmpty(loc) ? null : new Locale(loc);
        this.fields = (fields != null) ? fields : "";
        this.lastProcessedId = lastId.longValue();
        this.pageSize = pageSize;
    }


    public CoreAdapterExportData(long pubId, String type, String loc, String fields, Long lastId, int cnt)
    {
        this(pubId, null, type, loc, fields, lastId, cnt);
    }


    public long getTargetSystemPublicationId()
    {
        return this.targetSystemPublicationId;
    }


    public String getTargetName()
    {
        return this.targetName;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public Locale getLanguageLocale()
    {
        return this.locale;
    }


    public String getLocale()
    {
        return this.locale.getLanguage();
    }


    public String getFields()
    {
        return this.fields;
    }


    public long getLastProcessedId()
    {
        return this.lastProcessedId;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public boolean impliesNoData()
    {
        assert this.itemType != null : "Item Type comes from a path parameter and therefore cannot be null";
        assert this.fields != null : "Always initialized in the constructor";
        return (this.itemType.isEmpty() || this.fields.trim().isEmpty());
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        CoreAdapterExportData that = (CoreAdapterExportData)o;
        if(this.lastProcessedId != that.lastProcessedId)
        {
            return false;
        }
        if(this.pageSize != that.pageSize)
        {
            return false;
        }
        if(this.targetSystemPublicationId != that.targetSystemPublicationId)
        {
            return false;
        }
        if((this.itemType != null) ? !this.itemType.equals(that.itemType) : (that.itemType != null))
        {
            return false;
        }
        if((this.locale != null) ? !this.locale.equals(that.locale) : (that.locale != null))
        {
            return false;
        }
        if((this.targetName != null) ? !this.targetName.equals(that.targetName) : (that.targetName != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = (int)(this.targetSystemPublicationId ^ this.targetSystemPublicationId >>> 32L);
        result = 31 * result + ((this.itemType != null) ? this.itemType.hashCode() : 0);
        result = 31 * result + ((this.locale != null) ? this.locale.hashCode() : 0);
        result = 31 * result + (int)(this.lastProcessedId ^ this.lastProcessedId >>> 32L);
        result = 31 * result + this.pageSize;
        result = 31 * result + ((this.targetName != null) ? this.targetName.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "CoreAdapterExportData{targetSystemPublicationId=" + this.targetSystemPublicationId + ", itemType='" + this.itemType + "', locale=" + this.locale + ", fields='" + this.fields + "', lastProcessedId=" + this.lastProcessedId + ", pageSize=" + this.pageSize + ", targetName='"
                        + this.targetName + "'}";
    }
}
