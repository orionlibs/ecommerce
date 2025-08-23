package de.hybris.platform.webservicescommons.mapping.filters;

import de.hybris.platform.webservicescommons.mapping.FieldSelectionStrategy;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import ma.glasnost.orika.Filter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Property;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Required;

@WsDTOMapping
public class GeneralFieldFilter implements Filter<Object, Object>
{
    private FieldSelectionStrategy fieldSelectionStrategy;


    public boolean appliesTo(Property property, Property property2)
    {
        return true;
    }


    public <S, D> boolean shouldMap(Type<S> sType, String sourceName, S source, Type<D> dType, String destName, D dest, MappingContext mappingContext)
    {
        return this.fieldSelectionStrategy.shouldMap(source, dest, mappingContext);
    }


    public boolean filtersSource()
    {
        return false;
    }


    public boolean filtersDestination()
    {
        return false;
    }


    public <S> S filterSource(S s, Type<S> sType, String s2, Type<?> type, String s3, MappingContext mappingContext)
    {
        return null;
    }


    public <D> D filterDestination(D d, Type<?> type, String s, Type<D> dType, String s2, MappingContext mappingContext)
    {
        return null;
    }


    public Type<Object> getAType()
    {
        return null;
    }


    public Type<Object> getBType()
    {
        return null;
    }


    @Required
    public void setFieldSelectionStrategy(FieldSelectionStrategy fieldSelectionStrategy)
    {
        this.fieldSelectionStrategy = fieldSelectionStrategy;
    }
}
