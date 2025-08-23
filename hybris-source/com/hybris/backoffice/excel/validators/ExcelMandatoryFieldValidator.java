package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Map;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExcelMandatoryFieldValidator implements ExcelValidator
{
    public static final String VALIDATION_MANDATORY_FIELD_MESSAGE_KEY = "excel.import.validation.mandatory.field";
    private CommonI18NService commonI18NService;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        boolean hasValue = (importParameters.getCellValue() != null && StringUtils.isNotBlank(importParameters.getCellValue().toString()));
        return hasValue ? ExcelValidationResult.SUCCESS :
                        new ExcelValidationResult(new ValidationMessage("excel.import.validation.mandatory.field"));
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        if(BooleanUtils.isTrue(attributeDescriptor.getOptional()))
        {
            return false;
        }
        return (BooleanUtils.isFalse(attributeDescriptor.getLocalized()) || this.commonI18NService
                        .getCurrentLanguage().getIsocode().equals(importParameters.getIsoCode()));
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
