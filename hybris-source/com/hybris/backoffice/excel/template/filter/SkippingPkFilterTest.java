package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SkippingPkFilterTest
{
    private SkippingPkFilter filter = new SkippingPkFilter();


    @Test
    public void shouldReturnFalseWhenAttributeDescriptorIsPK()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(model.getQualifier()).willReturn("pk");
        boolean result = this.filter.test(model);
        Assert.assertFalse(result);
    }


    @Test
    public void shouldReturnTrueWhenAttributeDescriptorIsNotPK()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(model.getQualifier()).willReturn("comments");
        boolean result = this.filter.test(model);
        Assert.assertTrue(result);
    }
}
