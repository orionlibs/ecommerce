package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.daos.BackofficeThemeDao;
import com.hybris.backoffice.model.CustomThemeModel;
import com.hybris.backoffice.model.ThemeModel;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Optional;

public class DefaultBackofficeThemeDao implements BackofficeThemeDao
{
    private GenericSearchService genericSearchService;


    public Optional<ThemeModel> findByCode(String code)
    {
        GenericCondition condition = GenericCondition.equals(new GenericSearchField("Theme", "code"), code);
        SearchResult<ThemeModel> searchResult = this.genericSearchService.search(new GenericQuery("Theme", condition));
        return searchResult.getResult().isEmpty() ? Optional.<ThemeModel>empty() : Optional.<ThemeModel>of(searchResult.getResult().get(0));
    }


    public List<ThemeModel> findAllThemes(boolean typeExclusive)
    {
        GenericQuery genericQuery = new GenericQuery("Theme", typeExclusive);
        genericQuery.addOrderBy(new GenericSearchOrderBy(new GenericSearchField("Theme", "sequence")));
        SearchResult<ThemeModel> searchResult = this.genericSearchService.search(genericQuery);
        return searchResult.getResult();
    }


    public List<CustomThemeModel> findAllCustomThemes()
    {
        SearchResult<CustomThemeModel> searchResult = this.genericSearchService.search(new GenericQuery("CustomTheme"));
        return searchResult.getResult();
    }


    public void setGenericSearchService(GenericSearchService genericSearchService)
    {
        this.genericSearchService = genericSearchService;
    }
}
