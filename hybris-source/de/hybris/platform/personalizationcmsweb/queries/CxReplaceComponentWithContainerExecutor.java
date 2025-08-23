package de.hybris.platform.personalizationcmsweb.queries;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.personalizationcmsweb.data.CxCmsComponentContainerData;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.impl.AbstractRestQueryExecutor;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

public class CxReplaceComponentWithContainerExecutor extends AbstractRestQueryExecutor
{
    public static final String OLD_ID = "oldComponentId";
    public static final String OLD_COMPONENT_CATALOG = "oldComponentCatalog";
    public static final String SLOT_ID = "slotId";
    private static final String[] CREATE_FIELDS = new String[] {"oldComponentId", "slotId", "catalog", "catalogVersion"};
    private CMSComponentService cmsComponentService;
    private FlexibleSearchService flexibleSearchService;
    private CatalogVersionService catalogVersionService;
    private CatalogService catalogService;
    private ModelService modelService;
    private Converter<CxCmsComponentContainerModel, CxCmsComponentContainerData> converter;


    protected void validateInputParams(Map<String, String> params, Errors errors)
    {
        validateMissingField(errors, CREATE_FIELDS);
    }


    protected Object executeAfterValidation(Map<String, String> params)
    {
        CxCmsComponentContainerModel container = replaceComponentWithContainer(findComponent(params), findSlot(params));
        return this.converter.convert(container);
    }


    protected ContentSlotModel findSlot(Map<String, String> params)
    {
        String slotCatalog = params.get("catalog");
        String slotCatalogVersion = params.get("catalogVersion");
        ContentSlotModel slotExample = new ContentSlotModel();
        slotExample.setUid(params.get("slotId"));
        slotExample.setCatalogVersion(this.catalogVersionService.getCatalogVersion(slotCatalog, slotCatalogVersion));
        try
        {
            return (ContentSlotModel)this.flexibleSearchService.getModelByExample(slotExample);
        }
        catch(ModelNotFoundException e)
        {
            throw new NotFoundException("Slot not found", "Invalid slot identifier or catalog version", e);
        }
    }


    protected SimpleCMSComponentModel findComponent(Map<String, String> params)
    {
        String currentCatalog = params.get("catalog");
        String componentCatalog = params.containsKey("oldComponentCatalog") ? params.get("oldComponentCatalog") : currentCatalog;
        CatalogVersionModel componentCatalogVersion = currentCatalog.equals(componentCatalog) ? this.catalogVersionService.getCatalogVersion(componentCatalog, params.get("catalogVersion")) : getActiveCatalogVersion(componentCatalog);
        try
        {
            return (SimpleCMSComponentModel)this.cmsComponentService.getAbstractCMSComponent(params.get("oldComponentId"), Collections.singleton(componentCatalogVersion));
        }
        catch(CMSItemNotFoundException e)
        {
            throw new NotFoundException("CMS Component not found", "Invalid component identifier or catalog version", e);
        }
    }


    protected CatalogVersionModel getActiveCatalogVersion(String componentCatalog)
    {
        CatalogModel catalog = this.catalogService.getCatalogForId(componentCatalog);
        return catalog.getActiveCatalogVersion();
    }


    protected CxCmsComponentContainerModel replaceComponentWithContainer(SimpleCMSComponentModel component, ContentSlotModel slot)
    {
        ServicesUtil.validateParameterNotNull(component, "Component cannot be null");
        ServicesUtil.validateParameterNotNull(slot, "Slot cannot be null");
        if(isComponentInSlot((AbstractCMSComponentModel)component, slot))
        {
            CxCmsComponentContainerModel cxCmsComponentContainerModel = createCxContainer(component, slot);
            replaceAndSaveComponents((AbstractCMSComponentModel)component, cxCmsComponentContainerModel, slot);
            return cxCmsComponentContainerModel;
        }
        CxCmsComponentContainerModel containerWithComponent = findCxContainerWithComponentInSlot(component, slot);
        if(containerWithComponent == null)
        {
            throw new NotFoundException("CMS Component not found in slot", "Invalid component identifier or slot identifier or catalog versions");
        }
        if(isContainerInSlotCatalog(containerWithComponent, slot))
        {
            return containerWithComponent;
        }
        CxCmsComponentContainerModel container = copyCxContainer(containerWithComponent, slot);
        replaceAndSaveComponents((AbstractCMSComponentModel)containerWithComponent, container, slot);
        return container;
    }


    protected boolean isComponentInSlot(AbstractCMSComponentModel component, ContentSlotModel slot)
    {
        return (slot.getCmsComponents() != null && slot.getCmsComponents().contains(component));
    }


