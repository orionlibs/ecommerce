package de.hybris.platform.cmsfacades.dto;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import java.io.Serializable;

public class ComponentTypeAndContentSlotValidationDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String componentType;
    private ContentSlotModel contentSlot;
    private AbstractPageModel page;


    public void setComponentType(String componentType)
    {
        this.componentType = componentType;
    }


    public String getComponentType()
    {
        return this.componentType;
    }


    public void setContentSlot(ContentSlotModel contentSlot)
    {
        this.contentSlot = contentSlot;
    }


    public ContentSlotModel getContentSlot()
    {
        return this.contentSlot;
    }


    public void setPage(AbstractPageModel page)
    {
        this.page = page;
    }


    public AbstractPageModel getPage()
    {
        return this.page;
    }
}
