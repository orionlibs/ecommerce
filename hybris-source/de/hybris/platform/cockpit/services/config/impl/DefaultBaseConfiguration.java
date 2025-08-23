package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.DefaultPropertySettings;
import de.hybris.platform.cockpit.services.config.InitialPropertyConfiguration;
import de.hybris.platform.cockpit.services.label.ObjectLabelProvider;
import de.hybris.platform.cockpit.services.meta.TypeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DefaultBaseConfiguration implements BaseConfiguration
{
    private SearchType searchType;
    private ObjectLabelProvider objectLabelProvider;
    private List<InitialPropertyConfiguration> initialPropertyConfigurations;
    private List<DefaultPropertySettings> declaredPropertySettings = Collections.EMPTY_LIST;


    public void setSearchType(SearchType searchType)
    {
        this.searchType = searchType;
    }


    public SearchType getSearchType()
    {
        return this.searchType;
    }


    public void setObjectLabelProvider(ObjectLabelProvider objectLabelProvider)
    {
        this.objectLabelProvider = objectLabelProvider;
    }


    public ObjectLabelProvider getObjectLabelProvider()
    {
        return this.objectLabelProvider;
    }


    private InitialPropertyConfiguration getInitialPropertyConfigurationInternal(ObjectTemplate objectTemplate)
    {
        if(objectTemplate == null)
        {
            return null;
        }
        if(this.initialPropertyConfigurations == null)
        {
            return null;
        }
        for(InitialPropertyConfiguration config : this.initialPropertyConfigurations)
        {
            if(objectTemplate.equals(config.getSourceObjectTemplate()))
            {
                return config;
            }
        }
        return null;
    }


    public InitialPropertyConfiguration getInitialPropertyConfiguration(ObjectTemplate objectTemplate, TypeService typeService)
    {
        InitialPropertyConfiguration initialPropertyConfig = getInitialPropertyConfigurationInternal(objectTemplate);
        BaseType baseType = objectTemplate.getBaseType();
        while(initialPropertyConfig == null && typeService != null)
        {
            Set<ObjectType> supertypes = baseType.getSupertypes();
            ObjectType superType = (supertypes.size() == 1) ? supertypes.iterator().next() : null;
            if(superType == null)
            {
                break;
            }
            ObjectType objectType1 = superType;
            ObjectTemplate template = typeService.getObjectTemplate(superType.getCode());
            if(template == null)
            {
                break;
            }
            initialPropertyConfig = getInitialPropertyConfigurationInternal(template);
        }
        return initialPropertyConfig;
    }


    public void setInitialPropertyConfigurations(List<InitialPropertyConfiguration> initialPropertyConfigurations)
    {
        this.initialPropertyConfigurations = initialPropertyConfigurations;
    }


    public List<DefaultPropertySettings> getBaseProperties()
    {
        List<DefaultPropertySettings> result = new ArrayList();
        for(DefaultPropertySettings dps : this.declaredPropertySettings)
        {
            if(dps.isBaseProperty())
            {
                result.add(dps);
            }
        }
        return result;
    }


    public List<DefaultPropertySettings> getAllDefaultPropertySettings()
    {
        return this.declaredPropertySettings;
    }


    public DefaultPropertySettings getDefaultPropertySettings(PropertyDescriptor propertyDescriptor)
    {
        DefaultPropertySettings result = null;
        for(DefaultPropertySettings dps : this.declaredPropertySettings)
        {
            if(propertyDescriptor.equals(dps.getPropertyDescriptor()))
            {
                result = dps;
            }
        }
        return result;
    }


    public void setDeclaredPropertySettings(List<DefaultPropertySettings> declaredPropertySettings)
    {
        this.declaredPropertySettings = Collections.unmodifiableList(declaredPropertySettings);
    }
}
