package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NegateFilterTest
{
    private NegateFilter negateFilter = new NegateFilter();
    private UniqueCheckingFilter uniqueCheckingFilter = new UniqueCheckingFilter();


    @Test
    public void shouldNegateResult()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(model.getUnique()).willReturn(Boolean.valueOf(true));
        this.negateFilter.setExcelFilter((ExcelFilter)this.uniqueCheckingFilter);
        boolean uniqueResult = this.uniqueCheckingFilter.test(model);
        boolean negateResult = this.negateFilter.test(model);
        Assert.assertFalse(negateResult);
        Assert.assertTrue(uniqueResult);
    }
}
