package de.hybris.platform.cms2.servicelayer.data.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collection;
import java.util.Collections;

public class DefaultContentSlotData implements ContentSlotData
{
    private static final long serialVersionUID = -7366547989914197595L;
    private final String pageId;
    private final String position;
    private final String name;
    private final String uid;
    private final boolean fromMaster;
    private final ContentSlotModel contentSlot;
    private final boolean allowOverwrite;
    private boolean isOverrideSlot;
    private Collection<ComposedTypeModel> availableCMSComponents;
    private Collection<ComposedTypeModel> availableCMSComponentContainers;


    protected DefaultContentSlotData(AbstractPageModel page, ContentSlotForTemplateModel csForTemplate)
    {
        this(page.getUid(), csForTemplate.getContentSlot(), csForTemplate.getPosition(), true, csForTemplate
                        .getAllowOverwrite().booleanValue());
    }


    protected DefaultContentSlotData(ContentSlotForPageModel csForPage)
    {
        this(csForPage.getPage().getUid(), csForPage.getContentSlot(), csForPage.getPosition(), false, true);
    }


    protected DefaultContentSlotData(String pageId, ContentSlotModel contentSlot, String position, boolean fromMaster, boolean allowOverwrite)
    {
        this.pageId = pageId;
        this.contentSlot = contentSlot;
        this.name = contentSlot.getName();
        this.uid = contentSlot.getUid();
        this.position = position;
        this.fromMaster = fromMaster;
        this.allowOverwrite = allowOverwrite;
        this.isOverrideSlot = false;
        if(!position.equals(contentSlot.getCurrentPosition()))
        {
            contentSlot.setCurrentPosition(position);
        }
    }


    public boolean isFromMaster()
    {
        return this.fromMaster;
    }


    public String getPosition()
    {
        return this.position;
    }


    public String getName()
    {
        return this.name;
    }


    public String getUid()
    {
        return this.uid;
    }


    public ContentSlotModel getContentSlot()
    {
        return this.contentSlot;
    }


    public boolean isAllowOverwrite()
    {
        return this.allowOverwrite;
    }


    public boolean isOverrideSlot()
    {
        return this.isOverrideSlot;
    }


    public Collection<ComposedTypeModel> getAvailableCMSComponents()
    {
        if(this.availableCMSComponents == null)
        {
            return Collections.emptyList();
        }
        return this.availableCMSComponents;
    }


    public void setAvailableCMSComponents(Collection<ComposedTypeModel> availableCMSComponents)
    {
        this.availableCMSComponents = availableCMSComponents;
    }


    public Collection<ComposedTypeModel> getAvailableCMSComponentContainers()
    {
        if(this.availableCMSComponentContainers == null)
        {
            return Collections.emptyList();
        }
        return this.availableCMSComponentContainers;
    }


    public void setAvailableCMSComponentContainers(Collection<ComposedTypeModel> availableCMSComponentContainers)
    {
        this.availableCMSComponentContainers = availableCMSComponentContainers;
    }


    public Collection<AbstractCMSComponentModel> getCMSComponents()
    {
        return this.contentSlot.getCmsComponents();
    }


    public String getPageId()
    {
        return this.pageId;
    }


    public void setIsOverrideSlot(Boolean isOverrideSlot)
    {
        this.isOverrideSlot = isOverrideSlot.booleanValue();
    }
}
