package de.hybris.platform.cmsfacades.dto;

import de.hybris.platform.cmsfacades.data.AbstractPageData;
import java.io.Serializable;

@Deprecated(since = "6.6", forRemoval = true)
public class UpdatePageValidationDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String originalUid;
    private AbstractPageData page;


    public void setOriginalUid(String originalUid)
    {
        this.originalUid = originalUid;
    }


    public String getOriginalUid()
    {
        return this.originalUid;
    }


    public void setPage(AbstractPageData page)
    {
        this.page = page;
    }


    public AbstractPageData getPage()
    {
        return this.page;
    }
}
