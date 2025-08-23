package de.hybris.platform.webservicescommons.errors.converters;

import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class WebserviceExceptionConverter extends AbstractErrorConverter
{
    public boolean supports(Class<?> clazz)
    {
        return WebserviceException.class.isAssignableFrom(clazz);
    }


    public void populate(Object o, List<ErrorWsDTO> webserviceErrorList)
    {
        WebserviceException ex = (WebserviceException)o;
        ErrorWsDTO error = createTargetElement();
        if(StringUtils.isNotEmpty(ex.getSubject()))
        {
            error.setSubject(ex.getSubject());
            if(StringUtils.isNotEmpty(ex.getSubjectType()))
            {
                error.setSubjectType(ex.getSubjectType());
            }
        }
        if(StringUtils.isNotEmpty(ex.getReason()))
        {
            error.setReason(ex.getReason());
        }
        if(StringUtils.isNotBlank(ex.getType()))
        {
            error.setType(ex.getType());
        }
        error.setMessage(ex.getMessage());
        webserviceErrorList.add(error);
    }
}
