package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class ChainMapper<INPUT, ATTRIBUTE extends ExcelAttribute> implements ExcelMapper<INPUT, ATTRIBUTE>
{
    private ExcelMapper<INPUT, AttributeDescriptorModel> mapper1;
    private ExcelMapper<Collection<AttributeDescriptorModel>, ATTRIBUTE> mapper2;
    private Collection<ExcelFilter<AttributeDescriptorModel>> filters1;
    private Collection<ExcelFilter<ATTRIBUTE>> filters2;


    public Collection<ATTRIBUTE> apply(INPUT input)
    {
        Collection<AttributeDescriptorModel> result1 = (Collection<AttributeDescriptorModel>)((Collection)this.mapper1.apply(input)).stream().filter(this::filter1).collect(Collectors.toList());
        return (Collection<ATTRIBUTE>)((Collection)this.mapper2.apply(result1)).stream().filter(this::filter2).collect(Collectors.toList());
    }


    protected boolean filter1(AttributeDescriptorModel attributeDescriptor)
    {
        return filter(this.filters1, attributeDescriptor);
    }


    protected boolean filter2(ATTRIBUTE excelAttribute)
    {
        return filter(this.filters2, excelAttribute);
    }


    private static <T> boolean filter(Collection<ExcelFilter<T>> filters, T t)
    {
        return CollectionUtils.emptyIfNull(filters).stream().allMatch(filter -> filter.test(t));
    }


    @Required
    public void setMapper1(ExcelMapper<INPUT, AttributeDescriptorModel> mapper1)
    {
        this.mapper1 = mapper1;
    }


    @Required
    public void setMapper2(ExcelMapper<Collection<AttributeDescriptorModel>, ATTRIBUTE> mapper2)
    {
        this.mapper2 = mapper2;
    }


    public void setFilters1(Collection<ExcelFilter<AttributeDescriptorModel>> filters1)
    {
        this.filters1 = filters1;
    }


    public void setFilters2(Collection<ExcelFilter<ATTRIBUTE>> filters2)
    {
        this.filters2 = filters2;
    }
}
