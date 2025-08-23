package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultValueCheckingFilterTest
{
    private final DefaultValueCheckingFilter filter = new DefaultValueCheckingFilter();


    @Test
    public void shouldReturnFalseWhenAttributeHasNoDefaultValue()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getDefaultValue()).willReturn(null);
        boolean result = this.filter.test(attributeDescriptorModel);
        Assert.assertFalse(result);
    }


    @Test
    public void shouldReturnTrueWhenAttributeHasDefaultValue()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getDefaultValue()).willReturn(new Object());
        boolean result = this.filter.test(attributeDescriptorModel);
        Assert.assertTrue(result);
    }
}
