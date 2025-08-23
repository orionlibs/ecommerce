package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

public interface WorkbookValidator
{
    List<ExcelValidationResult> validate(Workbook paramWorkbook);
}
