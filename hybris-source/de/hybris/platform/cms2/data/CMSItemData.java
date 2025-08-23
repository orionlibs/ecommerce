package de.hybris.platform.cms2.data;

import de.hybris.platform.core.PK;
import java.io.Serializable;

public class CMSItemData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String catalogId;
    private String catalogVersion;
    private PK pk;
    private String typeCode;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }


    public void setPk(PK pk)
    {
        this.pk = pk;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }
}
