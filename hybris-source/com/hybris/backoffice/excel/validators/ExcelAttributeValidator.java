package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.Map;
import javax.annotation.Nonnull;

public interface ExcelAttributeValidator<T extends com.hybris.backoffice.excel.data.ExcelAttribute>
{
    boolean canHandle(@Nonnull T paramT, @Nonnull ImportParameters paramImportParameters);


    ExcelValidationResult validate(@Nonnull T paramT, @Nonnull ImportParameters paramImportParameters, @Nonnull Map<String, Object> paramMap);
}
