package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.DefaultObjectType;
import de.hybris.platform.cockpit.model.search.Facet;
import de.hybris.platform.cockpit.model.search.SearchType;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DefaultSearchType extends DefaultObjectType implements SearchType
{
    private Set<Facet> facets = Collections.emptySet();
    private List<PropertyDescriptor> sortProperties = Collections.emptyList();


    public DefaultSearchType(String code)
    {
        super(code);
    }


    public void setDeclaredPropertyDescriptors(Set<PropertyDescriptor> propertyDescriptors)
    {
        if(propertyDescriptors != null)
        {
            for(PropertyDescriptor pd : propertyDescriptors)
            {
                if(!(pd instanceof de.hybris.platform.cockpit.model.search.SearchParameterDescriptor))
                {
                    throw new IllegalArgumentException("invalid search parameter descriptor " + pd + " - must be instance of SearchParameterDescriptor");
                }
            }
        }
        super.setDeclaredPropertyDescriptors(propertyDescriptors);
    }


    public List<PropertyDescriptor> getSortProperties()
    {
        return Collections.unmodifiableList(this.sortProperties);
    }


    public Set<Facet> getFacets()
    {
        return Collections.unmodifiableSet(this.facets);
    }


    public void setFacets(Set<Facet> facets)
    {
        this.facets = facets;
    }


    public void setSortProperties(List<PropertyDescriptor> sortDescriptors)
    {
        this.sortProperties = sortDescriptors;
    }


    public boolean isAbstract()
    {
        return false;
    }


    public String getName()
    {
        return null;
    }


    public String getName(String languageIsoCode)
    {
        return null;
    }


    public String getDescription()
    {
        return null;
    }


    public String getDescription(String languageIsoCode)
    {
        return null;
    }
}
