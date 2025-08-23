package de.hybris.platform.webservicescommons.mapping.mappers;

import de.hybris.platform.webservicescommons.mapping.FieldSelectionStrategy;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;

@WsDTOMapping
public class AbstractCustomMapper<A, B> extends CustomMapper<A, B>
{
    private FieldSelectionStrategy fieldSelectionStrategy;


    protected boolean shouldMap(Object source, Object dest, MappingContext mappingContext)
    {
        return this.fieldSelectionStrategy.shouldMap(source, dest, mappingContext);
    }


    @Required
    public void setFieldSelectionStrategy(FieldSelectionStrategy fieldSelectionStrategy)
    {
        this.fieldSelectionStrategy = fieldSelectionStrategy;
    }
}
