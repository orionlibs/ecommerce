package de.hybris.platform.webservicescommons.errors.converters;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidationErrorConverter extends AbstractLocalizedErrorConverter
{
    private static final String TYPE = "ValidationError";
    private static final String SUBJECT_TYPE = "parameter";
    private static final String REASON_INVALID = "invalid";
    private static final String REASON_MISSING = "missing";
    private I18NService i18NService;


    public boolean supports(Class<?> clazz)
    {
        return Errors.class.isAssignableFrom(clazz);
    }


    public void populate(Object o, List<ErrorWsDTO> webserviceErrorList)
    {
        Errors errors = (Errors)o;
        Locale currentLocale = this.i18NService.getCurrentLocale();
        for(ObjectError error : errors.getGlobalErrors())
        {
            ErrorWsDTO errorDto = createTargetElement();
            errorDto.setType("ValidationError");
            errorDto.setSubjectType("parameter");
            errorDto.setMessage(getMessage(error.getCode(), error.getArguments(), currentLocale, error.getDefaultMessage()));
            errorDto.setSubject(error.getObjectName());
            errorDto.setReason("invalid");
            webserviceErrorList.add(errorDto);
        }
        for(FieldError error : errors.getFieldErrors())
        {
            ErrorWsDTO errorDto = createTargetElement();
            errorDto.setType("ValidationError");
            errorDto.setSubjectType("parameter");
            errorDto.setMessage(getMessage(error.getCode(), error.getArguments(), currentLocale, error.getDefaultMessage()));
            errorDto.setSubject(error.getField());
            errorDto.setReason((error.getRejectedValue() == null) ? "missing" : "invalid");
            webserviceErrorList.add(errorDto);
        }
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
