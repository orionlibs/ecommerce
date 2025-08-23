package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.math.NumberUtils;

public class ExcelNumberClassificationFieldValidator extends AbstractSingleClassificationFieldValidator
{
    public static final String VALIDATION_INCORRECTTYPE_NUMBER_MESSAGE_KEY = "excel.import.validation.incorrecttype.number";


    public boolean canHandleSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (excelAttribute.getAttributeAssignment().getAttributeType() == ClassificationAttributeTypeEnum.NUMBER);
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        boolean isCreatable = NumberUtils.isCreatable(importParameters.getCellValue().toString());
        return isCreatable ? ExcelValidationResult.SUCCESS :
                        new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.number", new Serializable[] {importParameters
                                        .getCellValue()}));
    }
}
