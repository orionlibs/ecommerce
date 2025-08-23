package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import javax.annotation.Nonnull;

public interface ClassificationAttributeHeaderValueCreator
{
    String create(@Nonnull ExcelClassificationAttribute paramExcelClassificationAttribute, @Nonnull ExcelImportContext paramExcelImportContext);
}
