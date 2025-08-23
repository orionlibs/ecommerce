package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import java.util.Collection;
import java.util.Map;

public interface ExcelSingleMediaValidator extends ExcelValidator
{
    Collection<ValidationMessage> validateSingleValue(Map<String, Object> paramMap, Map<String, String> paramMap1);
}
