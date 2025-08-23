package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class CMSComponentTypesForPageSearchData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageId;
    private String mask;
    private String langIsoCode;
    private boolean readOnly;
    private List<String> requiredFields;


    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }


    public String getPageId()
    {
        return this.pageId;
    }


    public void setMask(String mask)
    {
        this.mask = mask;
    }


    public String getMask()
    {
        return this.mask;
    }


    public void setLangIsoCode(String langIsoCode)
    {
        this.langIsoCode = langIsoCode;
    }


    public String getLangIsoCode()
    {
        return this.langIsoCode;
    }


    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }


    public boolean isReadOnly()
    {
        return this.readOnly;
    }


    public void setRequiredFields(List<String> requiredFields)
    {
        this.requiredFields = requiredFields;
    }


    public List<String> getRequiredFields()
    {
        return this.requiredFields;
    }
}
