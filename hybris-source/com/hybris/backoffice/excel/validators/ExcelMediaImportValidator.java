package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExcelMediaImportValidator implements ExcelSingleMediaValidator
{
    public static final String VALIDATION_DECLARED_MULTIPLE_REFERENCES = "excel.import.validation.media.multiple.declared";
    public static final String VALIDATION_PATH_AND_CODE_EMPTY = "excel.import.validation.media.pathandcode.empty";
    public static final String VALIDATION_MISSING_FILE_IN_ZIP = "excel.import.validation.media.content.missing.entry";
    public static final String VALIDATION_MISSING_ZIP = "excel.import.validation.media.content.missing.zip";
    protected TypeService typeService;
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelImportService importService;


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (hasImportData(importParameters) &&
                        getTypeService().isAssignableFrom(attributeDescriptor.getAttributeType().getCode(), "Media"));
    }


    protected boolean hasImportData(ImportParameters importParameters)
    {
        return (importParameters.isCellValueNotBlank() || importParameters.getMultiValueParameters().stream()
                        .anyMatch(singleParams -> !StringUtils.isAllBlank(new CharSequence[] {(CharSequence)singleParams.get("code"), (CharSequence)singleParams.get("filePath")})));
    }


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        if(importParameters.getMultiValueParameters().size() > 1)
        {
            return new ExcelValidationResult(new ValidationMessage("excel.import.validation.media.multiple.declared"));
        }
        List<ValidationMessage> validationMessages = validateSingleValue(context, importParameters
                        .getSingleValueParameters());
        return validationMessages.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(validationMessages);
    }


    public List<ValidationMessage> validateSingleValue(Map<String, Object> context, Map<String, String> parameters)
    {
        List<ValidationMessage> validations = new ArrayList<>();
        String filePath = parameters.get("filePath");
        String code = parameters.get("code");
        if(StringUtils.isAllBlank(new CharSequence[] {filePath, code}))
        {
            validations.add(new ValidationMessage("excel.import.validation.media.pathandcode.empty", new Serializable[] {"filePath", "code"}));
        }
        if(StringUtils.isNotBlank(filePath))
        {
            Set<String> zipEntries = (Set<String>)context.get("mediaContentEntries");
            if(zipEntries == null)
            {
                validations.add(new ValidationMessage("excel.import.validation.media.content.missing.zip", new Serializable[] {"filePath", filePath}));
            }
            else if(!zipEntries.contains(filePath))
            {
                validations.add(new ValidationMessage("excel.import.validation.media.content.missing.entry", new Serializable[] {filePath}));
            }
        }
        return validations;
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


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelImportService getImportService()
    {
        return this.importService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setImportService(ExcelImportService importService)
    {
        this.importService = importService;
    }
}
