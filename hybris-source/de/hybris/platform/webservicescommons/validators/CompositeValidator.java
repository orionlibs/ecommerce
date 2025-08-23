package de.hybris.platform.webservicescommons.validators;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CompositeValidator implements Validator
{
    private Validator[] validators;


    public boolean supports(Class<?> clazz)
    {
        for(Validator v : this.validators)
        {
            if(v.supports(clazz))
            {
                return true;
            }
        }
        return false;
    }


    public void validate(Object object, Errors errors)
    {
        for(Validator v : this.validators)
        {
            if(v.supports(object.getClass()))
            {
                v.validate(object, errors);
            }
        }
    }


    public Validator[] getValidators()
    {
        return this.validators;
    }


    @Required
    public void setValidators(Validator[] validators)
    {
        this.validators = validators;
    }
}
