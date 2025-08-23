package de.hybris.platform.cms2.version.converter.customattribute.impl;

import de.hybris.platform.cms2.version.converter.customattribute.CustomAttributeContentConverter;
import de.hybris.platform.cms2.version.converter.customattribute.CustomAttributeStrategyConverterProvider;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCustomAttributeStrategyConverterProvider implements CustomAttributeStrategyConverterProvider
{
    private List<CustomAttributeContentConverter> converters;


    public List<CustomAttributeContentConverter> getConverters(ItemModel itemModel)
    {
        return (List<CustomAttributeContentConverter>)getConverters().stream().filter(converter -> converter.getConstrainedBy().test(itemModel)).collect(Collectors.toList());
    }


    protected List<CustomAttributeContentConverter> getConverters()
    {
        return this.converters;
    }


    @Required
    public void setConverters(List<CustomAttributeContentConverter> converters)
    {
        this.converters = converters;
    }
}
