package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.ArrayList;
import java.util.HashMap;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ExcelMandatoryClassificationFieldValidatorTest
{
    private static final HashMap<String, Object> VALIDATION_CONTEXT = new HashMap<>();
    private final ExcelMandatoryClassificationFieldValidator validator = new ExcelMandatoryClassificationFieldValidator();


    @Test
    public void shouldHandleAttributeWhenAttributeIsMandatory()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = prepareImportParameters("");
        BDDMockito.given(Boolean.valueOf(attribute.isMandatory())).willReturn(Boolean.valueOf(true));
        boolean canHandle = this.validator.canHandle(attribute, importParameters);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleAttributeWhenAttributeIsNotMandatory()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = prepareImportParameters("");
        BDDMockito.given(Boolean.valueOf(attribute.isMandatory())).willReturn(Boolean.valueOf(false));
        boolean canHandle = this.validator.canHandle(attribute, importParameters);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldReturnValidationErrorWhenCellValueIsNull()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = prepareImportParameters(null);
        BDDMockito.given(Boolean.valueOf(attribute.isMandatory())).willReturn(Boolean.valueOf(true));
        ExcelValidationResult validationResult = this.validator.validate(attribute, importParameters, VALIDATION_CONTEXT);
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
    }


    @Test
    public void shouldReturnValidationErrorWhenCellValueIsBlank()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = prepareImportParameters("   ");
        BDDMockito.given(Boolean.valueOf(attribute.isMandatory())).willReturn(Boolean.valueOf(true));
        ExcelValidationResult validationResult = this.validator.validate(attribute, importParameters, VALIDATION_CONTEXT);
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
    }


    @Test
    public void shouldNotReturnValidationErrorWhenCellValueIsNotBlank()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = prepareImportParameters("  abc ");
        BDDMockito.given(Boolean.valueOf(attribute.isMandatory())).willReturn(Boolean.valueOf(true));
        ExcelValidationResult validationResult = this.validator.validate(attribute, importParameters, VALIDATION_CONTEXT);
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(0);
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        return new ImportParameters("", null, cellValue, null, new ArrayList());
    }
}
