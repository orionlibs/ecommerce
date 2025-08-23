package com.hybris.backoffice.excel.validators.engine;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.localized.LocalizedAttributeConstraint;
import de.hybris.platform.validation.localized.LocalizedConstraintsRegistry;
import de.hybris.platform.validation.services.ConstraintViolationFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.validation.ConstraintViolation;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelValidationEngineAwareLocalizedStrategyTest extends AbstractValidationEngineAwareStrategyTest
{
    @Mock
    private ConstraintViolationFactory violationFactory;
    @Mock
    private LocalizedConstraintsRegistry localizedConstraintsRegistry;
    @Mock
    private ExcelLocalizedConstraintsProvider excelLocalizedConstraintsProvider;
    @Spy
    @InjectMocks
    private ExcelValidationEngineAwareLocalizedStrategy strategy;


    @Before
    public void setup()
    {
        super.setup();
        Mockito.when(this.strategy.getConverterRegistry()).thenReturn(this.converterRegistry);
    }


    @Test
    public void shouldReturnValidationErrorWhenAttributeIsLocalizedAndSeveritiesAreEqualAndConstraintIsNotLocalized()
    {
        String validationMessage = "Value cannot be empty";
        LocalizedAttributeConstraint constraint = mockLocalizedConstraintsRegistry(Locale.ENGLISH);
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters("", Locale.ENGLISH.toString());
        HybrisConstraintViolation constraintViolation = prepareLocalizedConstraintViolation("Value cannot be empty", Severity.ERROR, Locale.ENGLISH);
        BDDMockito.given(excelAttribute.getQualifier()).willReturn("code");
        BDDMockito.given(Boolean.valueOf(constraint.matchesNonLocalizedViolation(constraintViolation))).willReturn(Boolean.FALSE);
        mockValidateValue("code", Sets.newHashSet((Object[])new HybrisConstraintViolation[] {constraintViolation}));
        this.strategy.setSeverities(Arrays.asList(new Severity[] {Severity.ERROR}));
        ExcelValidationResult validationResult = this.strategy.validate(importParameters, excelAttribute);
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("Value cannot be empty");
    }


    @Test
    public void shouldReturnValidationErrorWhenAttributeIsLocalizedAndSeveritiesAreEqualAndConstraintIsLocalizedForGivenLanguage()
    {
        String localizedValidationMessage = "Value cannot be empty in [en]";
        LocalizedAttributeConstraint constraint = mockLocalizedConstraintsRegistry(Locale.ENGLISH);
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters("", Locale.ENGLISH.toString());
        HybrisConstraintViolation constraintViolation = prepareLocalizedConstraintViolation("Value cannot be empty in [en]", Severity.ERROR, Locale.ENGLISH);
        ConstraintViolation constraintViolationOfValidationError = (ConstraintViolation)Mockito.mock(ConstraintViolation.class);
        BDDMockito.given(excelAttribute.getQualifier()).willReturn("code");
        BDDMockito.given(Boolean.valueOf(constraint.matchesNonLocalizedViolation(constraintViolation))).willReturn(Boolean.TRUE);
        BDDMockito.given(constraintViolation.getConstraintViolation()).willReturn(constraintViolationOfValidationError);
        BDDMockito.given(this.violationFactory.createLocalizedConstraintViolation((ConstraintViolation)Matchers.any(), (Locale)Matchers.eq(Locale.ENGLISH))).willReturn(constraintViolation);
        mockValidateValue("code", Sets.newHashSet((Object[])new HybrisConstraintViolation[] {constraintViolation}));
        this.strategy.setSeverities(Arrays.asList(new Severity[] {Severity.ERROR}));
        ExcelValidationResult validationResult = this.strategy.validate(importParameters, excelAttribute);
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("Value cannot be empty in [en]");
    }


    @Test
    public void shouldNotReturnValidationErrorWhenAttributeIsLocalizedAndSeveritiesAreEqualAndConstraintIsNotLocalizedForGivenLanguage()
    {
        String localizedValidationMessage = "Value cannot be empty in [en]";
        LocalizedAttributeConstraint constraint = mockLocalizedConstraintsRegistry(Locale.ENGLISH);
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters("", Locale.GERMANY.toString());
        HybrisConstraintViolation constraintViolation = prepareLocalizedConstraintViolation("Value cannot be empty in [en]", Severity.ERROR, Locale.ENGLISH);
        ConstraintViolation constraintViolationOfValidationError = (ConstraintViolation)Mockito.mock(ConstraintViolation.class);
        BDDMockito.given(excelAttribute.getQualifier()).willReturn("code");
        BDDMockito.given(Boolean.valueOf(constraint.matchesNonLocalizedViolation(constraintViolation))).willReturn(Boolean.TRUE);
        Mockito.lenient().when(constraintViolation.getConstraintViolation()).thenReturn(constraintViolationOfValidationError);
        Mockito.lenient().when(this.violationFactory.createLocalizedConstraintViolation((ConstraintViolation)Matchers.any(), (Locale)Matchers.eq(Locale.ENGLISH))).thenReturn(constraintViolation);
        mockValidateValue("code", Sets.newHashSet((Object[])new HybrisConstraintViolation[] {constraintViolation}));
        this.strategy.setSeverities(Arrays.asList(new Severity[] {Severity.ERROR}));
        ExcelValidationResult validationResult = this.strategy.validate(importParameters, excelAttribute);
        Assertions.assertThat(validationResult.getValidationErrors()).isEmpty();
    }


    private LocalizedAttributeConstraint mockLocalizedConstraintsRegistry(Locale locale)
    {
        LocalizedAttributeConstraint localizedConstraint = (LocalizedAttributeConstraint)Mockito.mock(LocalizedAttributeConstraint.class);
        BDDMockito.given(localizedConstraint.getLanguages()).willReturn(Arrays.asList(new Locale[] {locale}));
        BDDMockito.given(this.excelLocalizedConstraintsProvider.getLocalizedAttributeConstraints((Class)Matchers.any(), (List)Matchers.any()))
                        .willReturn(Arrays.asList(new LocalizedAttributeConstraint[] {localizedConstraint}));
        return localizedConstraint;
    }


    private ImportParameters prepareImportParameters(String cellValue, String isoCode)
    {
        return new ImportParameters("Product", isoCode, cellValue, null, Collections.emptyList());
    }
}
