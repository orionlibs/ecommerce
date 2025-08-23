package de.hybris.platform.webservicescommons.errors.converters;

import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;
import java.util.List;
import org.springframework.core.NestedRuntimeException;

public class ExceptionConverter extends AbstractErrorConverter
{
    public boolean supports(Class<?> clazz)
    {
        return (Exception.class.isAssignableFrom(clazz) && !WebserviceException.class.isAssignableFrom(clazz));
    }


    public void populate(Object o, List<ErrorWsDTO> webserviceErrorList)
    {
        Exception ex = (Exception)o;
        ErrorWsDTO error = new ErrorWsDTO();
        error.setType(ex.getClass().getSimpleName().replace("Exception", "Error"));
        error.setMessage(filterExceptionMessage(ex));
        webserviceErrorList.add(error);
    }


    protected String filterExceptionMessage(Throwable t)
    {
        String message = t.getMessage();
        if(NestedRuntimeException.class.isAssignableFrom(t.getClass()))
        {
            int index = message.indexOf("; nested exception is ");
            if(index > 0)
            {
                return message.substring(0, index);
            }
        }
        return t.getMessage();
    }
}
