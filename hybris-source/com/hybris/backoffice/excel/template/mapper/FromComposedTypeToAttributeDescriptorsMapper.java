package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class FromComposedTypeToAttributeDescriptorsMapper implements ToAttributeDescriptorsMapper<ComposedTypeModel>
{
    private Collection<ExcelFilter<AttributeDescriptorModel>> filters;
    private TypeService typeService;


    public Collection<AttributeDescriptorModel> apply(ComposedTypeModel composedTypeModel)
    {
        return (Collection<AttributeDescriptorModel>)this.typeService.getAttributeDescriptorsForType(composedTypeModel)
                        .stream()
                        .filter(attribute -> filter(attribute, this.filters))
                        .collect(Collectors.toSet());
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setFilters(Collection<ExcelFilter<AttributeDescriptorModel>> filters)
    {
        this.filters = filters;
    }
}
