package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import javax.annotation.Nonnull;

public abstract class AbstractSingleClassificationFieldValidator implements ExcelAttributeValidator<ExcelClassificationAttribute>
{
    public boolean canHandle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (importParameters.isCellValueNotBlank() && isSimpleSingleValue(importParameters) &&
                        canHandleSingle(excelAttribute, importParameters));
    }


    private static boolean isSimpleSingleValue(@Nonnull ImportParameters importParameters)
    {
        return (ExcelValidatorUtils.isNotRange(importParameters) && !ExcelValidatorUtils.hasUnit(importParameters) &&
                        ExcelValidatorUtils.isNotMultivalue(importParameters));
    }


    public abstract boolean canHandleSingle(@Nonnull ExcelClassificationAttribute paramExcelClassificationAttribute, @Nonnull ImportParameters paramImportParameters);
}
