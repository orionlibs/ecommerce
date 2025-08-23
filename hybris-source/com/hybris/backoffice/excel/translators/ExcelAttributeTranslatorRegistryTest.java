package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.translators.classification.ExcelClassificationJavaTypeTranslator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ExcelAttributeTranslatorRegistryTest
{
    @Test
    public void shouldReturnTrueIfAtLeastOneTranslatorIsAbleToHandleRequest()
    {
        ExcelAttributeTranslator translator = (ExcelAttributeTranslator)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslatorRegistry registry = new ExcelAttributeTranslatorRegistry();
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        registry.setTranslators(Arrays.asList(new ExcelAttributeTranslator[] {translator}));
        BDDMockito.given(Boolean.valueOf(translator.canHandle(excelAttribute))).willReturn(Boolean.valueOf(true));
        boolean canHandle = registry.canHandle(excelAttribute);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldReturnFalseIfNoTranslatorIsAbleToHandleRequest()
    {
        ExcelAttributeTranslator translator = (ExcelAttributeTranslator)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslatorRegistry registry = new ExcelAttributeTranslatorRegistry();
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        registry.setTranslators(Arrays.asList(new ExcelAttributeTranslator[] {translator}));
        BDDMockito.given(Boolean.valueOf(translator.canHandle(excelAttribute))).willReturn(Boolean.valueOf(false));
        boolean canHandle = registry.canHandle(excelAttribute);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldReturnFirstTranslatorWhichIsAbleToHandleRequest()
    {
        ExcelAttributeTranslator translator1 = (ExcelAttributeTranslator)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslator translator2 = (ExcelAttributeTranslator)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslator translator3 = (ExcelAttributeTranslator)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslatorRegistry registry = new ExcelAttributeTranslatorRegistry();
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        registry.setTranslators(Arrays.asList(new ExcelAttributeTranslator[] {translator1, translator2, translator3}));
        BDDMockito.given(Boolean.valueOf(translator2.canHandle(excelAttribute))).willReturn(Boolean.valueOf(true));
        Optional<ExcelAttributeTranslator<ExcelAttribute>> foundTranslator = registry.findTranslator(excelAttribute);
        Assertions.assertThat(foundTranslator).isPresent();
        Assertions.assertThat(foundTranslator.get()).isEqualTo(translator2);
    }


    @Test
    public void shouldReturnFirstTranslatorWhichIsAbleToHandleRequestAndIsNotExcluded()
    {
        ExcelAttributeTranslator<ExcelAttribute> translator1 = (ExcelAttributeTranslator<ExcelAttribute>)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslator<ExcelAttribute> translator2 = (ExcelAttributeTranslator<ExcelAttribute>)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslator<ExcelAttribute> translator3 = (ExcelAttributeTranslator<ExcelAttribute>)Mockito.mock(ExcelAttributeTranslator.class);
        ExcelAttributeTranslatorRegistry registry = (ExcelAttributeTranslatorRegistry)Mockito.spy(ExcelAttributeTranslatorRegistry.class);
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        List<ExcelAttributeTranslator<ExcelAttribute>> translators = new ArrayList();
        translators.add(translator1);
        translators.add(translator2);
        translators.add(translator3);
        registry.setTranslators(translators);
        ((ExcelAttributeTranslatorRegistry)Mockito.doReturn(ExcelClassificationJavaTypeTranslator.class).when(registry)).getTranslatorClass(translator1);
        ((ExcelAttributeTranslatorRegistry)Mockito.doReturn(ExcelAttributeTranslator.class).when(registry)).getTranslatorClass(translator2);
        ((ExcelAttributeTranslatorRegistry)Mockito.doReturn(ExcelAttributeTranslator.class).when(registry)).getTranslatorClass(translator3);
        BDDMockito.given(Boolean.valueOf(translator1.canHandle((ExcelAttribute)excelAttribute))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(translator2.canHandle((ExcelAttribute)excelAttribute))).willReturn(Boolean.valueOf(true));
        Optional<ExcelAttributeTranslator<ExcelAttribute>> foundTranslator = registry.findTranslator((ExcelAttribute)excelAttribute, new Class[] {ExcelClassificationJavaTypeTranslator.class});
        Assertions.assertThat(foundTranslator).isPresent();
        Assertions.assertThat(foundTranslator.get()).isEqualTo(translator2);
    }
}
