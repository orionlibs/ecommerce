package de.hybris.platform.personalizationcmsweb.queries;

import de.hybris.platform.personalizationcmsweb.data.CxCmsComponentsListWsDTO;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.impl.AbstractFlexibleSearchRestQueryExecutor;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class CmsContainersFromVariationQueryExecutor extends AbstractFlexibleSearchRestQueryExecutor<CxCmsComponentsListWsDTO, String>
{
    private static final String CUSTOMIZATION = "customization";
    private static final String VARIATIONS = "variations";
    private static final String SEPARATOR = ",";
    private static final String FIELD_REQUIRED = "field.required";
    private static final String DEFAULT_MESSAGE = "Missing required parameter: ";


    protected void validateInputParams(Map<String, String> params, Errors error)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(error, "customization", "field.required", "Missing required parameter: customization");
        ValidationUtils.rejectIfEmptyOrWhitespace(error, "variations", "field.required", "Missing required parameter: variations");
        ValidationUtils.rejectIfEmptyOrWhitespace(error, "catalog", "field.required", "Missing required parameter: catalog");
        ValidationUtils.rejectIfEmptyOrWhitespace(error, "catalogVersion", "field.required", "Missing required parameter: catalogVersion");
    }


    protected String createQuery(Map<String, String> params)
    {
        return "SELECT DISTINCT { A.containerId} FROM { CxCustomization AS CU JOIN CxVariation AS V ON {CU.pk} = {V.customization} JOIN CxCmsAction AS A ON {V.pk} = {A.variation} JOIN CatalogVersion AS CV ON {V.catalogVersion} = {CV.pk} JOIN Catalog AS CT ON {CV.catalog} = {CT.pk} } WHERE {V.code} IN (?variationCodes) AND {CU.code} = ?customizationCode AND {CV.version} = ?version AND {CT.id} = ?catalog ";
    }


    protected Map<String, Object> createQueryParmas(Map<String, String> params)
    {
        String variationsParam = params.getOrDefault("variations", "");
        String[] splittedCodes = variationsParam.split(",");
        List<String> list = Arrays.asList(splittedCodes);
        String customizationsParam = params.getOrDefault("customization", "");
        Map<String, Object> result = new HashMap<>();
        result.put("customizationCode", customizationsParam);
        result.put("variationCodes", list);
        result.put("catalog", params.get("catalog"));
        result.put("version", params.get("catalogVersion"));
        return result;
    }


    protected CxCmsComponentsListWsDTO readResults(SearchResult<String> searchResult)
    {
        CxCmsComponentsListWsDTO result = new CxCmsComponentsListWsDTO();
        result.setComponents(searchResult.getResult());
        return result;
    }


    public List<CatalogVersionWsDTO> getCatalogsForWriteAccess(Map<String, String> params)
    {
        return Collections.emptyList();
    }


    protected List<Class<?>> getResultClasses()
    {
        return Collections.singletonList(String.class);
    }


    public List<CatalogVersionWsDTO> getCatalogsForReadAccess(Map<String, String> params)
    {
        return getCatalogFromParams(params);
    }
}
