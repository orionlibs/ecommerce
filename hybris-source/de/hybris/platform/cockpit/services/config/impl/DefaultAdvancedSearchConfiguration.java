package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldGroupConfiguration;
import java.util.Collections;
import java.util.List;

public class DefaultAdvancedSearchConfiguration extends AbstractUIComponentConfiguration implements AdvancedSearchConfiguration
{
    private SearchFieldGroupConfiguration rootGroupConfiguration;
    private List<ObjectTemplate> relatedTypes = Collections.emptyList();
    private final ObjectTemplate rootType;
    private boolean excludeRootType = false;
    private boolean includeSubTypes = true;
    private boolean includeSubTypesForRelatedTypes = false;


    public DefaultAdvancedSearchConfiguration(ObjectTemplate rootType)
    {
        this.rootType = rootType;
    }


    public void setRootGroupConfiguration(SearchFieldGroupConfiguration rootGroupConfiguration)
    {
        this.rootGroupConfiguration = rootGroupConfiguration;
    }


    public SearchFieldGroupConfiguration getRootGroupConfiguration()
    {
        return this.rootGroupConfiguration;
    }


    public void setRelatedTypes(List<ObjectTemplate> relatedTypes)
    {
        this.relatedTypes = relatedTypes;
    }


    public List<ObjectTemplate> getRelatedTypes()
    {
        return this.relatedTypes;
    }


    public ObjectTemplate getRootType()
    {
        return this.rootType;
    }


    public void setExcludeRootType(boolean excludeRootType)
    {
        this.excludeRootType = excludeRootType;
    }


    public boolean isExcludeRootType()
    {
        return this.excludeRootType;
    }


    public void setIncludeSubTypes(boolean includeSubTypes)
    {
        this.includeSubTypes = includeSubTypes;
    }


    public boolean isIncludeSubTypes()
    {
        return this.includeSubTypes;
    }


    public void setIncludeSubTypesForRelatedTypes(boolean includeSubTypesForRelatedTypes)
    {
        this.includeSubTypesForRelatedTypes = includeSubTypesForRelatedTypes;
    }


    public boolean isIncludeSubTypesForRelatedTypes()
    {
        return this.includeSubTypesForRelatedTypes;
    }
}
