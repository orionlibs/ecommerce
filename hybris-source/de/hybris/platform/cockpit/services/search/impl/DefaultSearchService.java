package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.services.search.SearchService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSearchService implements SearchService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSearchService.class);
    private TypeService typeService;
    private UIConfigurationService uiConfigurationService;
    private SearchProvider searchProvider;


    public SearchType getSearchType(ObjectTemplate objectTemplate)
    {
        BaseConfiguration baseConfig = (BaseConfiguration)this.uiConfigurationService.getComponentConfiguration(objectTemplate, "base", BaseConfiguration.class);
        return baseConfig.getSearchType();
    }


    public SearchType getSearchType(ObjectType objectType)
    {
        if(objectType instanceof SearchType)
        {
            return (SearchType)objectType;
        }
        ObjectTemplate objectTemplate = this.typeService.getObjectTemplate(objectType.getCode());
        return getSearchType(objectTemplate);
    }


    public SearchType getSearchType(String objectTemplateCode)
    {
        if(objectTemplateCode == null)
        {
            throw new IllegalArgumentException("Code can not be null.");
        }
        ObjectTemplate objectTemplate = this.typeService.getObjectTemplate(objectTemplateCode);
        return getSearchType(objectTemplate);
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setUiConfigurationService(UIConfigurationService uiConfigurationService)
    {
        this.uiConfigurationService = uiConfigurationService;
    }


    public boolean isSortable(PropertyDescriptor propDescr)
    {
        boolean sortable = false;
        AttributeDescriptorModel attrDescr = null;
        if(propDescr instanceof ItemAttributePropertyDescriptor)
        {
            attrDescr = ((ItemAttributePropertyDescriptor)propDescr).getLastAttributeDescriptor();
        }
        else if(propDescr instanceof ItemAttributeSearchDescriptor)
        {
            attrDescr = ((ItemAttributeSearchDescriptor)propDescr).getLastAttributeDescriptor();
        }
        if(attrDescr != null)
        {
            try
            {
                sortable = (TypeTools.primitiveValue(attrDescr.getSearch()) && (!(attrDescr.getAttributeType() instanceof ComposedTypeModel) || !TypeTools.primitiveValue(((ComposedTypeModel)attrDescr.getAttributeType()).getAbstract())));
            }
            catch(Exception e)
            {
                sortable = false;
                LOG.warn("Could not resolve attribute sortability.", e);
            }
        }
        return sortable;
    }


    public SearchParameterDescriptor getSearchDescriptor(PropertyDescriptor propDescr)
    {
        return getSearchDescriptor(propDescr, false);
    }


    public SearchParameterDescriptor getSearchDescriptor(PropertyDescriptor propDescr, boolean simpleSearch)
    {
        ClassAttributeSearchDescriptor classAttributeSearchDescriptor;
        GenericSearchParameterDescriptor searchDescriptor = null;
        if(propDescr instanceof ItemAttributePropertyDescriptor)
        {
            ItemAttributeSearchDescriptor itemAttributeSearchDescriptor = new ItemAttributeSearchDescriptor((ItemAttributePropertyDescriptor)propDescr);
            itemAttributeSearchDescriptor.setSimpleSearchProperty(simpleSearch);
        }
        else if(propDescr instanceof ClassAttributePropertyDescriptor)
        {
            classAttributeSearchDescriptor = new ClassAttributeSearchDescriptor((ClassAttributePropertyDescriptor)propDescr);
            classAttributeSearchDescriptor.setSimpleSearchProperty(simpleSearch);
        }
        return (SearchParameterDescriptor)classAttributeSearchDescriptor;
    }


    @Required
    public void setSearchProvider(SearchProvider searchProvider)
    {
        this.searchProvider = searchProvider;
    }


    public SearchProvider getSearchProvider()
    {
        return this.searchProvider;
    }
}
