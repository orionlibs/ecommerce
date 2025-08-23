package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class ContentSlotTypeRestrictionsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String contentSlotUid;
    private List<String> validComponentTypes;


    public void setContentSlotUid(String contentSlotUid)
    {
        this.contentSlotUid = contentSlotUid;
    }


    public String getContentSlotUid()
    {
        return this.contentSlotUid;
    }


    public void setValidComponentTypes(List<String> validComponentTypes)
    {
        this.validComponentTypes = validComponentTypes;
    }


    public List<String> getValidComponentTypes()
    {
        return this.validComponentTypes;
    }
}
