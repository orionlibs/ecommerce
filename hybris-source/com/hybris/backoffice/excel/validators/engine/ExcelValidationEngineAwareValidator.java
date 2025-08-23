package com.hybris.backoffice.excel.validators.engine;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;

public interface ExcelValidationEngineAwareValidator
{
    ExcelValidationResult validate(ExcelAttribute paramExcelAttribute, ImportParameters paramImportParameters);
}
