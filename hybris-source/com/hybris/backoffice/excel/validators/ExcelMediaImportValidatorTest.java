package com.hybris.backoffice.excel.validators;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.backoffice.BackofficeTestUtil;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaImportValidatorTest
{
    @InjectMocks
    private ExcelMediaImportValidator validator;
    @Mock
    private TypeService typeService;
    @Mock
    private ExcelImportService importService;


    @Before
    public void setUp()
    {
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).thenReturn(Boolean.valueOf(true));
    }


    @Test
    public void shouldHandleMediaType()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "theCode");
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockAttributeDescriptor("Media");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        Assertions.assertThat(this.validator.canHandle(importParameters, attrDesc)).isTrue();
    }


    @Test
    public void shouldNotHandleProductType()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "theCode");
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockAttributeDescriptor("Product");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        Assertions.assertThat(this.validator.canHandle(importParameters, attrDesc)).isFalse();
    }


    @Test
    public void shouldValidateCodeAndPathCannotBeBothEmpty()
    {
        Map<String, String> params = new HashMap<>();
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockAttributeDescriptor("Product");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        ExcelValidationResult validate = this.validator.validate(importParameters, attrDesc, new HashMap<>());
        Assertions.assertThat(validate.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validate.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.media.pathandcode.empty");
    }


    @Test
    public void shouldValidateZipExists()
    {
        Map<String, String> params = new HashMap<>();
        params.put("filePath", "d");
        HashMap<String, Object> context = new HashMap<>();
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockAttributeDescriptor("Product");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        ExcelValidationResult validate = this.validator.validate(importParameters, attrDesc, context);
        Assertions.assertThat(validate.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validate.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.media.content.missing.zip");
    }


    @Test
    public void shouldValidateFileExists()
    {
        Map<String, String> params = new HashMap<>();
        params.put("filePath", "d");
        HashMap<String, Object> context = new HashMap<>();
        context.put("mediaContentEntries", Sets.newHashSet((Object[])new String[] {"a,b,c"}));
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockAttributeDescriptor("Product");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        ExcelValidationResult validate = this.validator.validate(importParameters, attrDesc, context);
        Assertions.assertThat(validate.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validate.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.media.content.missing.entry");
    }
}
