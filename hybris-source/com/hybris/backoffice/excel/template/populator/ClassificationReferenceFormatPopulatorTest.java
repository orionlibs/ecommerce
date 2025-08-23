package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslator;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslatorRegistry;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationReferenceFormatPopulatorTest
{
    @Mock
    private ExcelAttributeTranslatorRegistry registry;
    private ClassificationReferenceFormatPopulator populator = new ClassificationReferenceFormatPopulator();


    @Before
    public void setUp()
    {
        this.populator.setRegistry(this.registry);
    }


    @Test
    public void shouldCorrectReferenceFormatBeReturned()
    {
        ExcelAttributeTranslator<ExcelAttribute> translator = (ExcelAttributeTranslator<ExcelAttribute>)Mockito.mock(ExcelAttributeTranslator.class);
        String referenceFormat = "system:version";
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ExcelAttributeContext<ExcelClassificationAttribute> context = DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute);
        BDDMockito.given(translator.referenceFormat((ExcelAttribute)attribute)).willReturn("system:version");
        BDDMockito.given(this.registry.findTranslator((ExcelAttribute)attribute)).willReturn(Optional.of(translator));
        String populatedValue = this.populator.apply(context);
        Assertions.assertThat(populatedValue).isEqualTo("system:version");
    }
}
