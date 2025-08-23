package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.parser.RangeParserUtils;
import org.apache.commons.lang3.StringUtils;

class ExcelValidatorUtils
{
    static boolean isMultivalue(ImportParameters importParameters)
    {
        return StringUtils.contains(String.valueOf(importParameters.getCellValue()), ",");
    }


    static boolean isNotRange(ImportParameters importParameters)
    {
        return !RangeParserUtils.RANGE_PATTERN.matcher(String.valueOf(importParameters.getCellValue())).matches();
    }


    static boolean isNotMultivalue(ImportParameters importParameters)
    {
        return !isMultivalue(importParameters);
    }


    static boolean hasUnit(ImportParameters importParameters)
    {
        return importParameters.getSingleValueParameters().containsKey("unit");
    }
}
