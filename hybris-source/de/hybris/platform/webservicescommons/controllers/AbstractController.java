package de.hybris.platform.webservicescommons.controllers;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.errors.exceptions.CodeConflictException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import java.util.function.Supplier;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class AbstractController
{
    public static final String CATALOG_PATH = "/catalogs/{catalog}/catalogVersions/{catalogVersion}";
    @Resource
    private WebPaginationUtils webPaginationUtils;


    protected <T> T validateResponse(T item, String type)
    {
        if(item == null)
        {
            throw new NotFoundException(type + " with given code was not found");
        }
        return item;
    }


    protected <T> ResponseEntity<T> getLocationHeader(T item, UriComponentsBuilder builder, String path, String... values)
    {
        UriComponents uriComponents = builder.path(path).buildAndExpand((Object[])values);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity(item, (MultiValueMap)headers, HttpStatus.CREATED);
    }


    protected String validateInputCode(String code, String objectCode, String objectName)
    {
        if((StringUtils.isEmpty(code) ^ StringUtils.isEmpty(objectCode)) != 0)
        {
            return StringUtils.isEmpty(code) ? objectCode : code;
        }
        if(StringUtils.equals(code, objectCode))
        {
            return objectCode;
        }
        throw new CodeConflictException(objectName + " code in request body and url do not match. Code can't be updated.");
    }


    protected void validate(Validator validator, Object object, String name)
    {
        BindingResult result = getBindingResult(object, name);
        validator.validate(object, (Errors)result);
        validate(result);
    }


    protected BindingResult getBindingResult(Object object, String name)
    {
        return (BindingResult)new BeanPropertyBindingResult(object, name);
    }


    protected void validate(BindingResult result)
    {
        if(result.hasErrors())
        {
            throw new WebserviceValidationException(result);
        }
    }


    protected <R> R executeAndConvertException(Supplier<R> supplier)
    {
        try
        {
            return supplier.get();
        }
        catch(UnknownIdentifierException e)
        {
            throw new NotFoundException(e.getMessage(), "Object not found", e);
        }
    }


    protected void executeAndConvertException(Executor executor)
    {
        try
        {
            executor.execute();
        }
        catch(UnknownIdentifierException e)
        {
            throw new NotFoundException(e.getMessage(), "Object not found", e);
        }
    }


    public WebPaginationUtils getWebPaginationUtils()
    {
        return this.webPaginationUtils;
    }


    public void setWebPaginationUtils(WebPaginationUtils webPaginationUtils)
    {
        this.webPaginationUtils = webPaginationUtils;
    }
}
