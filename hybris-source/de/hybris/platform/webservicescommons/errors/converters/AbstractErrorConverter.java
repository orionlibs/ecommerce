package de.hybris.platform.webservicescommons.errors.converters;

import de.hybris.platform.converters.impl.AbstractConverter;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractErrorConverter extends AbstractConverter<Object, List<ErrorWsDTO>>
{
    protected AbstractErrorConverter()
    {
        setTargetClassInternal((Class)LinkedList.class);
    }


    protected ErrorWsDTO createTargetElement()
    {
        return new ErrorWsDTO();
    }


    public abstract boolean supports(Class paramClass);


    private void setTargetClassInternal(Class<List<ErrorWsDTO>> targetClass)
    {
        setTargetClass(targetClass);
    }
}
