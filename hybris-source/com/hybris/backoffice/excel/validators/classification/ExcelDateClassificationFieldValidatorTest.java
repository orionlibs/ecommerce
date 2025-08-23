package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelDateClassificationFieldValidatorTest
{
    private static final HashMap<String, Object> ANY_CONTEXT = new HashMap<>();
    @Mock
    private ExcelDateUtils excelDateUtils;
    @InjectMocks
    private final ExcelDateClassificationFieldValidator validator = new ExcelDateClassificationFieldValidator();


    @Test
    public void shouldValidatorBeInvokedWhenInputIsOfTypeDate()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "some value", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertTrue(output);
    }


    @Test
    public void shouldValidatorNotBeHandledWhenInputIsRangeOfBooleans()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "RANGE[20.05.2018 08:53:31;21.05.2018 08:53:31]", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertFalse(output);
    }


    @Test
    public void shouldValidatorNotBeHandledWhenInputIsMultivaluedBoolean()
    {
        ImportParameters importParameters = new ImportParameters(null, null, "20.05.2018 08:53:31,21.05.2018 08:53:31", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        boolean output = this.validator.canHandle(mockExcelAttribute(), importParameters);
        Assert.assertFalse(output);
    }


    @Test
    public void shouldValidationSucceedWhenInputIsDate()
    {
        String cellValue = "20.05.2018 08:53:31";
        BDDMockito.given(this.excelDateUtils.importDate("20.05.2018 08:53:31")).willReturn("20.05.2018 08:53:31");
        ImportParameters importParameters = new ImportParameters(null, null, "20.05.2018 08:53:31", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        ExcelValidationResult result = this.validator.validate(mockExcelAttribute(), importParameters, ANY_CONTEXT);
        Assert.assertFalse(result.hasErrors());
    }


    @Test
    public void shouldValidationFailWhenInputIsNotDate()
    {
        String cellValue = "2.05.18 0:3:31";
        BDDMockito.given(this.excelDateUtils.importDate("2.05.18 0:3:31")).willThrow(new Throwable[] {new DateTimeParseException("any", "any", 1)});
        ImportParameters importParameters = new ImportParameters(null, null, "2.05.18 0:3:31", null, Lists.newArrayList((Object[])new Map[] {new HashMap<>()}));
        ExcelValidationResult result = this.validator.validate(mockExcelAttribute(), importParameters, ANY_CONTEXT);
        Assert.assertTrue(result.hasErrors());
    }


    private ExcelClassificationAttribute mockExcelAttribute()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel assignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.DATE);
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignmentModel);
        return attribute;
    }
}
