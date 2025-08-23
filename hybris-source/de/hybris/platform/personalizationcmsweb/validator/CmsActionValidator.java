package de.hybris.platform.personalizationcmsweb.validator;

import de.hybris.platform.personalizationcmsweb.data.CxCmsActionData;
import de.hybris.platform.personalizationwebservices.validator.ActionValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class CmsActionValidator implements ActionValidator
{
    public boolean supports(Class<?> clazz)
    {
        return CxCmsActionData.class.isAssignableFrom(clazz);
    }


    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "componentId", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "containerId", "field.required");
    }
}
