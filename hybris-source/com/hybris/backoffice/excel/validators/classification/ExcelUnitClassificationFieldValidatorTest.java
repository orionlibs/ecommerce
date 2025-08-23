package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelUnitClassificationFieldValidatorTest
{
    private static final HashMap<String, Object> ANY_CONTEXT = new HashMap<>();
    @Mock
    ClassificationSystemService mockedClassificationSystemService;
    @InjectMocks
    ExcelUnitClassificationFieldValidator excelUnitClassificationFieldValidator;


    @Before
    public void before()
    {
        this.excelUnitClassificationFieldValidator.setValidators(Collections.emptyList());
    }


    @Test
    public void shouldHandleAttributesWithUnitAndSingleValue()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        Map<String, String> params = new HashMap<>();
        params.put("unit", "unit");
        BDDMockito.given(importParameters.getSingleValueParameters()).willReturn(params);
        boolean result = this.excelUnitClassificationFieldValidator.canHandle(excelAttribute, importParameters);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldNotHandleMultiValueAttributes()
    {
        ExcelClassificationAttribute excelAttributeWithUnit = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = prepareMultiValueImportParameters();
        boolean result = this.excelUnitClassificationFieldValidator.canHandle(excelAttributeWithUnit, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    private ImportParameters prepareMultiValueImportParameters()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        Mockito.lenient().when(importParameters.getCellValue()).thenReturn(",");
        return importParameters;
    }


    @Test
    public void shouldNotHandleRanges()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        Mockito.lenient().when(importParameters.getCellValue()).thenReturn("RANGE[from;to]");
        boolean result = this.excelUnitClassificationFieldValidator.canHandle(excelAttribute, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldNotHandleAttributesWithoutUnit()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ExcelClassificationAttribute excelAttributeWithoutUnit = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        Mockito.lenient().when(excelAttributeWithoutUnit.getAttributeAssignment()).thenReturn(classAttributeAssignment);
        boolean result = this.excelUnitClassificationFieldValidator.canHandle(excelAttributeWithoutUnit, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldCacheUnits()
    {
        String unitType = "unitType";
        ClassificationSystemVersionModel systemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationSystemModel classificationSystemModel = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        BDDMockito.given(systemVersionModel.getCatalog()).willReturn(classificationSystemModel);
        BDDMockito.given(classificationSystemModel.getId()).willReturn("catalogId");
        BDDMockito.given(systemVersionModel.getVersion()).willReturn("version");
        ClassificationAttributeUnitModel unitModel = (ClassificationAttributeUnitModel)Mockito.mock(ClassificationAttributeUnitModel.class);
        BDDMockito.given(unitModel.getUnitType()).willReturn("unitType");
        BDDMockito.given(unitModel.getCode()).willReturn("unitCode");
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignmentModel.getUnit()).willReturn(unitModel);
        BDDMockito.given(classAttributeAssignmentModel.getSystemVersion()).willReturn(systemVersionModel);
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        Map<String, Object> context = new HashMap<>();
        BDDMockito.given(this.mockedClassificationSystemService.getUnitsOfTypeForSystemVersion(systemVersionModel, "unitType"))
                        .willReturn(Collections.singletonList(unitModel));
        this.excelUnitClassificationFieldValidator.validate(excelAttribute, importParameters, context);
        Assertions.assertThat(context).containsOnly(new Map.Entry[] {(Map.Entry)Assertions.entry("PossibleUnitsOf:catalogId:version:unitType", Collections.singletonList("unitCode"))});
    }


    @Test
    public void shouldCacheUnitsWithNullUnitType()
    {
        ClassificationSystemVersionModel systemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationSystemModel classificationSystemModel = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        BDDMockito.given(systemVersionModel.getCatalog()).willReturn(classificationSystemModel);
        BDDMockito.given(classificationSystemModel.getId()).willReturn("catalogId");
        BDDMockito.given(systemVersionModel.getVersion()).willReturn("version");
        ClassificationAttributeUnitModel unitModel = (ClassificationAttributeUnitModel)Mockito.mock(ClassificationAttributeUnitModel.class);
        BDDMockito.given(unitModel.getUnitType()).willReturn(null);
        BDDMockito.given(unitModel.getCode()).willReturn("unitCode");
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignmentModel.getUnit()).willReturn(unitModel);
        BDDMockito.given(classAttributeAssignmentModel.getSystemVersion()).willReturn(systemVersionModel);
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        Map<String, Object> context = new HashMap<>();
        Mockito.lenient().when(this.mockedClassificationSystemService.getUnitsOfTypeForSystemVersion(systemVersionModel, null))
                        .thenReturn(Collections.singletonList(unitModel));
        this.excelUnitClassificationFieldValidator.validate(excelAttribute, importParameters, context);
        Assertions.assertThat(context).containsOnly(new Map.Entry[] {(Map.Entry)Assertions.entry("PossibleUnitsOf:catalogId:version:null", Collections.singletonList("unitCode"))});
        ((ClassificationSystemService)Mockito.verify(this.mockedClassificationSystemService, Mockito.never())).getUnitsOfTypeForSystemVersion((ClassificationSystemVersionModel)Matchers.any(), (String)Matchers.any());
    }


    @Test
    public void shouldExecuteValidators()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ImportParameters expectedPassedImportParameters = new ImportParameters(null, null, "someValue", null, new ArrayList());
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeUnitModel classificationAttributeUnitModel = (ClassificationAttributeUnitModel)Mockito.mock(ClassificationAttributeUnitModel.class);
        ExcelAttributeValidator<ExcelClassificationAttribute> validator = (ExcelAttributeValidator<ExcelClassificationAttribute>)Mockito.mock(ExcelAttributeValidator.class);
        ClassificationSystemVersionModel systemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationSystemModel classificationSystemModel = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        Map<String, String> params = new HashMap<>();
        params.put("value", "someValue");
        BDDMockito.given(importParameters.getSingleValueParameters()).willReturn(params);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getSystemVersion()).willReturn(systemVersionModel);
        BDDMockito.given(systemVersionModel.getCatalog()).willReturn(classificationSystemModel);
        BDDMockito.given(classificationSystemModel.getId()).willReturn("catalogId");
        BDDMockito.given(systemVersionModel.getVersion()).willReturn("version");
        BDDMockito.given(classAttributeAssignmentModel.getUnit()).willReturn(classificationAttributeUnitModel);
        BDDMockito.given(classificationAttributeUnitModel.getUnitType()).willReturn("unitType");
        BDDMockito.given(Boolean.valueOf(validator.canHandle((ExcelAttribute)excelAttribute, expectedPassedImportParameters))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(validator.validate((ExcelAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any())).willReturn(ExcelValidationResult.SUCCESS);
        this.excelUnitClassificationFieldValidator.setValidators(Collections.singletonList(validator));
        this.excelUnitClassificationFieldValidator.validate(excelAttribute, importParameters, ANY_CONTEXT);
        ((ExcelAttributeValidator)BDDMockito.then(validator).should()).validate((ExcelAttribute)excelAttribute, expectedPassedImportParameters, ANY_CONTEXT);
    }


    @Test
    public void shouldAggregateValidatorsMessages()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationSystemVersionModel systemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationSystemModel classificationSystemModel = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        ClassificationAttributeUnitModel classificationAttributeUnitModel = (ClassificationAttributeUnitModel)Mockito.mock(ClassificationAttributeUnitModel.class);
        ExcelAttributeValidator<ExcelClassificationAttribute> firstValidator = (ExcelAttributeValidator<ExcelClassificationAttribute>)Mockito.mock(ExcelAttributeValidator.class);
        ExcelAttributeValidator<ExcelClassificationAttribute> secondValidator = (ExcelAttributeValidator<ExcelClassificationAttribute>)Mockito.mock(ExcelAttributeValidator.class);
        ValidationMessage firstErrorMessage = new ValidationMessage("firstMessageKey");
        ValidationMessage secondErrorMessage = new ValidationMessage("secondMessageKey");
        ValidationMessage thirdErrorMessage = new ValidationMessage("excel.import.validation.unit.invalid", new Serializable[] {"someUnit", "unitType"});
        Map<String, String> params = new HashMap<>();
        params.put("value", "someValue");
        params.put("unit", "someUnit");
        BDDMockito.given(importParameters.getSingleValueParameters()).willReturn(params);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getSystemVersion()).willReturn(systemVersionModel);
        BDDMockito.given(systemVersionModel.getCatalog()).willReturn(classificationSystemModel);
        BDDMockito.given(classificationSystemModel.getId()).willReturn("catalogId");
        BDDMockito.given(systemVersionModel.getVersion()).willReturn("version");
        BDDMockito.given(classAttributeAssignmentModel.getUnit()).willReturn(classificationAttributeUnitModel);
        BDDMockito.given(classificationAttributeUnitModel.getUnitType()).willReturn("unitType");
        BDDMockito.given(Boolean.valueOf(firstValidator.canHandle((ExcelAttribute)Matchers.any(), (ImportParameters)Matchers.any()))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(secondValidator.canHandle((ExcelAttribute)Matchers.any(), (ImportParameters)Matchers.any()))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(firstValidator.validate((ExcelAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any())).willReturn(new ExcelValidationResult(firstErrorMessage));
        BDDMockito.given(secondValidator.validate((ExcelAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any())).willReturn(new ExcelValidationResult(secondErrorMessage));
        this.excelUnitClassificationFieldValidator.setValidators(Arrays.asList(new ExcelAttributeValidator[] {firstValidator, secondValidator}));
        ExcelValidationResult result = this.excelUnitClassificationFieldValidator.validate(excelAttribute, importParameters, ANY_CONTEXT);
        Assertions.assertThat((Comparable)result).isNotNull();
        Assertions.assertThat(result.hasErrors()).isTrue();
        Assertions.assertThat(result.getValidationErrors()).contains((Object[])new ValidationMessage[] {firstErrorMessage, secondErrorMessage, thirdErrorMessage});
    }
}
