package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrFilterTest
{
    private final OrFilter orFilter = new OrFilter();
    private final UniqueCheckingFilter uniqueCheckingFilter = new UniqueCheckingFilter();
    private final DefaultValueCheckingFilter defaultValueCheckingFilter = new DefaultValueCheckingFilter();


    @Before
    public void setUp()
    {
        this.orFilter.setExcelFilter1((ExcelFilter)this.uniqueCheckingFilter);
        this.orFilter.setExcelFilter2((ExcelFilter)this.defaultValueCheckingFilter);
    }


    @Test
    public void shouldResultBeOrOfGivenFilters()
    {
        AttributeDescriptorModel model = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(model.getUnique()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(model.getDefaultValue()).willReturn(null);
        boolean uniqueResult = this.uniqueCheckingFilter.test(model);
        boolean defaultValueResult = this.defaultValueCheckingFilter.test(model);
        boolean orFilterResult = this.orFilter.test(model);
        Assert.assertTrue(uniqueResult);
        Assert.assertFalse(defaultValueResult);
        Assert.assertTrue(orFilterResult);
    }
}
