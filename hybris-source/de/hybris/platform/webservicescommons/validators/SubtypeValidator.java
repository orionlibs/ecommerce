package de.hybris.platform.webservicescommons.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SubtypeValidator implements Validator
{
    protected Class<?> markerInterface;
    protected Collection<Validator> validators = new ArrayList<>();


    @PostConstruct
    public void init()
    {
        this.validators = findValidators(this.validators);
    }


    protected Collection<Validator> findValidators(Collection<Validator> validators)
    {
        return (Collection<Validator>)validators.stream().filter(v -> this.markerInterface.isAssignableFrom(v.getClass())).collect(Collectors.toList());
    }


    public boolean supports(Class<?> clazz)
    {
        return this.validators.stream().anyMatch(v -> v.supports(clazz));
    }


    public void validate(Object object, Errors errors)
    {
        if(object == null)
        {
            return;
        }
        Class<?> objectClass = object.getClass();
        this.validators.stream().filter(v -> v.supports(objectClass)).forEach(v -> v.validate(object, errors));
    }


    @Autowired
    public void setValidators(Collection<Validator> validators)
    {
        this.validators = validators;
    }


    @Required
    public void setMarkerInterface(Class<?> markerInterface)
    {
        this.markerInterface = markerInterface;
    }
}
