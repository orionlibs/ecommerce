package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelImportWorkbookValidationAwareDecorator extends ExcelImportWorkbookDecorator
{
    @Deprecated(since = "1905", forRemoval = true)
    Collection<ExcelValidationResult> validate(Workbook paramWorkbook);


    @Deprecated(since = "2005", forRemoval = true)
    default Collection<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries)
    {
        return validate(workbook);
    }


    default Collection<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries, Map<String, Object> context)
    {
        return validate(workbook);
    }
}
