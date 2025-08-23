package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class AbstractCMSComponentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Date modifiedtime;
    private String uid;
    private String name;
    private String typeCode;
    private String catalogVersion;
    private Map<String, Object> otherProperties;
    private String uuid;


    public void setModifiedtime(Date modifiedtime)
    {
        this.modifiedtime = modifiedtime;
    }


    public Date getModifiedtime()
    {
        return this.modifiedtime;
    }


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }


    public void setOtherProperties(Map<String, Object> otherProperties)
    {
        this.otherProperties = otherProperties;
    }


    public Map<String, Object> getOtherProperties()
    {
        return this.otherProperties;
    }


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }
}
