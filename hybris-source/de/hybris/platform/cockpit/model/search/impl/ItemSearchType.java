package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.model.search.Facet;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemSearchType extends ItemType implements SearchType
{
    private Set<Facet> facets = Collections.emptySet();
    private List<PropertyDescriptor> sortProperties = Collections.emptyList();


    public ItemSearchType(ComposedTypeModel composedType)
    {
        super(composedType);
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


    public Set<Facet> getFacets()
    {
        return Collections.unmodifiableSet(this.facets);
    }


    public List<PropertyDescriptor> getSortProperties()
    {
        return Collections.unmodifiableList(this.sortProperties);
    }


    public void setFacets(Set<Facet> facets)
    {
        this.facets = facets;
    }


    public void setSortProperties(List<PropertyDescriptor> sortProperties)
    {
        this.sortProperties = sortProperties;
    }
}
