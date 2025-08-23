package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class ExcelBooleanValidator implements ExcelValidator
{
    public static final String VALIDATION_INCORRECTTYPE_BOOLEAN_MESSAGE_KEY = "excel.import.validation.incorrecttype.boolean";


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        boolean isBooleanValue = StringUtils.equalsAnyIgnoreCase(importParameters.getCellValue().toString(), new CharSequence[] {"true", "false"});
        return isBooleanValue ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.boolean", new Serializable[] {importParameters
                        .getCellValue()}));
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() &&
                        StringUtils.equals(attributeDescriptor.getAttributeType().getCode(), Boolean.class.getCanonicalName()));
    }
}
