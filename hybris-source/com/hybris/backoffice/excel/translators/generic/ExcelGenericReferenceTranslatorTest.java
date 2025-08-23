package com.hybris.backoffice.excel.translators.generic;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelGenericReferenceTranslatorTest
{
    @InjectMocks
    private ExcelGenericReferenceTranslator excelGenericReferenceTranslator;


    @Test
    public void shouldUseDefaultOrderWhenAnotherOrderIsNotSet()
    {
        int order = this.excelGenericReferenceTranslator.getOrder();
        Assertions.assertThat(order).isEqualTo(2147483547);
    }


    @Test
    public void shouldOverrideDefaultOrder()
    {
        this.excelGenericReferenceTranslator.setOrder(100);
        int order = this.excelGenericReferenceTranslator.getOrder();
        Assertions.assertThat(order).isEqualTo(100);
    }
}
