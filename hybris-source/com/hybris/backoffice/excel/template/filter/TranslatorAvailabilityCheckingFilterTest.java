package com.hybris.backoffice.excel.template.filter;

import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.translators.ExcelValueTranslator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Optional;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TranslatorAvailabilityCheckingFilterTest
{
    @Mock
    ExcelTranslatorRegistry mockedExcelTranslatorRegistry;
    @InjectMocks
    TranslatorAvailabilityCheckingFilter filter;


    @Test
    public void shouldFilterOutAttributesNotPresentInTranslatorRegistry()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(this.mockedExcelTranslatorRegistry.getTranslator(attributeDescriptorModel)).willReturn(Optional.empty());
        boolean result = this.filter.test(attributeDescriptorModel);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldNotFilterOutAttributesPresentInTranslatorRegistry()
    {
        ExcelValueTranslator excelValueTranslator = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(this.mockedExcelTranslatorRegistry.getTranslator(attributeDescriptorModel)).willReturn(Optional.of(excelValueTranslator));
        boolean result = this.filter.test(attributeDescriptorModel);
        Assertions.assertThat(result).isTrue();
    }
}
