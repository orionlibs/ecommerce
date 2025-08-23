package de.hybris.platform.webservicescommons.jaxb;

import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.factory.WebserviceErrorFactory;
import java.io.IOException;
import javax.xml.transform.Result;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpHeaders;

public class Jaxb2HttpErrorConverter extends Jaxb2HttpMessageConverter
{
    private WebserviceErrorFactory webserviceErrorFactory;


    protected boolean supports(Class<?> clazz)
    {
        return (Exception.class.isAssignableFrom(clazz) && super.supports(clazz));
    }


    protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException
    {
        ErrorListWsDTO dto = new ErrorListWsDTO();
        dto.setErrors(this.webserviceErrorFactory.createErrorList(o));
        super.writeToResult(dto, headers, result);
    }


    @Required
    public void setWebserviceErrorFactory(WebserviceErrorFactory webserviceErrorFactory)
    {
        this.webserviceErrorFactory = webserviceErrorFactory;
    }
}
