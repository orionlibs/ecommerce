package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class WritableCheckingFilterTest
{
    private final WritableCheckingFilter filter = new WritableCheckingFilter();


    @Test
    public void shouldFilterOutNotWritableAttributes()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getWritable()).willReturn(Boolean.valueOf(false));
        boolean result = this.filter.test(attributeDescriptorModel);
        Assertions.assertThat(result).isFalse();
    }
}
