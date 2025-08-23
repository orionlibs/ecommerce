package de.hybris.platform.hac.data.validator;

import de.hybris.platform.hac.data.form.ImpexContentFormData;
import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ImpexContentDataValidator implements Validator
{
    public boolean supports(Class<?> clazz)
    {
        return ImpexContentFormData.class.isAssignableFrom(clazz);
    }


    public void validate(Object target, Errors errors)
    {
        ImpexContentFormData dataIn = (ImpexContentFormData)target;
        if(GenericValidator.isBlankOrNull(dataIn.getScriptContent()))
        {
            errors.rejectValue("scriptContent", "scriptContent", "Script content should not be empty");
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
