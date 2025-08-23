package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.core.Ordered;

public interface ExcelAttributeTranslator<T extends ExcelAttribute> extends Ordered
{
    boolean canHandle(@Nonnull T paramT);


    Optional<String> exportData(@Nonnull T paramT, @Nonnull Object paramObject);


    default String referenceFormat(@Nonnull T excelAttribute)
    {
        return "";
    }


    Impex importData(ExcelAttribute paramExcelAttribute, ImportParameters paramImportParameters, ExcelImportContext paramExcelImportContext);
}
