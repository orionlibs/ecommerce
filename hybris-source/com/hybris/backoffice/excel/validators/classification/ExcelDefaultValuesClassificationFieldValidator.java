package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nonnull;

public class ExcelDefaultValuesClassificationFieldValidator implements ExcelAttributeValidator<ExcelClassificationAttribute>
{
    private static final String EXCEL_IMPORT_VALIDATION_INCORRECTFORMAT = "excel.import.validation.incorrectformat";


    public boolean canHandle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return importParameters.hasFormatErrors();
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        return new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrectformat", new Serializable[] {importParameters
                        .getCellValue(), importParameters.getFormatError()}));
    }
}
