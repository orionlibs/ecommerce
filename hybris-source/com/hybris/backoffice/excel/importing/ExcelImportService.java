package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelImportService
{
    Impex convertToImpex(Workbook paramWorkbook);


    @Deprecated(since = "6.7", forRemoval = true)
    List<ExcelValidationResult> validate(Workbook paramWorkbook);


    @Deprecated(since = "2005", forRemoval = true)
    default List<ExcelValidationResult> validate(Workbook workbook, @Nullable Set<String> mediaContentEntries)
    {
        return validate(workbook);
    }


    default List<ExcelValidationResult> validate(Workbook workbook, @Nullable Set<String> mediaContentEntries, Map<String, Object> context)
    {
        return validate(workbook, new HashSet<>());
    }
}
