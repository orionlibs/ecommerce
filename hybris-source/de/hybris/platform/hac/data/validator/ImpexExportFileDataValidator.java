package de.hybris.platform.hac.data.validator;

import de.hybris.platform.hac.data.form.ImpexFileFormData;
import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ImpexExportFileDataValidator implements Validator
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
        if(GenericValidator.isBlankOrNull(dataIn.getEncoding()))
        {
            errors.rejectValue("encoding", "encoding", "Encoding is required");
        }
    }
}
