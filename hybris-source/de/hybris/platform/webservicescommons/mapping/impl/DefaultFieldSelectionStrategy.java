package de.hybris.platform.webservicescommons.mapping.impl;

import de.hybris.platform.webservicescommons.mapping.FieldSelectionStrategy;
import java.util.Set;
import ma.glasnost.orika.MappingContext;

public class DefaultFieldSelectionStrategy implements FieldSelectionStrategy
{
    public boolean shouldMap(Object source, Object dest, MappingContext mappingContext)
    {
        if(mappingContext.getProperty("MAP_NULLS") != null)
        {
            Boolean mapNullsProperty = (Boolean)mappingContext.getProperty("MAP_NULLS");
            boolean mapNulls = (mapNullsProperty != null && mapNullsProperty.booleanValue());
            if(!mapNulls && source == null)
            {
                return false;
            }
        }
        if(mappingContext.getProperty("FIELD_SET_NAME") != null)
        {
            Set<String> config = (Set<String>)mappingContext.getProperty("FIELD_SET_NAME");
            return config.contains(mappingContext.getFullyQualifiedDestinationPath());
        }
        return true;
    }
}
