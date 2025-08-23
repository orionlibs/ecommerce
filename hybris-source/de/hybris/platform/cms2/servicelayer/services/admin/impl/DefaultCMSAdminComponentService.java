package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.registry.CMSComponentContainerRegistry;
import de.hybris.platform.cms2.servicelayer.daos.CMSComponentDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSAdminComponentService extends AbstractCMSAdminService implements CMSAdminComponentService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSAdminComponentService.class);
    private static final String CONTAINER = "container";
    private static final String DEFAULT_UID_PREFIX = "comp_";
    protected static final String CE_SYSTEM_PROPS = "SystemProperties";
    private TypeService typeService;
    private CMSComponentDao cmsComponentDao;
    private List<String> systemProperties;
    private PersistentKeyGenerator componentUidGenerator;
    private CMSComponentContainerRegistry cmsComponentContainerRegistry;
    private Comparator<AbstractCMSComponentModel> cmsItemCatalogLevelComparator;


    public AbstractCMSComponentModel createCmsComponent(ContentSlotModel contentSlotModel, String componentUid, String componentName, String typeCode)
    {
        String eName = StringUtils.isEmpty(componentName) ? getTypeService().getComposedTypeForCode(typeCode).getName() : componentName;
        AbstractCMSComponentModel component = (AbstractCMSComponentModel)getModelService().create(typeCode);
        if(component != null)
        {
            String uid = componentUid;
            if(Strings.isNullOrEmpty(uid))
            {
                uid = generateCmsComponentUid();
            }
            component.setUid(uid);
            component.setName(eName);
            component.setCatalogVersion(getActiveCatalogVersion());
            getModelService().save(component);
            getModelService().refresh(component);
        }
        return component;
    }


    public String generateCmsComponentUid()
    {
        return "comp_" + getComponentUidGenerator().generate();
    }


    public Collection<ComposedTypeModel> getAllowedCMSComponentContainers()
    {
        return getComposedTypeOfCode(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENTCONTAINER);
    }


    public Collection<ComposedTypeModel> getAllowedCMSComponents()
    {
        Set<ComposedTypeModel> ret = new HashSet<>();
        if(getActiveSite() == null)
        {
            LOG.error("Cannot get allowed cms components, no active Site defined.");
        }
        else
        {
            if(getActiveSite().getValidComponentTypes().isEmpty())
            {
                return getComposedTypeOfCode(GeneratedCms2Constants.TC.SIMPLECMSCOMPONENT);
            }
            ret.addAll(getActiveSite().getValidComponentTypes());
        }
        return ret;
    }


    public AbstractCMSComponentContainerModel getCMSComponentContainerForId(String id) throws AmbiguousIdentifierException, UnknownIdentifierException
    {
        List<AbstractCMSComponentContainerModel> containers = getCmsComponentDao().findCMSComponentContainersByIdAndCatalogVersion(id, getActiveCatalogVersion());
        if(containers.isEmpty())
        {
            throw new UnknownIdentifierException("CMSComponentContainer with id [" + id + "] not found.");
        }
        if(containers.size() > 1)
        {
            throw new AmbiguousIdentifierException("CMSComponentContainer id '" + id + "' is not unique, " + containers
                            .size() + " cms component containers found!");
        }
        return containers.get(0);
    }


    public AbstractCMSComponentModel getCMSComponentForId(String id) throws AmbiguousIdentifierException, UnknownIdentifierException
    {
        List<AbstractCMSComponentModel> elements = getCmsComponentDao().findCMSComponentsByIdAndCatalogVersion(id,
                        getActiveCatalogVersion());
        if(elements.isEmpty())
        {
            throw new UnknownIdentifierException("CMSComponent with id [" + id + "] not found.");
        }
        if(elements.size() > 1)
        {
            throw new AmbiguousIdentifierException("CMSComponent id '" + id + "' is not unique, " + elements
                            .size() + " cms components found!");
        }
        return elements.get(0);
    }


    public AbstractCMSComponentModel getCMSComponentForIdAndCatalogVersions(String id, Collection<CatalogVersionModel> catalogVersions) throws UnknownIdentifierException
    {
        List<AbstractCMSComponentModel> elements = getCmsComponentDao().findCMSComponentsByIdAndCatalogVersions(id, catalogVersions);
        if(elements.isEmpty())
        {
            throw new UnknownIdentifierException("CMSComponent with id [" + id + "] not found.");
        }
        if(elements.size() > 1)
        {
            List<AbstractCMSComponentModel> sortedComponents = (List<AbstractCMSComponentModel>)elements.stream().sorted(getCmsItemCatalogLevelComparator()).collect(Collectors.toList());
            return (AbstractCMSComponentModel)Iterables.getLast(sortedComponents);
        }
        return elements.get(0);
    }


    public Collection<String> getEditorProperties(ItemModel component)
    {
        List<String> result = new ArrayList<>();
        ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(component.getItemtype());
        Collection<AttributeDescriptorModel> attributeDescriptors = getTypeService().getAttributeDescriptorsForType(composedType);
        if(component instanceof AbstractCMSComponentModel)
        {
            Collection<String> systemProps = getSystemProperties((AbstractCMSComponentModel)component);
            for(AttributeDescriptorModel ad : attributeDescriptors)
            {
                String qualifier = ad.getQualifier();
                if(!systemProps.contains(qualifier))
                {
                    result.add(qualifier);
                }
            }
        }
        else
        {
            for(AttributeDescriptorModel atributeDescriptor : attributeDescriptors)
            {
                result.add(atributeDescriptor.getQualifier());
            }
        }
        return result;
    }


    public Collection<String> getSystemProperties(AbstractCMSComponentModel component)
    {
        String code = component.getItemtype();
        try
        {
            return (List)Registry.getApplicationContext().getBean(code + "SystemProperties");
        }
        catch(NoSuchBeanDefinitionException nsbde)
        {
            LOG.debug("No bean found for : " + code + "SystemProperties", (Throwable)nsbde);
            return getSystemProperties();
        }
    }


    public void removeCMSComponentFromContentSlot(AbstractCMSComponentModel component, ContentSlotModel contentSlot)
    {
        List<AbstractCMSComponentModel> currentComponentList = new ArrayList<>();
        currentComponentList.addAll(contentSlot.getCmsComponents());
        currentComponentList.remove(component);
        contentSlot.setCmsComponents(currentComponentList);
        getModelService().save(contentSlot);
    }


    public List<AbstractCMSComponentModel> getAllCMSComponentsForCatalogVersion(CatalogVersionModel catalogVersionModel)
    {
        return getCmsComponentDao().findAllCMSComponentsByCatalogVersion(catalogVersionModel);
    }


    public Collection<AbstractCMSComponentModel> getCMSComponentsForContainer(AbstractCMSComponentContainerModel container)
    {
        return getCmsComponentContainerRegistry().getStrategy(container).getDisplayComponentsForContainer(container);
    }


    public Collection<AbstractCMSComponentModel> getDisplayedComponentsForContentSlot(ContentSlotModel contentSlot)
    {
        return (Collection<AbstractCMSComponentModel>)contentSlot.getCmsComponents().stream()
                        .flatMap(component -> getDisplayedComponentsForComponent(component).stream())
                        .collect(Collectors.toList());
    }


    public Collection<AbstractCMSComponentContainerModel> getContainersForContentSlot(ContentSlotModel contentSlot)
    {
        return (Collection<AbstractCMSComponentContainerModel>)contentSlot.getCmsComponents().stream().filter(this::isComponentContainer)
                        .map(component -> (AbstractCMSComponentContainerModel)component).collect(Collectors.toList());
    }


    protected Collection<AbstractCMSComponentModel> getDisplayedComponentsForComponent(AbstractCMSComponentModel component)
    {
        if(isComponentContainer(component).booleanValue())
        {
            return getCMSComponentsForContainer((AbstractCMSComponentContainerModel)component);
        }
        return Collections.singleton(component);
    }


    protected Boolean isComponentContainer(AbstractCMSComponentModel component)
    {
        ComposedTypeModel componentType = getTypeService().getComposedTypeForClass(component.getClass());
        TypeModel typeModel = getTypeService().getTypeForCode(componentType.getCode());
        Type type = (Type)getModelService().getSource(typeModel);
        return Optional.<Object>ofNullable(type.getProperty("container")).orElse(Boolean.FALSE);
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


    protected Collection<ComposedTypeModel> getComposedTypeOfCode(String code)
    {
        return getTypeService().getComposedTypeForCode(code).getAllSubTypes();
    }


    protected PersistentKeyGenerator getComponentUidGenerator()
    {
        return this.componentUidGenerator;
    }


    @Required
    public void setComponentUidGenerator(PersistentKeyGenerator componentUidGenerator)
    {
        this.componentUidGenerator = componentUidGenerator;
    }


    protected CMSComponentContainerRegistry getCmsComponentContainerRegistry()
    {
        return this.cmsComponentContainerRegistry;
    }


    @Required
    public void setCmsComponentContainerRegistry(CMSComponentContainerRegistry cmsComponentContainerRegistry)
    {
        this.cmsComponentContainerRegistry = cmsComponentContainerRegistry;
    }


    public SearchResult<AbstractCMSComponentModel> findByCatalogVersionAndMask(CatalogVersionModel catalogVersionModel, String mask, PageableData pageableData)
    {
        return getCmsComponentDao().findByCatalogVersionAndMask(catalogVersionModel, mask, pageableData);
    }


    protected Comparator<AbstractCMSComponentModel> getCmsItemCatalogLevelComparator()
    {
        return this.cmsItemCatalogLevelComparator;
    }


    @Required
    public void setCmsItemCatalogLevelComparator(Comparator<AbstractCMSComponentModel> cmsItemCatalogLevelComparator)
    {
        this.cmsItemCatalogLevelComparator = cmsItemCatalogLevelComparator;
    }
}
