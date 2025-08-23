package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Map;

public interface ExcelValidator
{
    public static final String CTX_MEDIA_CONTENT_ENTRIES = "mediaContentEntries";


    ExcelValidationResult validate(ImportParameters paramImportParameters, AttributeDescriptorModel paramAttributeDescriptorModel, Map<String, Object> paramMap);


    boolean canHandle(ImportParameters paramImportParameters, AttributeDescriptorModel paramAttributeDescriptorModel);
}
