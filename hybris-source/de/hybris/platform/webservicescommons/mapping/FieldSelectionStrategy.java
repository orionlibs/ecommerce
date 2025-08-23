package de.hybris.platform.webservicescommons.mapping;

import ma.glasnost.orika.MappingContext;

public interface FieldSelectionStrategy
{
    boolean shouldMap(Object paramObject1, Object paramObject2, MappingContext paramMappingContext);
}
