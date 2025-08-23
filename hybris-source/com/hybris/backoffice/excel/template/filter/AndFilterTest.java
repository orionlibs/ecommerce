package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AndFilterTest
{
    private final AndFilter<AttributeDescriptorModel> andFilter = new AndFilter();
    @Mock
    private UniqueCheckingFilter uniqueCheckingFilter;
    @Mock
    private DefaultValueCheckingFilter defaultValueCheckingFilter;
    final AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);


    @Test
    public void shouldReturnTrueWhenAllSubFiltersReturnTrue()
    {
        BDDMockito.given(Boolean.valueOf(this.uniqueCheckingFilter.test(this.model))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.defaultValueCheckingFilter.test(this.model))).willReturn(Boolean.valueOf(true));
        this.andFilter.setFilters(Arrays.asList(new ExcelFilter[] {(ExcelFilter)this.uniqueCheckingFilter, (ExcelFilter)this.defaultValueCheckingFilter}));
        boolean result = this.andFilter.test(this.model);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldReturnFalseWhenNotAllSubFiltersReturnTrue()
    {
        BDDMockito.given(Boolean.valueOf(this.uniqueCheckingFilter.test(this.model))).willReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.defaultValueCheckingFilter.test(this.model))).thenReturn(Boolean.valueOf(true));
        this.andFilter.setFilters(Arrays.asList(new ExcelFilter[] {(ExcelFilter)this.uniqueCheckingFilter, (ExcelFilter)this.defaultValueCheckingFilter}));
        boolean result = this.andFilter.test(this.model);
        Assertions.assertThat(result).isFalse();
    }
}
