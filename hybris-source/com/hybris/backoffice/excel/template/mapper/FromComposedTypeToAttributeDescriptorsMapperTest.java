package com.hybris.backoffice.excel.template.mapper;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
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
public class FromComposedTypeToAttributeDescriptorsMapperTest extends AbstractExcelMapperTest
{
    @Mock
    private TypeService typeService;
    private FromComposedTypeToAttributeDescriptorsMapper mapper = new FromComposedTypeToAttributeDescriptorsMapper();


    @Before
    public void setUp()
    {
        this.mapper.setTypeService(this.typeService);
    }


    @Test
    public void shouldFilterReturnedCollection()
    {
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        AttributeDescriptorModel uniqueAttributeDescriptor = mockAttributeDescriptorUnique(true);
        AttributeDescriptorModel nonUniqueAttributeDescriptor = mockAttributeDescriptorUnique(false);
        BDDMockito.given(this.typeService.getAttributeDescriptorsForType(composedTypeModel))
                        .willReturn(Sets.newHashSet((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor, nonUniqueAttributeDescriptor}));
        this.mapper.setFilters(Lists.newArrayList((Object[])new ExcelFilter[] {getUniqueFilter()}));
        Collection<AttributeDescriptorModel> returnedAttributeDescriptors = this.mapper.apply(composedTypeModel);
        Assertions.assertThat(returnedAttributeDescriptors.size()).isEqualTo(1);
        Assertions.assertThat(returnedAttributeDescriptors).containsOnly((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor});
        Assertions.assertThat(returnedAttributeDescriptors).doesNotContain((Object[])new AttributeDescriptorModel[] {nonUniqueAttributeDescriptor});
    }
}
