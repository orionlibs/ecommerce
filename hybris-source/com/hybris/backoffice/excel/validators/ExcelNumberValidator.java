package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExcelNumberValidator implements ExcelValidator
{
    public static final String VALIDATION_INCORRECTTYPE_NUMBER_MESSAGE_KEY = "excel.import.validation.incorrecttype.number";
    private TypeService typeService;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        boolean isCreatable = NumberUtils.isCreatable(importParameters.getCellValue().toString());
        return isCreatable ? ExcelValidationResult.SUCCESS :
                        new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.number", new Serializable[] {importParameters
                                        .getCellValue()}));
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() && this.typeService
                        .isAssignableFrom(Number.class.getCanonicalName(), attributeDescriptor.getAttributeType().getCode()));
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
