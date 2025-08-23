package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Collection;
import java.util.stream.Collectors;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Required;

public class FromAttributeDescriptorsToExcelAttributesMapper implements ToExcelAttributesMapper<Collection<AttributeDescriptorModel>, ExcelAttributeDescriptorAttribute>
{
    private CommonI18NService commonI18NService;
    private Collection<ExcelFilter<ExcelAttributeDescriptorAttribute>> filters;


    public Collection<ExcelAttributeDescriptorAttribute> apply(Collection<AttributeDescriptorModel> attributeDescriptorModels)
    {
        return (Collection<ExcelAttributeDescriptorAttribute>)attributeDescriptorModels.stream()
                        .map(this::getExcelAttributes)
                        .flatMap(Collection::stream)
                        .filter(attribute -> filter(attribute, this.filters))
                        .distinct()
                        .collect(Collectors.toSet());
    }


    protected Collection<ExcelAttributeDescriptorAttribute> getExcelAttributes(AttributeDescriptorModel attributeDescriptor)
    {
        return attributeDescriptor.getLocalized().booleanValue() ? getLocalizedExcelAttributes(attributeDescriptor) :
                        getUnlocalizedExcelAttributes(attributeDescriptor);
    }


    protected Collection<ExcelAttributeDescriptorAttribute> getLocalizedExcelAttributes(AttributeDescriptorModel attributeDescriptor)
    {
        return (Collection<ExcelAttributeDescriptorAttribute>)this.commonI18NService.getAllLanguages().stream().filter(C2LItemModel::getActive).map(C2LItemModel::getIsocode)
                        .map(isoCode -> new ExcelAttributeDescriptorAttribute(attributeDescriptor, isoCode)).collect(Collectors.toList());
    }


    protected Collection<ExcelAttributeDescriptorAttribute> getUnlocalizedExcelAttributes(AttributeDescriptorModel attributeDescriptor)
    {
        return Lists.newArrayList((Object[])new ExcelAttributeDescriptorAttribute[] {new ExcelAttributeDescriptorAttribute(attributeDescriptor)});
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public void setFilters(Collection<ExcelFilter<ExcelAttributeDescriptorAttribute>> filters)
    {
        this.filters = filters;
    }
}
