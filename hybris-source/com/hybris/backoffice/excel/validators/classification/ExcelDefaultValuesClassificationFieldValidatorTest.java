package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelDefaultValuesClassificationFieldValidatorTest
{
    private final ExcelDefaultValuesClassificationFieldValidator validator = new ExcelDefaultValuesClassificationFieldValidator();


    @Test
    public void shouldValidatorBeInvokedWhenImportParametersContainsFormatErrors()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        BDDMockito.given(Boolean.valueOf(importParameters.hasFormatErrors())).willReturn(Boolean.valueOf(true));
        boolean output = this.validator.canHandle((ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class), importParameters);
        Assert.assertTrue(output);
    }
}
