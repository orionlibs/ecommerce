package de.hybris.platform.webservicescommons.oauth2;

import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.converters.AbstractErrorConverter;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.ServletWebRequest;

public class OAuth2ExceptionRenderer extends DefaultOAuth2ExceptionRenderer
{
    private AbstractErrorConverter exceptionConverter;


    public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception
    {
        ResponseEntity<OAuth2Exception> castedResponseEntity = (ResponseEntity)responseEntity;
        OAuth2Exception ex = (OAuth2Exception)castedResponseEntity.getBody();
        ErrorListWsDTO errorListDto = new ErrorListWsDTO();
        errorListDto.setErrors((List)this.exceptionConverter.convert(ex));
        ResponseEntity<ErrorListWsDTO> convertedResponseEntity = new ResponseEntity(errorListDto, (MultiValueMap)responseEntity.getHeaders(), castedResponseEntity.getStatusCode());
        super.handleHttpEntityResponse((HttpEntity)convertedResponseEntity, webRequest);
    }


    @Required
    public void setExceptionConverter(AbstractErrorConverter exceptionConverter)
    {
        this.exceptionConverter = exceptionConverter;
    }
}
