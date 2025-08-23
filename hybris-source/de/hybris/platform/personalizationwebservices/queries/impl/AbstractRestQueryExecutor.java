package de.hybris.platform.personalizationwebservices.queries.impl;

import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.RestQueryExecutor;
import de.hybris.platform.personalizationwebservices.security.QueryEndpointPermissionsChecker;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.util.LocalViewExecutor;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ValidationUtils;

public abstract class AbstractRestQueryExecutor implements RestQueryExecutor
{
    private LocalViewExecutor localViewExecutor;
    private QueryEndpointPermissionsChecker queryEndpointPermissionsChecker;


    public Object execute(Map<String, String> params)
    {
        return this.localViewExecutor.executeWithAllCatalogs(() -> {
            validate(params);
            this.queryEndpointPermissionsChecker.checkCurrentUserAllowed(getCatalogsForReadAccess(params), getCatalogsForWriteAccess(params));
            return executeAfterValidation(params);
        });
    }


    protected void validate(Map<String, String> params)
    {
        MapBindingResult mapBindingResult = new MapBindingResult(params, "params");
        validateInputParams(params, (Errors)mapBindingResult);
        if(mapBindingResult.hasErrors())
        {
            throw new WebserviceValidationException(mapBindingResult);
        }
    }


    protected void validateMissingField(Errors errors, String... fields)
    {
        if(fields == null)
        {
            return;
        }
        for(String field : fields)
        {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, "field.required", "Missing required field: " + field);
        }
    }


    protected List<CatalogVersionWsDTO> getCatalogFromParams(Map<String, String> params)
    {
        if(params.get("catalog") == null || params.get("catalogVersion") == null)
        {
            Collections.emptyList();
        }
        CatalogVersionWsDTO cv = new CatalogVersionWsDTO();
        cv.setCatalog(params.get("catalog"));
        cv.setVersion(params.get("catalogVersion"));
        return Collections.singletonList(cv);
    }


    public void setLocalViewExecutor(LocalViewExecutor localViewExecutor)
    {
        this.localViewExecutor = localViewExecutor;
    }


    protected LocalViewExecutor getLocalViewExecutor()
    {
        return this.localViewExecutor;
    }


    protected QueryEndpointPermissionsChecker getQueryEndpointPermissionsChecker()
    {
        return this.queryEndpointPermissionsChecker;
    }


    public void setQueryEndpointPermissionsChecker(QueryEndpointPermissionsChecker queryEndpointPermissionsChecker)
    {
        this.queryEndpointPermissionsChecker = queryEndpointPermissionsChecker;
    }


    protected abstract Object executeAfterValidation(Map<String, String> paramMap);


    protected abstract void validateInputParams(Map<String, String> paramMap, Errors paramErrors);
}
