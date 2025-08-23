package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

class AbstractExcelMapperTest
{
    AttributeDescriptorModel mockAttributeDescriptorUnique(boolean unique)
    {
        AttributeDescriptorModel uniqueAttributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(uniqueAttributeDescriptor.getUnique()).willReturn(Boolean.valueOf(unique));
        return uniqueAttributeDescriptor;
    }


    AttributeDescriptorModel mockAttributeDescriptorLocalized(boolean localized)
    {
        AttributeDescriptorModel uniqueAttributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(uniqueAttributeDescriptor.getLocalized()).willReturn(Boolean.valueOf(localized));
        return uniqueAttributeDescriptor;
    }


    ExcelFilter<AttributeDescriptorModel> getUniqueFilter()
    {
        return AttributeDescriptorModel::getUnique;
    }
}
