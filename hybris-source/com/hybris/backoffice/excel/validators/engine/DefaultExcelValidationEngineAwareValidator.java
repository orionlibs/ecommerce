package com.hybris.backoffice.excel.validators.engine;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelValidationEngineAwareValidator implements ExcelValidationEngineAwareValidator
{
    private Collection<ExcelValidationEngineAwareStrategy> strategies;


    public ExcelValidationResult validate(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        Optional<ExcelValidationEngineAwareStrategy> foundStrategy = this.strategies.stream().filter(strategy -> strategy.canHandle(importParameters, excelAttribute)).findFirst();
        if(foundStrategy.isPresent())
        {
            return ((ExcelValidationEngineAwareStrategy)foundStrategy.get()).validate(importParameters, excelAttribute);
        }
        return ExcelValidationResult.SUCCESS;
    }


    public Collection<ExcelValidationEngineAwareStrategy> getStrategies()
    {
        return this.strategies;
    }


    @Required
    public void setStrategies(Collection<ExcelValidationEngineAwareStrategy> strategies)
    {
        this.strategies = strategies;
    }
}
