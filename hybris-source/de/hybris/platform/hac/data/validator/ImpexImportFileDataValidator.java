package de.hybris.platform.hac.data.validator;

import de.hybris.platform.hac.data.form.ImpexFileFormData;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ImpexImportFileDataValidator implements Validator
{
    public boolean supports(Class<?> clazz)
    {
        return ImpexFileFormData.class.isAssignableFrom(clazz);
    }


    public void validate(Object target, Errors errors)
    {
        ImpexFileFormData dataIn = (ImpexFileFormData)target;
        if(dataIn.getFile() != null && dataIn.getFile().isEmpty())
        {
            errors.rejectValue("file", "file", "Import file is required");
        }
        if(dataIn.getValidationEnum() != ImpExValidationModeEnum.IMPORT_RELAXED && dataIn
                        .getValidationEnum() != ImpExValidationModeEnum.IMPORT_STRICT)
        {
            errors.rejectValue("validationEnum", "validationEnum", "Validation mode should be RELAXED or STRICT selected for import mode");
        }
        if(dataIn.getMaxThreads() != null && dataIn.getMaxThreads().intValue() <= 0)
        {
            errors.rejectValue("maxThreads", "maxThreads", "Max threads value should be equal or grater than 1");
        }
        if(GenericValidator.isBlankOrNull(dataIn.getEncoding()))
        {
            errors.rejectValue("encoding", "encoding", "Encoding is required");
        }
    }
}
