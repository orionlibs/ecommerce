package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSContentSlotService extends AbstractCMSService implements CMSContentSlotService
{
    private CMSContentSlotDao cmsContentSlotDao;
    private CMSDataFactory cmsDataFactory;
    private CatalogVersionService catalogVersionService;
    private CMSRestrictionService cmsRestrictionService;
    private CMSPageService cmsPageService;


    public String getAvailableContentSlotsNames(AbstractPageModel page)
    {
        PageTemplateModel master = page.getMasterTemplate();
        if(master != null)
        {
            List<String> result = new ArrayList<>();
            List<ContentSlotNameModel> availableContentSlots = master.getAvailableContentSlots();
            if(availableContentSlots != null)
            {
                for(ContentSlotNameModel slot : availableContentSlots)
                {
                    result.add(slot.getName());
                }
            }
            return StringUtils.join(result, "; ");
        }
        return "";
    }


    public List<String> getDefinedContentSlotPositions(AbstractPageModel page)
    {
        List<String> result = new ArrayList<>();
        for(ContentSlotForPageModel slot : this.cmsPageService.getOwnContentSlotsForPage(page))
        {
            result.add(slot.getPosition());
        }
        return result;
    }


    public List<String> getDefinedContentSlotPositions(PageTemplateModel pageTemplate)
    {
        List<String> result = new ArrayList<>();
        for(ContentSlotForTemplateModel slot : this.cmsPageService.getContentSlotsForPageTemplate(pageTemplate))
        {
            result.add(slot.getPosition());
        }
        return result;
    }


    public String getMissingContentSlotsNames(AbstractPageModel page)
    {
        List<String> result = new ArrayList<>();
        PageTemplateModel master = page.getMasterTemplate();
        if(master != null)
        {
            Collection<ContentSlotNameModel> slots = master.getAvailableContentSlots();
            if(slots != null)
            {
                List<String> predefined = getDefinedContentSlotPositions(master);
                List<String> defined = getDefinedContentSlotPositions(page);
                for(ContentSlotNameModel slot : slots)
                {
                    String name = slot.getName();
                    if(!defined.contains(name) && !predefined.contains(name))
                    {
                        result.add(name);
                    }
                }
                return StringUtils.join(result, "; ");
            }
        }
        return "";
    }


    public Collection<AbstractPageModel> getPagesForContentSlot(ContentSlotModel contentSlot)
    {
        return getCmsContentSlotDao().findPagesByContentSlot(contentSlot);
    }


    public List<SimpleCMSComponentModel> getSimpleCMSComponents(ContentSlotModel contentSlot, boolean previewEnabled, HttpServletRequest httpRequest)
    {
        List<SimpleCMSComponentModel> ret = new ArrayList<>();
        for(AbstractCMSComponentModel component : contentSlot.getCmsComponents())
        {
            boolean allowed = true;
            if(component.isRestricted() && !previewEnabled)
            {
                RestrictionData data = populate(httpRequest);
                allowed = this.cmsRestrictionService.evaluateCMSComponent(component, data);
            }
            else if(Boolean.FALSE.equals(component.getVisible()))
            {
                allowed = false;
            }
            if(allowed)
            {
                if(component.isContainer())
                {
                    AbstractCMSComponentContainerModel container = (AbstractCMSComponentContainerModel)component;
                    ret.addAll(container.getCurrentCMSComponents());
                    continue;
                }
                ret.add((SimpleCMSComponentModel)component);
            }
        }
        return ret;
    }


    public boolean isSharedSlot(ContentSlotModel contentSlot)
    {
        List<ContentSlotForTemplateModel> slotForTemplate = getCmsContentSlotDao().findAllContentSlotForTemplateByContentSlot(contentSlot, contentSlot.getCatalogVersion());
        return !slotForTemplate.isEmpty();
    }


    protected RestrictionData populate(HttpServletRequest request)
    {
        Object catalog = request.getAttribute("catalogId");
        Object category = request.getAttribute("currentCategoryCode");
        Object product = request.getAttribute("currentProductCode");
        String catalogId = Objects.nonNull(catalog) ? catalog.toString() : "";
        String categoryCode = Objects.nonNull(category) ? category.toString() : "";
        String productCode = Objects.nonNull(product) ? product.toString() : "";
        return getCmsDataFactory().createRestrictionData(categoryCode, productCode, catalogId);
    }


    public ContentSlotModel getContentSlotForId(String id) throws AmbiguousIdentifierException, UnknownIdentifierException
    {
        List<ContentSlotModel> contentSlots = getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(id,
                        getCatalogVersionService().getSessionCatalogVersions());
        if(contentSlots.isEmpty())
        {
            throw new UnknownIdentifierException("No contentSlot with id [" + id + "] found.");
        }
        if(contentSlots.size() > 1)
        {
            throw new AmbiguousIdentifierException("Content slot id '" + id + "' is not unique, " + contentSlots
                            .size() + " content slots found!");
        }
        return contentSlots.get(0);
    }


    protected CMSContentSlotDao getCmsContentSlotDao()
    {
        return this.cmsContentSlotDao;
    }


    @Required
    public void setCmsContentSlotDao(CMSContentSlotDao cmsContentSlotDao)
    {
        this.cmsContentSlotDao = cmsContentSlotDao;
    }


    protected CMSDataFactory getCmsDataFactory()
    {
        return this.cmsDataFactory;
    }


    @Required
    public void setCmsDataFactory(CMSDataFactory cmsDataFactory)
    {
        this.cmsDataFactory = cmsDataFactory;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    @Required
    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }


    public CMSRestrictionService getCmsRestrictionService()
    {
        return this.cmsRestrictionService;
    }


    @Required
    public void setCmsRestrictionService(CMSRestrictionService cmsRestrictionService)
    {
        this.cmsRestrictionService = cmsRestrictionService;
    }
}
