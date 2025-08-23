package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class FromTypeCodeToAttributeDescriptorsMapper implements ToAttributeDescriptorsMapper<String>
{
    private ExcelMapper<ComposedTypeModel, AttributeDescriptorModel> mapper;
    private TypeService typeService;
    private Collection<ExcelFilter<AttributeDescriptorModel>> filters;


    public Collection<AttributeDescriptorModel> apply(String s)
    {
        return (Collection<AttributeDescriptorModel>)((Collection)this.mapper.apply(this.typeService.getComposedTypeForCode(s)))
                        .stream()
                        .filter(attribute -> filter(attribute, this.filters))
                        .collect(Collectors.toList());
    }


    @Required
    public void setMapper(ExcelMapper<ComposedTypeModel, AttributeDescriptorModel> mapper)
    {
        this.mapper = mapper;
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
