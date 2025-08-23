package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collections;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UniqueCheckingFilterTest
{
    private UniqueCheckingFilter filter = new UniqueCheckingFilter();


    @Test
    public void shouldReturnFalseWhenAttributeDescriptorIsNotUniqueAndItsNotReturnedByComposedType()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(composedType.getUniqueKeyAttributes()).willReturn(Collections.emptyList());
        BDDMockito.given(model.getEnclosingType()).willReturn(composedType);
        BDDMockito.given(model.getUnique()).willReturn(Boolean.valueOf(false));
        boolean result = this.filter.test(model);
        Assert.assertFalse(result);
    }


    @Test
    public void shouldReturnTrueWhenAttributeDescriptorIsUnique()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(model.getUnique()).willReturn(Boolean.valueOf(true));
        boolean result = this.filter.test(model);
        Assert.assertTrue(result);
    }


    @Test
    public void shouldReturnTrueWhenAttributeDescriptorIsReturnedByComposedType()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(composedType.getUniqueKeyAttributes()).willReturn(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {model}));
        BDDMockito.given(model.getEnclosingType()).willReturn(composedType);
        BDDMockito.given(model.getUnique()).willReturn(Boolean.valueOf(false));
        boolean result = this.filter.test(model);
        Assert.assertTrue(result);
    }
}