    protected CxCmsComponentContainerModel createCxContainer(SimpleCMSComponentModel component, ContentSlotModel slot)
    {
        CxCmsComponentContainerModel container = (CxCmsComponentContainerModel)this.modelService.create(CxCmsComponentContainerModel.class);
        container.setDefaultCmsComponent(component);
        String uid = "cxContainer_" + slot.getUid() + "_" + component.getUid() + "_" + UUID.randomUUID();
        container.setUid(uid);
        container.setName(uid);
        container.setSourceId(uid);
        container.setCatalogVersion(slot.getCatalogVersion());
        container.setSlots(Collections.singletonList(slot));
        return container;
    }


    protected void replaceAndSaveComponents(AbstractCMSComponentModel component, CxCmsComponentContainerModel container, ContentSlotModel slot)
    {
        slot.setCmsComponents((slot.getCmsComponents() == null) ? new ArrayList() : new ArrayList(slot.getCmsComponents()));
        Collections.replaceAll(slot.getCmsComponents(), component, container);
        component.setSlots((component.getSlots() == null) ? new ArrayList() : new ArrayList(component.getSlots()));
        component.getSlots().remove(slot);
        this.modelService.saveAll(new Object[] {container, component, slot});
    }


    protected CxCmsComponentContainerModel findCxContainerWithComponentInSlot(SimpleCMSComponentModel component, ContentSlotModel slot)
    {
        if(slot.getCmsComponents() == null)
        {
            return null;
        }
        return slot.getCmsComponents().stream()
                        .filter(comp -> comp instanceof CxCmsComponentContainerModel)
                        .map(comp -> (CxCmsComponentContainerModel)comp)
                        .filter(container -> component.equals(container.getDefaultCmsComponent()))
                        .findFirst().orElse(null);
    }


    protected boolean isContainerInSlotCatalog(CxCmsComponentContainerModel container, ContentSlotModel slot)
    {
        return (slot.getCatalogVersion() != null && slot.getCatalogVersion().equals(container.getCatalogVersion()));
    }


    protected CxCmsComponentContainerModel copyCxContainer(CxCmsComponentContainerModel originalContainer, ContentSlotModel slot)
    {
        CxCmsComponentContainerModel newContainer = (CxCmsComponentContainerModel)this.modelService.create(CxCmsComponentContainerModel.class);
        newContainer.setDefaultCmsComponent(originalContainer.getDefaultCmsComponent());
        String uid = "cxContainer_" + slot.getUid() + "_" + originalContainer.getDefaultCmsComponent().getUid() + "_" + UUID.randomUUID();
        newContainer.setUid(uid);
        newContainer.setName(uid);
        newContainer.setSourceId(originalContainer.getSourceId());
        newContainer.setCatalogVersion(slot.getCatalogVersion());
        newContainer.setSlots(Collections.singletonList(slot));
        return newContainer;
    }


    public List<CatalogVersionWsDTO> getCatalogsForWriteAccess(Map<String, String> params)
    {
        String currentCatalog = params.get("catalog");
        if(currentCatalog == null || params.get("catalogVersion") == null)
        {
            Collections.emptyList();
        }
        List<CatalogVersionWsDTO> catalogVersions = new ArrayList<>();
        catalogVersions.add(createCatalogVersionDTO(currentCatalog, params.get("catalogVersion")));
        return catalogVersions;
    }


    public List<CatalogVersionWsDTO> getCatalogsForReadAccess(Map<String, String> params)
    {
        String currentCatalog = params.get("catalog");
        if(currentCatalog == null || params.get("catalogVersion") == null)
        {
            Collections.emptyList();
        }
        List<CatalogVersionWsDTO> catalogVersions = new ArrayList<>();
        catalogVersions.add(createCatalogVersionDTO(currentCatalog, params.get("catalogVersion")));
        String componentCatalog = params.get("oldComponentCatalog");
        if(componentCatalog != null && !componentCatalog.equals(currentCatalog))
        {
            catalogVersions.add(createCatalogVersionDTO(componentCatalog, getActiveCatalogVersion(componentCatalog).getVersion()));
        }
        return catalogVersions;
    }


    protected CatalogVersionWsDTO createCatalogVersionDTO(String catalog, String catalogVersion)
    {
        CatalogVersionWsDTO cv = new CatalogVersionWsDTO();
        cv.setCatalog(catalog);
        cv.setVersion(catalogVersion);
        return cv;
    }


    protected CMSComponentService getCmsComponentService()
    {
        return this.cmsComponentService;
    }


    @Required
    public void setCmsComponentService(CMSComponentService cmsComponentService)
    {
        this.cmsComponentService = cmsComponentService;
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected Converter<CxCmsComponentContainerModel, CxCmsComponentContainerData> getConverter()
    {
        return this.converter;
    }


    @Required
    public void setConverter(Converter<CxCmsComponentContainerModel, CxCmsComponentContainerData> converter)
    {
        this.converter = converter;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected CatalogService getCatalogService()
    {
        return this.catalogService;
    }


    @Required
    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }
}
