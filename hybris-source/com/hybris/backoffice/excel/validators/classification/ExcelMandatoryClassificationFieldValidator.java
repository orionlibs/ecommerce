package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Map;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class ExcelMandatoryClassificationFieldValidator implements ExcelAttributeValidator<ExcelClassificationAttribute>
{
    private CommonI18NService commonI18NService;


    public boolean canHandle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        if(!excelAttribute.isMandatory())
        {
            return false;
        }
        if(excelAttribute.isLocalized())
        {
            return this.commonI18NService.getCurrentLanguage().getIsocode().equals(excelAttribute.getIsoCode());
        }
        return true;
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        if(importParameters.isCellValueBlank())
        {
            return new ExcelValidationResult(new ValidationMessage("excel.import.validation.mandatory.classification.field"));
        }
        return ExcelValidationResult.SUCCESS;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
