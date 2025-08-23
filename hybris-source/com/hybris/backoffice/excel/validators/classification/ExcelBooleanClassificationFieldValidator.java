package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

public class ExcelBooleanClassificationFieldValidator extends AbstractSingleClassificationFieldValidator
{
    public static final String VALIDATION_INCORRECTTYPE_BOOLEAN_MESSAGE_KEY = "excel.import.validation.incorrecttype.boolean";


    public boolean canHandleSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (excelAttribute.getAttributeAssignment().getAttributeType() == ClassificationAttributeTypeEnum.BOOLEAN);
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        boolean isBooleanValue = StringUtils.equalsAnyIgnoreCase(importParameters.getCellValue().toString(), new CharSequence[] {"true", "false"});
        return isBooleanValue ? ExcelValidationResult.SUCCESS :
                        new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.boolean", new Serializable[] {importParameters
                                        .getCellValue()}));
    }
}
