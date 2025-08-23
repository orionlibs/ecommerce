package com.hybris.backoffice.excel.template;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DisplayNameAttributeNameFormatterTest
{
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private TypeService typeService;
    @Spy
    private DisplayNameAttributeNameFormatter formatter = new DisplayNameAttributeNameFormatter();


    @Before
    public void setUp()
    {
        this.formatter.setCommonI18NService(this.commonI18NService);
        this.formatter.setTypeService(this.typeService);
    }


    @Test
    public void shouldReturnCorrectNameForLocalizedAttribute()
    {
        String attributeName = "Description";
        String isoCode = "pl";
        AttributeDescriptorModel attributeDescriptorModel = mockAttributeDescriptor(false, false, "Description", "pl");
        ExcelAttributeDescriptorAttribute attribute = new ExcelAttributeDescriptorAttribute(attributeDescriptorModel, "pl");
        String returnedName = this.formatter.format(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        AssertionsForClassTypes.assertThat(String.format("%s[%s]", new Object[] {"Description", "pl"})).isEqualTo(returnedName);
    }


    @Test
    public void shouldDisplayNameBeBasedOnQualifierWhenAttributesNameIsNull()
    {
        String attributeName = "Order";
        AttributeDescriptorModel attributeDescriptorModel = mockAttributeDescriptor(false, false, "Order", null);
        Mockito.lenient().when(attributeDescriptorModel.getQualifier()).thenReturn("Order");
        ExcelAttributeDescriptorAttribute attribute = new ExcelAttributeDescriptorAttribute(attributeDescriptorModel);
        String returnedName = this.formatter.format(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        AssertionsForClassTypes.assertThat("Order").isEqualTo(returnedName);
    }


    private AttributeDescriptorModel mockAttributeDescriptor(boolean mandatory, boolean unique, String name, String isoCode)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(attributeDescriptor.getName()).willReturn(name);
        BDDMockito.given(attributeDescriptor.getEnclosingType()).willReturn(composedTypeModel);
        BDDMockito.given(this.typeService.getAttributeDescriptorsForType(composedTypeModel)).willReturn(Sets.newHashSet((Object[])new AttributeDescriptorModel[] {attributeDescriptor}));
        BDDMockito.given(attributeDescriptor.getLocalized()).willReturn(Boolean.valueOf((isoCode != null)));
        Mockito.lenient().when(attributeDescriptor.getUnique()).thenReturn(Boolean.valueOf(unique));
        Mockito.lenient().when(attributeDescriptor.getOptional()).thenReturn(Boolean.valueOf(!mandatory));
        return attributeDescriptor;
    }
}
