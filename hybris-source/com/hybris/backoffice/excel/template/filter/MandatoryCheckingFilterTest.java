package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MandatoryCheckingFilterTest
{
    private MandatoryCheckingFilter filter = new MandatoryCheckingFilter();


    @Test
    public void shouldReturnFalseWhenAttributeDescriptorIsOptional()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(model.getOptional()).willReturn(Boolean.valueOf(true));
        Mockito.lenient().when(model.getPrivate()).thenReturn(Boolean.valueOf(false));
        boolean result = this.filter.test(model);
        Assert.assertFalse(result);
    }


    @Test
    public void shouldReturnTrueWhenAttributeDescriptorIsNotOptional()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(model.getOptional()).willReturn(Boolean.valueOf(false));
        BDDMockito.given(model.getPrivate()).willReturn(Boolean.valueOf(false));
        boolean result = this.filter.test(model);
        Assert.assertTrue(result);
    }
}
