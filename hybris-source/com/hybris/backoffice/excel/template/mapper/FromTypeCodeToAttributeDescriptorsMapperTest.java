package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import org.assertj.core.api.Java6Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FromTypeCodeToAttributeDescriptorsMapperTest extends AbstractExcelMapperTest
{
    @Mock
    private ExcelMapper<ComposedTypeModel, AttributeDescriptorModel> helperMapper;
    @Mock
    private TypeService typeService;
    private FromTypeCodeToAttributeDescriptorsMapper mapper = new FromTypeCodeToAttributeDescriptorsMapper();


    @Before
    public void setUp()
    {
        this.mapper.setTypeService(this.typeService);
        this.mapper.setMapper(this.helperMapper);
    }


    @Test
    public void shouldReturnCollectionOfAttributeDescriptors()
    {
        String typeCode = "Product";
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Collection<AttributeDescriptorModel> givenAttributeDescriptors = Lists.newArrayList((Object[])new AttributeDescriptorModel[] {(AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class), (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class)});
        BDDMockito.given(this.typeService.getComposedTypeForCode("Product")).willReturn(composedTypeModel);
        BDDMockito.given(this.helperMapper.apply(composedTypeModel)).willReturn(givenAttributeDescriptors);
        Collection<AttributeDescriptorModel> returnedAttributeDescriptors = this.mapper.apply("Product");
        Java6Assertions.assertThat(returnedAttributeDescriptors).isNotNull();
        Java6Assertions.assertThat(returnedAttributeDescriptors).isEqualTo(givenAttributeDescriptors);
    }


    @Test
    public void shouldResultBeFiltered()
    {
        String typeCode = "Product";
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        AttributeDescriptorModel uniqueAttributeDescriptor = mockAttributeDescriptorUnique(true);
        AttributeDescriptorModel nonUniqueAttributeDescriptor = mockAttributeDescriptorUnique(false);
        Collection<AttributeDescriptorModel> givenAttributeDescriptors = Lists.newArrayList((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor, nonUniqueAttributeDescriptor});
        BDDMockito.given(this.typeService.getComposedTypeForCode("Product")).willReturn(composedTypeModel);
        BDDMockito.given(this.helperMapper.apply(composedTypeModel)).willReturn(givenAttributeDescriptors);
        this.mapper.setFilters(Lists.newArrayList((Object[])new ExcelFilter[] {getUniqueFilter()}));
        Collection<AttributeDescriptorModel> returnedAttributeDescriptors = this.mapper.apply("Product");
        Java6Assertions.assertThat(returnedAttributeDescriptors.size()).isEqualTo(1);
        Java6Assertions.assertThat(returnedAttributeDescriptors).containsOnly((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor});
        Java6Assertions.assertThat(returnedAttributeDescriptors).doesNotContain((Object[])new AttributeDescriptorModel[] {nonUniqueAttributeDescriptor});
    }
}
