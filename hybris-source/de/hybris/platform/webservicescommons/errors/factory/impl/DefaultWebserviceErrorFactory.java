package de.hybris.platform.webservicescommons.errors.factory.impl;

import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.converters.AbstractErrorConverter;
import de.hybris.platform.webservicescommons.errors.factory.WebserviceErrorFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWebserviceErrorFactory implements WebserviceErrorFactory
{
    private List<AbstractErrorConverter> converters;


    public List<ErrorWsDTO> createErrorList(Object obj)
    {
        List<ErrorWsDTO> errors = createTarget();
        Iterator<AbstractErrorConverter> it = this.converters.iterator();
        while(it.hasNext())
        {
            AbstractErrorConverter converter = it.next();
            if(converter.supports(obj.getClass()))
            {
                errors.addAll((Collection<? extends ErrorWsDTO>)converter.convert(obj));
            }
        }
        return errors;
    }


    protected List<ErrorWsDTO> createTarget()
    {
        return new LinkedList<>();
    }


    @Required
    public void setConverters(List<AbstractErrorConverter> converters)
    {
        this.converters = converters;
    }
}
