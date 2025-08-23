package de.hybris.platform.cockpit.model.search;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.List;
import java.util.Set;

public interface SearchType extends ObjectType
{
    List<PropertyDescriptor> getSortProperties();


    Set<Facet> getFacets();
}
