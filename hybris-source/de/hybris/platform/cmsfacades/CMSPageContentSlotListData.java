package de.hybris.platform.cmsfacades;

import java.io.Serializable;
import java.util.List;

public class CMSPageContentSlotListData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageId;
    private List<String> slotIds;


    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }


    public String getPageId()
    {
        return this.pageId;
    }


    public void setSlotIds(List<String> slotIds)
    {
        this.slotIds = slotIds;
    }


    public List<String> getSlotIds()
    {
        return this.slotIds;
    }
}
