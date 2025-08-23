package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSyncSearchRestrictionService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultCMSSyncSearchRestrictionService implements CMSSyncSearchRestrictionService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSSyncSearchRestrictionService.class);
    private final FlexibleSearchService flexibleSearchService;
    private final ModelService modelService;
    private final UserService userService;
    private final TypeService typeService;


    public DefaultCMSSyncSearchRestrictionService(TypeService typeService, UserService userService, ModelService modelService, FlexibleSearchService flexibleSearchService)
    {
        this.typeService = typeService;
        this.userService = userService;
        this.modelService = modelService;
        this.flexibleSearchService = flexibleSearchService;
    }


    public SearchRestrictionModel createCmsSyncSearchRestriction(String restrictionCode, Class<?> restrictedTypeClass, String query)
    {
        UserModel principal = getSyncPrincipal();
        List<SearchRestrictionModel> existedSearchRestriction = getSearchRestrictionByCode(restrictionCode);
        if(!existedSearchRestriction.isEmpty())
        {
            LOG.info("SearchRestriction with code: '" + restrictionCode + "' is already existed!");
            return null;
        }
        ComposedTypeModel restrictedType = getTypeService().getComposedTypeForClass(restrictedTypeClass);
        SearchRestrictionModel searchRestriction = (SearchRestrictionModel)getModelService().create(SearchRestrictionModel.class);
        searchRestriction.setCode(restrictionCode);
        searchRestriction.setActive(Boolean.TRUE);
        searchRestriction.setGenerate(Boolean.TRUE);
        searchRestriction.setRestrictedType(restrictedType);
        searchRestriction.setPrincipal((PrincipalModel)principal);
        searchRestriction.setQuery(query);
        getModelService().save(searchRestriction);
        return searchRestriction;
    }


    protected UserModel getSyncPrincipal()
    {
        UserModel syncUser;
        try
        {
            syncUser = getUserService().getUserForUID("cmssyncuser");
        }
        catch(UnknownIdentifierException exception)
        {
            syncUser = (UserModel)getModelService().create(UserModel.class);
            syncUser.setUid("cmssyncuser");
            syncUser.setName("Cms Sync User");
            getModelService().save(syncUser);
        }
        return syncUser;
    }


    protected List<SearchRestrictionModel> getSearchRestrictionByCode(String restrictionCode)
    {
        String queryString = "SELECT {pk} FROM {SearchRestriction} WHERE {code} = ?code";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {SearchRestriction} WHERE {code} = ?code");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("code", restrictionCode);
        fQuery.addQueryParameters(queryParameters);
        SearchResult<SearchRestrictionModel> result = getFlexibleSearchService().search(fQuery);
        return result.getResult();
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }
}
