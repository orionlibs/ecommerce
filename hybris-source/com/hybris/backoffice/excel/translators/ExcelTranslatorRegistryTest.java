package com.hybris.backoffice.excel.translators;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ExcelTranslatorRegistryTest
{
    @Test
    public void shouldReturnTrueIfAtLeastOneTranslatorIsAbleToHandleRequest()
    {
        ExcelValueTranslator translator = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelTranslatorRegistry registry = new ExcelTranslatorRegistry();
        AttributeDescriptorModel excelAttribute = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        registry.setTranslators(Arrays.asList(new ExcelValueTranslator[] {translator}));
        BDDMockito.given(Boolean.valueOf(translator.canHandle(excelAttribute))).willReturn(Boolean.valueOf(true));
        boolean canHandle = registry.canHandle(excelAttribute);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldReturnFalseIfNoTranslatorIsAbleToHandleRequest()
    {
        ExcelValueTranslator translator = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelTranslatorRegistry registry = new ExcelTranslatorRegistry();
        AttributeDescriptorModel excelAttribute = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        registry.setTranslators(Arrays.asList(new ExcelValueTranslator[] {translator}));
        BDDMockito.given(Boolean.valueOf(translator.canHandle(excelAttribute))).willReturn(Boolean.valueOf(false));
        boolean canHandle = registry.canHandle(excelAttribute);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldReturnFirstTranslatorWhichIsAbleToHandleRequest()
    {
        ExcelValueTranslator translator1 = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelValueTranslator translator2 = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelValueTranslator translator3 = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelTranslatorRegistry registry = new ExcelTranslatorRegistry();
        AttributeDescriptorModel excelAttribute = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        registry.setTranslators(Arrays.asList(new ExcelValueTranslator[] {translator1, translator2, translator3}));
        BDDMockito.given(Boolean.valueOf(translator2.canHandle(excelAttribute))).willReturn(Boolean.valueOf(true));
        Optional<ExcelValueTranslator<Object>> foundTranslator = registry.getTranslator(excelAttribute);
        Assertions.assertThat(foundTranslator).isPresent();
        Assertions.assertThat(foundTranslator.get()).isEqualTo(translator2);
    }


    @Test
    public void shouldReturnFirstTranslatorWhichIsAbleToHandleRequestAndIsNotExcluded()
    {
        ExcelValueTranslator translator1 = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelValueTranslator translator2 = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelValueTranslator translator3 = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        ExcelTranslatorRegistry registry = (ExcelTranslatorRegistry)Mockito.spy(ExcelTranslatorRegistry.class);
        AttributeDescriptorModel excelAttribute = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        List<ExcelValueTranslator> translators = new ArrayList();
        translators.add(translator1);
        translators.add(translator2);
        translators.add(translator3);
        registry.setTranslators(translators);
        ((ExcelTranslatorRegistry)Mockito.doReturn(ExcelJavaTypeTranslator.class).when(registry)).getTranslatorClass(translator1);
        ((ExcelTranslatorRegistry)Mockito.doReturn(ExcelEnumTypeTranslator.class).when(registry)).getTranslatorClass(translator2);
        ((ExcelTranslatorRegistry)Mockito.doReturn(ExcelEnumTypeTranslator.class).when(registry)).getTranslatorClass(translator3);
        BDDMockito.given(Boolean.valueOf(translator1.canHandle(excelAttribute))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(translator2.canHandle(excelAttribute))).willReturn(Boolean.valueOf(true));
        Optional<ExcelValueTranslator<Object>> foundTranslator = registry.getTranslator(excelAttribute, new Class[] {ExcelJavaTypeTranslator.class});
        Assertions.assertThat(foundTranslator).isPresent();
        Assertions.assertThat(foundTranslator.get()).isEqualTo(translator2);
    }
}
