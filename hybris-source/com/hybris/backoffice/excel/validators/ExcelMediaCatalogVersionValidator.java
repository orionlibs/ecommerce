package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExcelMediaCatalogVersionValidator extends ExcelCatalogVersionValidator implements ExcelSingleMediaValidator
{
    public Collection<ValidationMessage> validateSingleValue(Map<String, Object> ctx, Map<String, String> parameters)
    {
        populateContextIfNeeded(ctx);
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validateSingleReference(ctx, validationMessages, parameters);
        return validationMessages;
    }
}
