package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMultivalueClassificationFieldValidatorTest
{
    private static final HashMap<String, Object> ANY_CONTEXT = new HashMap<>();
    @Mock
    private ExcelBooleanClassificationFieldValidator excelBooleanClassificationFieldValidator;
    private final ExcelMultivalueClassificationFieldValidator validator = new ExcelMultivalueClassificationFieldValidator();


    @Before
    public void setUp()
    {
        this.validator.setValidators(Lists.newArrayList((Object[])new ExcelAttributeValidator[] {(ExcelAttributeValidator)this.excelBooleanClassificationFieldValidator}));
    }


    @Test
    public void shouldValidatorBeInvokedWhenInputIsMultivalued()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "X,X", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertTrue(output);
    }


    @Test
    public void shouldValidatorNotBeHandledWhenInputIsNotMultivalued()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "X", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertFalse(output);
    }


    @Test
    public void shouldValidatorDelegateToOtherValidators()
    {
        Map<String, String> param = new HashMap<>();
        param.put("rawValue", "TRUE");
        List<Map<String, String>> params = Lists.newArrayList((Object[])new Map[] {param, param, param});
        ImportParameters importParameters = new ImportParameters(null, null, "TRUE,FALSE,TRUE", null, params);
        BDDMockito.given(Boolean.valueOf(this.excelBooleanClassificationFieldValidator.canHandle((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any()))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(this.excelBooleanClassificationFieldValidator.validate((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any())).willReturn(ExcelValidationResult.SUCCESS);
        this.validator.validate(mockExcelAttribute(), importParameters, ANY_CONTEXT);
        ((ExcelBooleanClassificationFieldValidator)BDDMockito.then(this.excelBooleanClassificationFieldValidator).should(Mockito.times(3))).validate((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any());
    }


    private ExcelClassificationAttribute mockExcelAttribute()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel assignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        Mockito.lenient().when(assignmentModel.getAttributeType()).thenReturn(ClassificationAttributeTypeEnum.BOOLEAN);
        BDDMockito.given(assignmentModel.getMultiValued()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignmentModel);
        return attribute;
    }
}
