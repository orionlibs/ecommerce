package com.hybris.backoffice.excel.validators.engine;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class ExcelValidationEngineAwareUnlocalizedStrategy extends ExcelAbstractValidationEngineAwareStrategy
{
    public boolean canHandle(ImportParameters importParameters, ExcelAttribute excelAttribute)
    {
        return !excelAttribute.isLocalized();
    }


    public ExcelValidationResult validate(ImportParameters importParameters, ExcelAttribute excelAttribute)
    {
        List<ValidationMessage> mappedErrors = new ArrayList<>();
        Collection<HybrisConstraintViolation> validationErrors = validateValue(importParameters, excelAttribute);
        if(CollectionUtils.isNotEmpty(validationErrors))
        {
            for(HybrisConstraintViolation error : validationErrors)
            {
                mappedErrors.add(new ValidationMessage(error.getLocalizedMessage(), error.getViolationSeverity()));
            }
        }
        return mappedErrors.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(mappedErrors);
    }
}
