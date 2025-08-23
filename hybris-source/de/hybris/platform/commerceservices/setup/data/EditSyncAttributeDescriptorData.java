package de.hybris.platform.commerceservices.setup.data;

import java.io.Serializable;

public class EditSyncAttributeDescriptorData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Boolean includeInSync;
    private Boolean copyByValue;
    private Boolean untranslatable;
    private String qualifier;


    public void setIncludeInSync(Boolean includeInSync)
    {
        this.includeInSync = includeInSync;
    }


    public Boolean getIncludeInSync()
    {
        return this.includeInSync;
    }


    public void setCopyByValue(Boolean copyByValue)
    {
        this.copyByValue = copyByValue;
    }


    public Boolean getCopyByValue()
    {
        return this.copyByValue;
    }


    public void setUntranslatable(Boolean untranslatable)
    {
        this.untranslatable = untranslatable;
    }


    public Boolean getUntranslatable()
    {
        return this.untranslatable;
    }


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }
}
