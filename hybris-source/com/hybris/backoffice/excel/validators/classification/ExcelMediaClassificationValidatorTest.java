package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelSingleMediaValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
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
public class ExcelMediaClassificationValidatorTest
{
    @Mock
    private TypeService typeService;
    @Mock
    private ExcelSingleMediaValidator excelSingleMediaValidator;
    @InjectMocks
    ExcelMediaClassificationValidator excelMediaClassificationValidator;


    @Before
    public void setup()
    {
        this.excelMediaClassificationValidator.setSingleMediaValidators(Collections.singletonList(this.excelSingleMediaValidator));
    }


    @Test
    public void shouldHandleRequestWhenAttributeIsMedia()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ComposedTypeModel mediaComposedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(mediaComposedType.getCode()).willReturn("Media");
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getReferenceType()).willReturn(mediaComposedType);
        BDDMockito.given(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).willReturn(Boolean.valueOf(true));
        boolean result = this.excelMediaClassificationValidator.canHandleSingle(attribute, importParameters);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldInvokeAllMediaValidatorsAndCollectValidationErrors()
    {
        HashMap<String, Object> ctx = new HashMap<>();
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelSingleMediaValidator firstSingleMediaValidator = (ExcelSingleMediaValidator)Mockito.mock(ExcelSingleMediaValidator.class);
        ExcelSingleMediaValidator secondSingleMediaValidator = (ExcelSingleMediaValidator)Mockito.mock(ExcelSingleMediaValidator.class);
        Map<String, String> firstParams = new HashMap<>();
        Map<String, String> secondParams = new HashMap<>();
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ValidationMessage firstValidationMessage = new ValidationMessage("first");
        ValidationMessage secondValidationMessage = new ValidationMessage("second");
        ValidationMessage thirdValidationMessage = new ValidationMessage("third");
        this.excelMediaClassificationValidator.setSingleMediaValidators(Arrays.asList(new ExcelSingleMediaValidator[] {firstSingleMediaValidator, secondSingleMediaValidator}));
        BDDMockito.given(importParameters.getMultiValueParameters()).willReturn(Arrays.asList(new Map[] {firstParams, secondParams}));
        BDDMockito.given(firstSingleMediaValidator.validateSingleValue((Map)Matchers.eq(ctx), (Map)Matchers.same(firstParams))).willReturn(Collections.emptyList());
        BDDMockito.given(firstSingleMediaValidator.validateSingleValue((Map)Matchers.eq(ctx), (Map)Matchers.same(secondParams)))
                        .willReturn(Collections.singletonList(firstValidationMessage));
        BDDMockito.given(secondSingleMediaValidator.validateSingleValue((Map)Matchers.eq(ctx), (Map)Matchers.same(firstParams)))
                        .willReturn(Collections.singletonList(secondValidationMessage));
        BDDMockito.given(secondSingleMediaValidator.validateSingleValue((Map)Matchers.eq(ctx), (Map)Matchers.same(secondParams)))
                        .willReturn(Collections.singletonList(thirdValidationMessage));
        ExcelValidationResult validationResult = this.excelMediaClassificationValidator.validate(excelClassificationAttribute, importParameters, ctx);
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(3);
        Assertions.assertThat(validationResult.getValidationErrors()).containsExactlyInAnyOrder((Object[])new ValidationMessage[] {firstValidationMessage, secondValidationMessage, thirdValidationMessage});
    }
}
