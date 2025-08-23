package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChainMapperTest extends AbstractExcelMapperTest
{
    @Mock
    private ExcelMapper<String, AttributeDescriptorModel> mapper1;
    @Mock
    private ExcelMapper<Collection<AttributeDescriptorModel>, ExcelAttributeDescriptorAttribute> mapper2;
    private ChainMapper<String, ExcelAttributeDescriptorAttribute> chainMapper = new ChainMapper();


    @Before
    public void setUp()
    {
        this.chainMapper.setMapper1(this.mapper1);
        this.chainMapper.setMapper2(this.mapper2);
    }


    @Test
    public void shouldReturnCollectionOfExcelAttributes()
    {
        String input = "Product";
        Collection<AttributeDescriptorModel> attributeDescriptors = Lists.newArrayList((Object[])new AttributeDescriptorModel[] {(AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class),
                        (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class)});
        BDDMockito.given(this.mapper1.apply("Product")).willReturn(attributeDescriptors);
        ExcelAttributeDescriptorAttribute excelAttribute1 = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        ExcelAttributeDescriptorAttribute excelAttribute2 = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        BDDMockito.given(this.mapper2.apply(attributeDescriptors)).willReturn(Lists.newArrayList((Object[])new ExcelAttributeDescriptorAttribute[] {excelAttribute1, excelAttribute2}));
        Collection<ExcelAttributeDescriptorAttribute> returnedAttributes = this.chainMapper.apply("Product");
        Assertions.assertThat(returnedAttributes.size()).isEqualTo(2);
        Assertions.assertThat(returnedAttributes).containsOnly((Object[])new ExcelAttributeDescriptorAttribute[] {excelAttribute1, excelAttribute2});
    }


    @Test
    public void shouldMapper1BeFilteredByFilters1()
    {
        String input = "Product";
        AttributeDescriptorModel uniqueAttributeDescriptor = mockAttributeDescriptorUnique(true);
        AttributeDescriptorModel nonUniqueAttributeDescriptor = mockAttributeDescriptorUnique(false);
        Collection<AttributeDescriptorModel> attributeDescriptors = Lists.newArrayList((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor, nonUniqueAttributeDescriptor});
        BDDMockito.given(this.mapper1.apply("Product")).willReturn(attributeDescriptors);
        this.chainMapper.setFilters1(Lists.newArrayList((Object[])new ExcelFilter[] {getUniqueFilter()}));
        ExcelAttributeDescriptorAttribute excelAttribute1 = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        ExcelAttributeDescriptorAttribute excelAttribute2 = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        BDDMockito.given(this.mapper2.apply(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor}))).willReturn(Lists.newArrayList((Object[])new ExcelAttributeDescriptorAttribute[] {excelAttribute1, excelAttribute2}));
        Collection<ExcelAttributeDescriptorAttribute> returnedAttributes = this.chainMapper.apply("Product");
        Assertions.assertThat(returnedAttributes.size()).isEqualTo(2);
        Assertions.assertThat(returnedAttributes).containsOnly((Object[])new ExcelAttributeDescriptorAttribute[] {excelAttribute1, excelAttribute2});
    }


    @Test
    public void shouldMapper2BeFilteredByFilters2()
    {
        String input = "Product";
        Collection<AttributeDescriptorModel> attributeDescriptors = Lists.newArrayList((Object[])new AttributeDescriptorModel[] {(AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class),
                        (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class)});
        BDDMockito.given(this.mapper1.apply("Product")).willReturn(attributeDescriptors);
        String name1 = "name1";
        String name2 = "name2";
        ExcelAttributeDescriptorAttribute excelAttribute1 = mockExcelAttribute("name1");
        ExcelAttributeDescriptorAttribute excelAttribute2 = mockExcelAttribute("name2");
        BDDMockito.given(this.mapper2.apply(attributeDescriptors)).willReturn(Lists.newArrayList((Object[])new ExcelAttributeDescriptorAttribute[] {excelAttribute1, excelAttribute2}));
        ExcelFilter<ExcelAttributeDescriptorAttribute> excelFilter = attr -> StringUtils.equals(attr.getName(), "name2");
        this.chainMapper.setFilters2(Lists.newArrayList((Object[])new ExcelFilter[] {excelFilter}));
        Collection<ExcelAttributeDescriptorAttribute> returnedAttributes = this.chainMapper.apply("Product");
        Assertions.assertThat(returnedAttributes.size()).isEqualTo(1);
        Assertions.assertThat(returnedAttributes).containsOnly((Object[])new ExcelAttributeDescriptorAttribute[] {excelAttribute2});
    }


    protected ExcelAttributeDescriptorAttribute mockExcelAttribute(String name)
    {
        ExcelAttributeDescriptorAttribute excelAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        BDDMockito.given(excelAttribute.getName()).willReturn(name);
        return excelAttribute;
    }
}
