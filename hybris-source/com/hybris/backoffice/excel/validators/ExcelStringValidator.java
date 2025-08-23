package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class ExcelStringValidator implements ExcelValidator
{
    public static final String VALIDATION_INCORRECTTYPE_STRING_MESSAGE_KEY = "excel.import.validation.incorrecttype.string";


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        boolean canBeCasted = importParameters.getCellValue() instanceof String;
        return canBeCasted ? ExcelValidationResult.SUCCESS :
                        new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.string", new Serializable[] {importParameters
                                        .getCellValue()}));
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() && (
                        StringUtils.equals(attributeDescriptor.getAttributeType().getCode(), String.class.getCanonicalName()) || attributeDescriptor
                                        .getAttributeType() instanceof de.hybris.platform.core.model.type.MapTypeModel));
    }
}
