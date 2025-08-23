package de.hybris.platform.personalizationcmsweb.queries;

import de.hybris.platform.personalizationfacades.customization.CustomizationFacade;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.impl.AbstractRestQueryExecutor;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.validation.Errors;

public class CxUpdateCustomizationRankExecutor extends AbstractRestQueryExecutor
{
    public static final String CUSTOMIZATION_CODE = "customization";
    public static final String INCREASE_VALUE = "increaseValue";
    private static final String[] CREATE_FIELDS = new String[] {"customization", "increaseValue", "catalog", "catalogVersion"};
    private CustomizationFacade customizationFacade;
    private FlexibleSearchService flexibleSearchService;
    Pattern integerValidationRegex = Pattern.compile("-?\\d+");


    protected void validateInputParams(Map<String, String> params, Errors errors)
    {
        validateMissingField(errors, CREATE_FIELDS);
        validateMissingField(errors, new String[] {"increaseValue"});
        if(!this.integerValidationRegex.matcher(params.get("increaseValue")).matches())
        {
            errors.rejectValue("increaseValue", "Increase value must be a number");
        }
    }


    protected Object executeAfterValidation(Map<String, String> params)
    {
        return updateCustomizationRank(params);
    }


    protected String createSubQuery()
    {
        return "SELECT { C.groupPOS} AS c_rank FROM { CxCustomization AS C JOIN CatalogVersion AS CV ON {C.catalogVersion} = {CV.pk} JOIN Catalog AS CT ON {CV.catalog} = {CT.pk} } WHERE (({C.groupPOS} <= ?calculatedRank AND ?increaseValue < 0) OR ({C.groupPOS} >= ?calculatedRank AND ?increaseValue > 0)) AND {C.status} <> ?status AND {CV.version} = ?catalogVersion AND {CT.id} = ?catalog ";
    }


    protected String createQuery(String subQuery, Integer increaseValue)
    {
        return (increaseValue.intValue() < 0) ? ("SELECT MAX(result.c_rank) FROM ( {{ " +
                        subQuery + " }} ) result") : ("SELECT MIN(result.c_rank) FROM ( {{ " +
                        subQuery + " }} ) result");
    }


    protected Object updateCustomizationRank(Map<String, String> params)
    {
        String customizationCode = params.get("customization");
        String catalog = params.get("catalog");
        String catalogVersion = params.get("catalogVersion");
        Integer increaseValue = Integer.valueOf(params.get("increaseValue"));
        CustomizationData customization = this.customizationFacade.getCustomization(customizationCode, catalog, catalogVersion);
        Integer calculatedRank = Integer.valueOf(customization.getRank().intValue() + increaseValue.intValue());
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("increaseValue", increaseValue);
        queryParams.put("calculatedRank", calculatedRank);
        queryParams.put("status", CxItemStatus.DELETED);
        queryParams.put("catalog", catalog);
        queryParams.put("catalogVersion", catalogVersion);
        FlexibleSearchQuery query = new FlexibleSearchQuery(createQuery(createSubQuery(), increaseValue), queryParams);
        query.setResultClassList(Collections.singletonList(Integer.class));
        SearchResult<Integer> searchResult = this.flexibleSearchService.search(query);
        Integer newRank = searchResult.getResult().stream().filter(Objects::nonNull).findFirst().orElse(customization.getRank());
        customization.setRank(newRank);
        this.customizationFacade.updateCustomization(customizationCode, customization, catalog, catalogVersion);
        return null;
    }


    public List<CatalogVersionWsDTO> getCatalogsForWriteAccess(Map<String, String> params)
    {
        return getCatalogFromParams(params);
    }


    public List<CatalogVersionWsDTO> getCatalogsForReadAccess(Map<String, String> params)
    {
        return getCatalogFromParams(params);
    }


    protected CustomizationFacade getCustomizationFacade()
    {
        return this.customizationFacade;
    }


    public void setCustomizationFacade(CustomizationFacade customizationFacade)
    {
        this.customizationFacade = customizationFacade;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
