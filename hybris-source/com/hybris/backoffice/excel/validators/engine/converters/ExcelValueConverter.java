package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import org.springframework.core.Ordered;

public interface ExcelValueConverter<TYPE> extends Ordered
{
    boolean canConvert(ExcelAttribute paramExcelAttribute, ImportParameters paramImportParameters);


    TYPE convert(ExcelAttribute paramExcelAttribute, ImportParameters paramImportParameters);
}
