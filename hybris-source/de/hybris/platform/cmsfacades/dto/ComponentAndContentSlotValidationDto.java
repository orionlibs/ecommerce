package de.hybris.platform.cmsfacades.dto;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import java.io.Serializable;

public class ComponentAndContentSlotValidationDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private AbstractCMSComponentModel component;
    private ContentSlotModel contentSlot;


    public void setComponent(AbstractCMSComponentModel component)
    {
        this.component = component;
    }


    public AbstractCMSComponentModel getComponent()
    {
        return this.component;
    }


    public void setContentSlot(ContentSlotModel contentSlot)
    {
        this.contentSlot = contentSlot;
    }


    public ContentSlotModel getContentSlot()
    {
        return this.contentSlot;
    }
}
