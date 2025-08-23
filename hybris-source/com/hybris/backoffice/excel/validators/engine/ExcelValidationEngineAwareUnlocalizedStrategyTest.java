package com.hybris.backoffice.excel.validators.engine;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import java.util.Arrays;
import java.util.Collections;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelValidationEngineAwareUnlocalizedStrategyTest extends AbstractValidationEngineAwareStrategyTest
{
    @Spy
    @InjectMocks
    private ExcelValidationEngineAwareUnlocalizedStrategy strategy;


    @Before
    public void setup()
    {
        super.setup();
        Mockito.when(this.strategy.getConverterRegistry()).thenReturn(this.converterRegistry);
    }


    @Test
    public void shouldReturnValidationErrorWhenSeveritiesAreEqual()
    {
        String validationMessage = "Value cannot be empty";
        this.strategy.setSeverities(Arrays.asList(new Severity[] {Severity.ERROR}));
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        BDDMockito.given(excelAttribute.getQualifier()).willReturn("code");
        ImportParameters importParameters = prepareImportParameters("");
        HybrisConstraintViolation constraintViolation = prepareConstraintViolation("Value cannot be empty", Severity.ERROR);
        mockValidateValue("code", Sets.newHashSet((Object[])new HybrisConstraintViolation[] {constraintViolation}));
        ExcelValidationResult validationResult = this.strategy.validate(importParameters, excelAttribute);
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("Value cannot be empty");
    }


    @Test
    public void shouldNotReturnValidationErrorWhenSeverityIsDifferent()
    {
        String validationMessage = "Value cannot be empty";
        this.strategy.setSeverities(Arrays.asList(new Severity[] {Severity.ERROR}));
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        BDDMockito.given(excelAttribute.getQualifier()).willReturn("code");
        ImportParameters importParameters = prepareImportParameters("");
        HybrisConstraintViolation constraintViolation = prepareConstraintViolation("Value cannot be empty", Severity.WARN);
        mockValidateValue("code", Sets.newHashSet((Object[])new HybrisConstraintViolation[] {constraintViolation}));
        ExcelValidationResult validationResult = this.strategy.validate(importParameters, excelAttribute);
        Assertions.assertThat(validationResult.getValidationErrors()).isEmpty();
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        return new ImportParameters("Product", null, cellValue, null, Collections.emptyList());
    }
}
