package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AbstractAdvancedSearchViewListener;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchHelper;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultBrowserAdvancedSearchViewListener extends AbstractAdvancedSearchViewListener
{
    private final SearchBrowserModel browser;


    public DefaultBrowserAdvancedSearchViewListener(SearchBrowserModel browser, DefaultAdvancedSearchModel model)
    {
        super(model);
        this.browser = browser;
    }


    public void searchButtonClicked(AdvancedSearchParameterContainer parameterContainer)
    {
        ObjectTemplate objectTemplate = this.model.getSelectedType();
        List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession().getSearchService()
                        .getSearchType((ObjectType)objectTemplate));
        Query query = new Query(searchTypes, null, 0, this.browser.getPageSize());
        query.setContextParameter("objectTemplate", objectTemplate);
        List<SearchParameterValue> parameterValues = new LinkedList<>();
        List<List<SearchParameterValue>> orValues = new LinkedList<>();
        AdvancedSearchHelper.createSearchParameterValues((AdvancedSearchModel)this.model, parameterContainer, parameterValues, orValues);
        query.setParameterValues(parameterValues);
        query.setParameterOrValues(orValues);
        query.setSimpleSearch(false);
        query.setExcludeSubTypes(parameterContainer.isExcludeSubtypes());
        this.model.setSortedByProperty(parameterContainer.getSortProperty());
        PropertyDescriptor sortDescr = parameterContainer.getSortProperty();
        if(sortDescr != null)
        {
            query.addSortCriterion(sortDescr, parameterContainer.isSortAscending());
        }
        this.browser.updateItems(query);
        if(!this.browser.isAdvancedSearchSticky())
        {
            this.browser.setAdvancedSearchVisible(false);
        }
    }
}
