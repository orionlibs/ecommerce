package de.hybris.platform.solrfacetsearch.config.mapping.converters;

import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.mapping.FacetSearchConfigMapping;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

@FacetSearchConfigMapping
public class ValueRangeConverter extends CustomConverter<ValueRange, ValueRange>
{
    public ValueRange convert(ValueRange source, Type<? extends ValueRange> destinationType, MappingContext mappingContext)
    {
        ValueRange target = new ValueRange();
        target.setName(source.getName());
        target.setFrom(source.getFrom());
        target.setTo(source.getTo());
        return target;
    }
}
