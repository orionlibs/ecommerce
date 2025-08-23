package de.hybris.platform.webservicescommons.resolver;

import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.errors.factory.WebserviceErrorFactory;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public abstract class AbstractRestHandlerExceptionResolver extends AbstractHandlerExceptionResolver
{
    protected static final int UNDEFINED_ERROR_STATUS = 400;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestHandlerExceptionResolver.class);
    private static final int DEFAULT_ORDER = 0;
    private HttpMessageConverter<?>[] messageConverters;
    private WebserviceErrorFactory webserviceErrorFactory;
    private boolean orderSet = false;


    protected int calculateStatusFromException(Exception ex)
    {
        return 400;
    }


    protected boolean shouldDisplayStack(Exception ex)
    {
        return false;
    }


    @PostConstruct
    public void init()
    {
        if(!this.orderSet)
        {
            setOrder(0);
        }
    }


    protected ModelAndView doResolveExceptionInternal(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        response.setStatus(calculateStatusFromException(ex));
        String sanitizedMessage = YSanitizer.sanitize(ex.getMessage());
        LOG.info("Translating exception [{}]: {}", ex.getClass().getName(), sanitizedMessage);
        if(shouldDisplayStack(ex))
        {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }
        ErrorListWsDTO errorListDto = convertException(ex);
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
        ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
        try
        {
            ServletServerHttpResponse servletServerHttpResponse = outputMessage;
            try
            {
                ModelAndView modelAndView = writeWithMessageConverters(errorListDto, (HttpInputMessage)inputMessage, (HttpOutputMessage)outputMessage);
                if(servletServerHttpResponse != null)
                {
                    servletServerHttpResponse.close();
                }
                return modelAndView;
            }
            catch(Throwable throwable)
            {
                if(servletServerHttpResponse != null)
                {
                    try
                    {
                        servletServerHttpResponse.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(Exception handlerException)
        {
            LOG.error(String.format("Handling of [%s] resulted in Exception", new Object[] {ex.getClass().getName()}), handlerException);
            return null;
        }
    }


    protected ErrorListWsDTO convertException(Exception ex)
    {
        List<ErrorWsDTO> errorList;
        if(ex instanceof WebserviceValidationException)
        {
            errorList = getWebserviceErrorFactory().createErrorList(((WebserviceValidationException)ex).getValidationObject());
        }
        else
        {
            errorList = getWebserviceErrorFactory().createErrorList(ex);
        }
        ErrorListWsDTO errorListDto = new ErrorListWsDTO();
        errorListDto.setErrors(errorList);
        return errorListDto;
    }


    protected ModelAndView writeWithMessageConverters(Object returnValue, HttpInputMessage inputMessage, HttpOutputMessage outputMessage) throws IOException
    {
        List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(inputMessage);
        MediaType.sortByQualityValue(acceptedMediaTypes);
        Class<?> returnValueType = returnValue.getClass();
        for(MediaType acceptedMediaType : acceptedMediaTypes)
        {
            for(HttpMessageConverter<?> messageConverter : getMessageConverters())
            {
                if(messageConverter.canWrite(returnValueType, acceptedMediaType))
                {
                    messageConverter.write(returnValue, acceptedMediaType, outputMessage);
                    return new ModelAndView();
                }
            }
        }
        LOG.warn("Could not find HttpMessageConverter that supports return type [{}}] and {}", returnValueType, acceptedMediaTypes);
        return null;
    }


    protected List<MediaType> getAcceptedMediaTypes(HttpInputMessage httpInputMessage)
    {
        List<MediaType> acceptedMediaTypes = httpInputMessage.getHeaders().getAccept();
        if(acceptedMediaTypes.isEmpty())
        {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        return acceptedMediaTypes;
    }


    public void setOrder(int order)
    {
        super.setOrder(order);
        this.orderSet = true;
    }


    public HttpMessageConverter<?>[] getMessageConverters()
    {
        return this.messageConverters;
    }


    @Required
    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters)
    {
        this.messageConverters = messageConverters;
    }


    protected WebserviceErrorFactory getWebserviceErrorFactory()
    {
        return this.webserviceErrorFactory;
    }


    @Required
    public void setWebserviceErrorFactory(WebserviceErrorFactory webserviceErrorFactory)
    {
        this.webserviceErrorFactory = webserviceErrorFactory;
    }
}
