package de.hybris.platform.cockpit.services;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class PropertyBasedService extends AbstractServiceImpl
{
    public Set<PropertyDescriptor> getAllSupportedPropertyDescriptors(Collection<ObjectType> rootTypes)
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        Collection<ObjectType> supertypes = new LinkedHashSet<>();
        for(ObjectType type : rootTypes)
        {
            ObjectType loadedType = type;
            if(type instanceof SearchType)
            {
                loadedType = getTypeService().getObjectType(type.getCode());
            }
            supertypes.addAll(loadedType.getSupertypes());
        }
        Collection<ObjectType> types, next;
        for(types = supertypes, next = new LinkedHashSet<>(); types != null && !types.isEmpty(); types = next, next = new LinkedHashSet<>())
        {
            for(ObjectType t : types)
            {
                SearchType searchType = null;
                if(t instanceof SearchType)
                {
                    searchType = (SearchType)t;
                }
                else
                {
                    searchType = UISessionUtils.getCurrentSession().getSearchService().getSearchType(t);
                }
                ret.addAll(searchType.getDeclaredPropertyDescriptors());
                next.addAll(t.getSupertypes());
            }
        }
        for(types = rootTypes, next = new LinkedHashSet<>(); types != null && !types.isEmpty(); types = next, next = new LinkedHashSet<>())
        {
            for(ObjectType t : types)
            {
                SearchType searchType = null;
                if(t instanceof SearchType)
                {
                    searchType = (SearchType)t;
                }
                else
                {
                    searchType = UISessionUtils.getCurrentSession().getSearchService().getSearchType(t);
                }
                ret.addAll(searchType.getDeclaredPropertyDescriptors());
                next.addAll(t.getSubtypes());
            }
        }
        return ret;
    }
}
