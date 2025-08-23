package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class ExcelEnumValidator implements ExcelValidator
{
    public static final String VALIDATION_INCORRECTTYPE_ENUMVALUE_MESSAGE_KEY = "excel.import.validation.incorrecttype.enumvalue";
    public static final String VALIDATION_INCORRECTTYPE_ENUM_MESSAGE_KEY = "excel.import.validation.incorrecttype.enum";
    private EnumerationService enumerationService;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        String enumCode = attributeDescriptor.getAttributeType().getCode();
        try
        {
            List<HybrisEnumValue> enumValues = getEnumerationService().getEnumerationValues(enumCode);
            boolean isCorrect = enumValues.stream().anyMatch(enumValue -> enumValue.getCode().equals(importParameters.getCellValue()));
            return isCorrect ? ExcelValidationResult.SUCCESS :
                            new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.enumvalue", new Serializable[] {importParameters
                                            .getCellValue(), enumCode}));
        }
        catch(UnknownIdentifierException e)
        {
            return new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.enum", new Serializable[] {importParameters
                            .getCellValue(), enumCode}));
        }
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() && attributeDescriptor.getAttributeType() instanceof de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel);
    }


    public EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }
}
