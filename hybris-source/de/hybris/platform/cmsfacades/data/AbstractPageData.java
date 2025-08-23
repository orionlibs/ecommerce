package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AbstractPageData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String pk;
    private Date creationtime;
    private Date modifiedtime;
    private String uid;
    private String name;
    private String localizedDescription;
    private String robotTag;
    private Map<String, String> title;
    private String typeCode;
    private String template;
    private Boolean defaultPage;
    private Boolean onlyOneRestrictionMustApply;
    private String localizedTitle;
    private List<PageContentSlotData> contentSlots;
    private String catalogVersionUuid;
    private Map<String, Object> otherProperties;


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public void setPk(String pk)
    {
        this.pk = pk;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public String getPk()
    {
        return this.pk;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public void setCreationtime(Date creationtime)
    {
        this.creationtime = creationtime;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public Date getCreationtime()
    {
        return this.creationtime;
    }


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


    public void setLocalizedDescription(String localizedDescription)
    {
        this.localizedDescription = localizedDescription;
    }


    public String getLocalizedDescription()
    {
        return this.localizedDescription;
    }


    public void setRobotTag(String robotTag)
    {
        this.robotTag = robotTag;
    }


    public String getRobotTag()
    {
        return this.robotTag;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public void setTitle(Map<String, String> title)
    {
        this.title = title;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public Map<String, String> getTitle()
    {
        return this.title;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTemplate(String template)
    {
        this.template = template;
    }


    public String getTemplate()
    {
        return this.template;
    }


    public void setDefaultPage(Boolean defaultPage)
    {
        this.defaultPage = defaultPage;
    }


    public Boolean getDefaultPage()
    {
        return this.defaultPage;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public void setOnlyOneRestrictionMustApply(Boolean onlyOneRestrictionMustApply)
    {
        this.onlyOneRestrictionMustApply = onlyOneRestrictionMustApply;
    }


    @Deprecated(since = "6.6", forRemoval = true)
    public Boolean getOnlyOneRestrictionMustApply()
    {
        return this.onlyOneRestrictionMustApply;
    }


    public void setLocalizedTitle(String localizedTitle)
    {
        this.localizedTitle = localizedTitle;
    }


    public String getLocalizedTitle()
    {
        return this.localizedTitle;
    }


    public void setContentSlots(List<PageContentSlotData> contentSlots)
    {
        this.contentSlots = contentSlots;
    }


    public List<PageContentSlotData> getContentSlots()
    {
        return this.contentSlots;
    }


    public void setCatalogVersionUuid(String catalogVersionUuid)
    {
        this.catalogVersionUuid = catalogVersionUuid;
    }


    public String getCatalogVersionUuid()
    {
        return this.catalogVersionUuid;
    }


    public void setOtherProperties(Map<String, Object> otherProperties)
    {
        this.otherProperties = otherProperties;
    }


    public Map<String, Object> getOtherProperties()
    {
        return this.otherProperties;
    }
}
