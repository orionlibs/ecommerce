package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.parser.RangeParserUtils;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class ExcelRangeClassificationFieldValidatorTest
{
    private static final HashMap<String, Object> ANY_CONTEXT = new HashMap<>();
    @Mock
    private ExcelNumberClassificationFieldValidator excelNumberClassificationFieldValidator;
    private final ExcelRangeClassificationFieldValidator validator = new ExcelRangeClassificationFieldValidator();


    @Before
    public void setUp()
    {
        this.validator.setValidators(Lists.newArrayList((Object[])new ExcelAttributeValidator[] {(ExcelAttributeValidator)this.excelNumberClassificationFieldValidator}));
    }


    @Test
    public void shouldValidatorBeInvokedWhenInputIsRange()
    {
        Map<String, String> param1 = new LinkedHashMap<>();
        param1.put(RangeParserUtils.prependFromPrefix("rawValue"), "2");
        Map<String, String> param2 = new LinkedHashMap<>();
        param2.put(RangeParserUtils.prependToPrefix("rawValue"), "3");
        List<Map<String, String>> params = Lists.newArrayList((Object[])new Map[] {param1, param2});
        ImportParameters importParameters = new ImportParameters(null, null, "RANGE[2;3]", null, params);
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertTrue(output);
    }


    @Test
    public void shouldValidatorNotBeHandledWhenInputIsMultivaluedRange()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "RANGE[2;3];RANGE[2;4]", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertFalse(output);
    }


    @Test
    public void shouldValidatorNotBeHandledWhenInputIsNotRange()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "XXX", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertFalse(output);
    }


    @Test
    public void shouldValidatorDelegateToOtherValidators()
    {
        Map<String, String> param1 = new LinkedHashMap<>();
        param1.put(RangeParserUtils.prependFromPrefix("rawValue"), "2");
        Map<String, String> param2 = new LinkedHashMap<>();
        param2.put(RangeParserUtils.prependToPrefix("rawValue"), "3");
        List<Map<String, String>> params = Lists.newArrayList((Object[])new Map[] {param1, param2});
        ImportParameters importParameters = new ImportParameters(null, null, "RANGE[2;3]", null, params);
        BDDMockito.given(Boolean.valueOf(this.excelNumberClassificationFieldValidator.canHandle((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any()))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(this.excelNumberClassificationFieldValidator.validate((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any())).willReturn(ExcelValidationResult.SUCCESS);
        this.validator.validate(mockExcelAttribute(), importParameters, ANY_CONTEXT);
        ((ExcelNumberClassificationFieldValidator)BDDMockito.then(this.excelNumberClassificationFieldValidator).should(Mockito.times(2))).validate((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any());
    }


    private ExcelClassificationAttribute mockExcelAttribute()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel assignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.NUMBER);
        BDDMockito.given(assignmentModel.getRange()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignmentModel);
        return attribute;
    }
}
