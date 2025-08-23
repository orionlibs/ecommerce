package de.hybris.platform.adaptivesearchfacades.facades.impl;

import de.hybris.adaptivesearchfacades.data.AsSearchProfileData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.adaptivesearchfacades.facades.AsSearchProfileFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileFacade implements AsSearchProfileFacade
{
    public static final String COMMA_SEPARATOR = ",";
    public static final String CATALOG_VERSIONS_FILTER = "catalogVersions";
    public static final String INDEX_TYPES_FILTER = "indexTypes";
    private AsSearchProfileService asSearchProfileService;
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private Converter<AbstractAsSearchProfileModel, AsSearchProfileData> asSearchProfileDataConverter;


    public List<AsSearchProfileData> getSearchProfiles(String query, Map<String, String> filters)
    {
        Map<String, Object> paramsMap = buildParameters(filters);
        if(currentUserReadableCatalogVersionParamsFromFilterIsEmpty(filters, paramsMap))
        {
            return Collections.emptyList();
        }
        List<AbstractAsSearchProfileModel> searchProfiles = this.asSearchProfileService.getSearchProfiles(query, paramsMap);
        return this.asSearchProfileDataConverter.convertAll(searchProfiles);
    }


    public SearchPageData<AsSearchProfileData> getSearchProfiles(String query, Map<String, String> filters, SearchPageData<?> pagination)
    {
        Map<String, Object> parameters = buildParameters(filters);
        if(currentUserReadableCatalogVersionParamsFromFilterIsEmpty(filters, parameters))
        {
            return createEmptyAsSearchPageData(pagination);
        }
        SearchPageData<AbstractAsSearchProfileModel> searchProfiles = this.asSearchProfileService.getSearchProfiles(query, parameters, pagination);
        return createAsSearchPageData(searchProfiles);
    }


    protected boolean currentUserReadableCatalogVersionParamsFromFilterIsEmpty(Map<String, String> filters, Map<String, Object> paramsMap)
    {
        return (StringUtils.isNotBlank(filters.get("catalogVersions")) &&
                        CollectionUtils.isEmpty((Collection)paramsMap.get("catalogVersion")));
    }


    protected SearchPageData<AsSearchProfileData> createAsSearchPageData(SearchPageData<AbstractAsSearchProfileModel> input)
    {
        List<AsSearchProfileData> asSearchProfileData = this.asSearchProfileDataConverter.convertAll(input.getResults());
        SearchPageData<AsSearchProfileData> result = new SearchPageData();
        result.setPagination(input.getPagination());
        result.setSorts(input.getSorts());
        result.setResults(asSearchProfileData);
        return result;
    }


    protected SearchPageData<AsSearchProfileData> createEmptyAsSearchPageData(SearchPageData<?> pagination)
    {
        SearchPageData<AsSearchProfileData> result = new SearchPageData();
        result.setResults(Collections.emptyList());
        result.setPagination(pagination.getPagination());
        return result;
    }


    protected Map<String, Object> buildParameters(Map<String, String> filters)
    {
        Map<String, Object> parameters = new HashMap<>();
        String catalogVersionsFilter = filters.get("catalogVersions");
        if(StringUtils.isNotBlank(catalogVersionsFilter))
        {
            parameters.put("catalogVersion", getCurrentUserReadableCatalogVersionsFromFilter(catalogVersionsFilter));
        }
        String indexTypesFilter = filters.get("indexTypes");
        if(StringUtils.isNotBlank(indexTypesFilter))
        {
            parameters.put("indexType", getIndexTypesFromFilter(indexTypesFilter));
        }
        return parameters;
    }


    protected Collection<CatalogVersionModel> getCurrentUserReadableCatalogVersions()
    {
        UserModel userModel = this.userService.getCurrentUser();
        return this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)userModel);
    }


    protected List<CatalogVersionModel> getCurrentUserReadableCatalogVersionsFromFilter(String catalogVersions)
    {
        List<String> filterCatalogVersions = Arrays.asList(catalogVersions.split(","));
        return (List<CatalogVersionModel>)getCurrentUserReadableCatalogVersions()
                        .stream()
                        .filter(cv -> {
                            String cvStr = cv.getCatalog().getId() + ":" + cv.getCatalog().getId();
                            return filterCatalogVersions.contains(cvStr);
                        }).collect(Collectors.toList());
    }


    protected List<String> getIndexTypesFromFilter(String indexTypes)
    {
        return Arrays.asList(indexTypes.split(","));
    }


    public AsSearchProfileService getAsSearchProfileService()
    {
        return this.asSearchProfileService;
    }


    @Required
    public void setAsSearchProfileService(AsSearchProfileService asSearchProfileService)
    {
        this.asSearchProfileService = asSearchProfileService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public Converter<AbstractAsSearchProfileModel, AsSearchProfileData> getAsSearchProfileDataConverter()
    {
        return this.asSearchProfileDataConverter;
    }


    @Required
    public void setAsSearchProfileDataConverter(Converter<AbstractAsSearchProfileModel, AsSearchProfileData> asSearchProfileDataConverter)
    {
        this.asSearchProfileDataConverter = asSearchProfileDataConverter;
    }
}
