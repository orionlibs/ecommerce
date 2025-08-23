package de.hybris.platform.platformbackoffice.services;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface BackofficeSavedQueriesService
{
    public static final String NOT_CONVERTED_ATTRIBUTE_VALUES = "notConvertedAttributeValues";


    List<BackofficeSavedQueryModel> getSavedQueries(UserModel paramUserModel);


    BackofficeSavedQueryModel createSavedQuery(Map<Locale, String> paramMap, AdvancedSearchData paramAdvancedSearchData, UserModel paramUserModel, List<UserGroupModel> paramList);


    AdvancedSearchInitContext getAdvancedSearchInitContext(BackofficeSavedQueryModel paramBackofficeSavedQueryModel);
}
