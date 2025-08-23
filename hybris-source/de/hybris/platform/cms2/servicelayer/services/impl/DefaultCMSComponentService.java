package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSComponentDao;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSComponentService extends AbstractCMSService implements CMSComponentService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSComponentService.class.getName());
    protected static final String CE_SYSTEM_PROPS = "SystemProperties";
    private CMSComponentDao cmsComponentDao;
    private List<String> systemProperties;
    private TypeService typeService;
    private CatalogVersionService catalogVersionService;
    private Map<String, List<String>> cSystemProperties = null;
    private Map<String, List<String>> cEditorProperties = null;
    private CMSContentSlotService cmsContentSlotService;
    private ModelService modelService;


    public DefaultCMSComponentService()
    {
        this.cSystemProperties = new ConcurrentHashMap<>();
        this.cEditorProperties = new ConcurrentHashMap<>();
    }


    public <T extends AbstractCMSComponentModel> T getAbstractCMSComponent(String id) throws CMSItemNotFoundException
    {
        List<AbstractCMSComponentModel> components = getCmsComponentDao().findCMSComponentsByIdAndCatalogVersions(id,
                        getCatalogVersionService().getSessionCatalogVersions());
        if(components.isEmpty())
        {
            throw new CMSItemNotFoundException("No AbstractCMSComponentModel found with id [" + id + "]");
        }
        return (T)components.iterator().next();
    }


    public <T extends AbstractCMSComponentModel> SearchPageData<T> getAbstractCMSComponents(Collection<String> ids, SearchPageData searchPageData)
    {
        return getCmsComponentDao().findCMSComponentsByIdsAndCatalogVersions(ids,
                        getCatalogVersionService().getSessionCatalogVersions(), searchPageData);
    }


    public <T extends AbstractCMSComponentModel> SearchPageData<T> getAllAbstractCMSComponents(SearchPageData searchPageData)
    {
        return getCmsComponentDao().findAllCMSComponentsByCatalogVersions(
                        getCatalogVersionService().getSessionCatalogVersions(), searchPageData);
    }


    public <T extends AbstractCMSComponentModel> T getAbstractCMSComponent(String id, Collection<CatalogVersionModel> catalogVersions) throws CMSItemNotFoundException
    {
        List<AbstractCMSComponentModel> element = getCmsComponentDao().findCMSComponentsByIdAndCatalogVersions(id, catalogVersions);
        if(element.isEmpty())
        {
            throw new CMSItemNotFoundException("No SimpleContentElementModel found with id [" + id + "]");
        }
        return (T)element.get(0);
    }


    public <T extends AbstractCMSComponentModel> T getAbstractCMSComponent(String id, String contentSlotId, Collection<CatalogVersionModel> catalogVersions) throws CMSItemNotFoundException
    {
        List<AbstractCMSComponentModel> element = getCmsComponentDao().findCMSComponents(id, contentSlotId, catalogVersions);
        if(element.isEmpty())
        {
            throw new CMSItemNotFoundException("No SimpleContentElementModel found with id [" + id + "]");
        }
        return (T)element.get(0);
    }


    public Collection<String> getEditorProperties(AbstractCMSComponentModel component)
    {
        return getEditorProperties(component, false);
    }


    public Collection<String> getReadableEditorProperties(AbstractCMSComponentModel component)
    {
        return getEditorProperties(component, true);
    }


    protected Collection<String> getEditorProperties(AbstractCMSComponentModel component, boolean readableOnly)
    {
        String code = component.getItemtype();
        if(!this.cEditorProperties.containsKey(code))
        {
            LOG.debug("caching editor properties for CMSComponent [" + component.getItemtype() + "]");
            List<String> props = new ArrayList<>();
            Collection<String> systemProps = getSystemProperties(component);
            Set<AttributeDescriptorModel> attributeDescriptors = getTypeService().getAttributeDescriptorsForType(getTypeService().getComposedTypeForCode(code));
            for(AttributeDescriptorModel ad : attributeDescriptors)
            {
                String qualifier = ad.getQualifier();
                if(!systemProps.contains(qualifier) && (!readableOnly || ad.getReadable().booleanValue()))
                {
                    props.add(qualifier);
                }
            }
            this.cEditorProperties.put(code, props);
        }
        return this.cEditorProperties.get(code);
    }


    public <T extends de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel> T getSimpleCMSComponent(String id) throws CMSItemNotFoundException
    {
        AbstractCMSComponentModel component = getAbstractCMSComponent(id);
        if(!(component instanceof de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel))
        {
            throw new CMSItemNotFoundException("Component is not of type SimpleCMSComponentModel [" + component.getItemtype() + "]");
        }
        return (T)component;
    }


    public Collection<String> getSystemProperties(AbstractCMSComponentModel component)
    {
        String code = component.getItemtype();
        if(!this.cSystemProperties.containsKey(code))
        {
            LOG.debug("caching system properties for CMSComponent [" + component.getItemtype() + "]");
            List<String> props = null;
            try
            {
                props = (List<String>)Registry.getApplicationContext().getBean(code + "SystemProperties");
            }
            catch(NoSuchBeanDefinitionException nsbde)
            {
                LOG.debug("No bean found for : " + code + "SystemProperties", (Throwable)nsbde);
                props = getSystemProperties();
            }
            this.cSystemProperties.put(code, props);
        }
        return this.cSystemProperties.get(code);
    }


    public boolean isComponentContainer(String componentTypeCode)
    {
        TypeModel typeModel = getTypeService().getTypeForCode(componentTypeCode);
        Type type = (Type)this.modelService.getSource(typeModel);
        return ((Boolean)type.getProperty("container")).booleanValue();
    }


    public boolean isComponentRestricted(AbstractCMSComponentModel component)
    {
        return CollectionUtils.isNotEmpty(component.getRestrictions());
    }


    public boolean isComponentUsedOutsidePage(AbstractCMSComponentModel component, AbstractPageModel page)
    {
        return (getCmsComponentDao().getComponentReferenceCountOutsidePage(component, page) > 0L);
    }


    public Set<AbstractCMSComponentModel> getAllParents(AbstractCMSComponentModel cmsComponentModel)
    {
        List<AbstractCMSComponentModel> parents = cmsComponentModel.getParents();
        Set<AbstractCMSComponentModel> allParents = new HashSet<>();
        while(null != parents && !parents.isEmpty())
        {
            allParents.addAll(parents);
            parents = (List<AbstractCMSComponentModel>)parents.stream().filter(componentModel -> !getModelService().isNew(componentModel)).flatMap(componentModel -> CollectionUtils.emptyIfNull(componentModel.getParents()).stream()).filter(componentModel -> !allParents.contains(componentModel))
                            .collect(Collectors.toList());
        }
        allParents.remove(cmsComponentModel);
        return allParents;
    }


    public Set<AbstractCMSComponentModel> getAllChildren(AbstractCMSComponentModel cmsComponentModel)
    {
        List<AbstractCMSComponentModel> children = cmsComponentModel.getChildren();
        Set<AbstractCMSComponentModel> allChildren = new HashSet<>();
        while(null != children && !children.isEmpty())
        {
            allChildren.addAll(children);
            children = (List<AbstractCMSComponentModel>)children.stream().filter(componentModel -> !getModelService().isNew(componentModel)).flatMap(child -> CollectionUtils.emptyIfNull(child.getChildren()).stream()).filter(child -> !allChildren.contains(child)).collect(Collectors.toList());
        }
        allChildren.remove(cmsComponentModel);
        return allChildren;
    }


    public boolean inSharedSlots(AbstractCMSComponentModel componentModel)
    {
        if(CollectionUtils.emptyIfNull(componentModel.getSlots())
                        .stream()
                        .anyMatch(slot -> (!getModelService().isNew(slot) && getCmsContentSlotService().isSharedSlot(slot))))
        {
            return true;
        }
        Set<AbstractCMSComponentModel> parentComponents = getAllParents(componentModel);
        return parentComponents
                        .stream()
                        .flatMap(component -> CollectionUtils.emptyIfNull(component.getSlots()).stream().filter(Objects::nonNull))
                        .anyMatch(slot -> (!getModelService().isNew(slot) && getCmsContentSlotService().isSharedSlot(slot)));
    }


    protected CMSComponentDao getCmsComponentDao()
    {
        return this.cmsComponentDao;
    }


    @Required
    public void setCmsComponentDao(CMSComponentDao cmsComponentDao)
    {
        this.cmsComponentDao = cmsComponentDao;
    }


    protected List<String> getSystemProperties()
    {
        return this.systemProperties;
    }


    @Required
    public void setSystemProperties(List<String> systemProperties)
    {
        this.systemProperties = systemProperties;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
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


    protected CMSContentSlotService getCmsContentSlotService()
    {
        return this.cmsContentSlotService;
    }


    @Required
    public void setCmsContentSlotService(CMSContentSlotService cmsContentSlotService)
    {
        this.cmsContentSlotService = cmsContentSlotService;
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
}
