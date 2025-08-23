package de.hybris.platform.previewwebservices.facades.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.errors.exceptions.AlreadyExistsException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractBaseFacade
{
    private ModelService modelService;


    protected void validateCode(String name, String code)
    {
        if(StringUtils.isEmpty(code))
        {
            throw new IllegalArgumentException(name + " code can't be empty");
        }
    }


    protected void throwAlreadyExists(String name, String code)
    {
        throw new AlreadyExistsException(name + " with code " + name + " already exists.");
    }


    protected NotFoundException createNotFoundException(String name, String code)
    {
        return new NotFoundException(name + " with code " + name + " does not exists");
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
