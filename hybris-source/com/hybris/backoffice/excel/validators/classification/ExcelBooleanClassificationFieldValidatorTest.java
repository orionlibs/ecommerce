package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelBooleanClassificationFieldValidatorTest
{
    private static final HashMap<String, Object> ANY_CONTEXT = new HashMap<>();
    private final ExcelBooleanClassificationFieldValidator validator = new ExcelBooleanClassificationFieldValidator();


    @Test
    public void shouldValidatorBeInvokedWhenInputIsOfTypeBoolean()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "some value", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertTrue(output);
    }


    @Test
    public void shouldValidatorNotBeHandledWhenInputIsRangeOfBooleans()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "RANGE[TRUE;FALSE]", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertFalse(output);
    }


    @Test
    public void shouldValidatorNotBeHandledWhenInputIsMultivaluedBoolean()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "TRUE,FALSE", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertFalse(output);
    }


    @Test
    public void shouldValidationSucceedWhenInputIsBoolean()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "TRUE", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        ExcelValidationResult result = this.validator.validate(mockExcelAttribute(), importParameters, ANY_CONTEXT);
        Assert.assertFalse(result.hasErrors());
    }


    @Test
    public void shouldValidationFailWhenInputIsNotBoolean()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "blabla", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        ExcelValidationResult result = this.validator.validate(mockExcelAttribute(), importParameters, ANY_CONTEXT);
        Assert.assertTrue(result.hasErrors());
    }


    private ExcelClassificationAttribute mockExcelAttribute()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel assignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.BOOLEAN);
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignmentModel);
        return attribute;
    }
}
