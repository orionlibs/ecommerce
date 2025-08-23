package de.hybris.platform.personalizationservices.occ.validators;

import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonalizationIdValidator implements Validator
{
    protected static final String INVALID_PERSONALIZATION_ID = "personalizationId.isInvalid";
    protected static final String INVALID_PERSONALIZATION_ID_MESSAGE = "PersonalizationId is invalid";
    protected Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");


    public boolean supports(Class<?> clazz)
    {
        return String.class.equals(clazz);
    }


    public void validate(Object target, Errors errors)
    {
        String personalizationId = (String)target;
        if(StringUtils.isEmpty(personalizationId) || !isValidUUID(personalizationId))
        {
            errors.reject("personalizationId.isInvalid", "PersonalizationId is invalid");
        }
    }


    protected boolean isValidUUID(String uuid)
    {
        return this.pattern.matcher(uuid).matches();
    }
}
