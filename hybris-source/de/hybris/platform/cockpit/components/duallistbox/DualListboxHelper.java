package de.hybris.platform.cockpit.components.duallistbox;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DualListboxHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(DualListboxHelper.class);


    public static void doAutoCompleteSearch(String searchTerm, DefaultSimpleReferenceSelectorModel model, List<TypedObject> assignedValuesList)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Starting search for '" + searchTerm + "'.");
            }
            SearchProvider searchProvider = getSearchProvider();
            if(searchProvider == null)
            {
                LOG.error("Could not perform search since no search provider could be loaded.");
            }
            else
            {
                ObjectType rootSearchType = model.getAutocompleteSearchType();
                List<? extends Object> resultList = Collections.EMPTY_LIST;
                if(rootSearchType != null)
                {
                    List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession()
                                    .getSearchService().getSearchType(rootSearchType));
                    Query query = new Query(searchTypes, searchTerm, 0, model.getMaxAutoCompleteResultSize());
                    query.setExcludedItems(assignedValuesList);
                    query.setNeedTotalCount(false);
                    query.setExcludeSubTypes(false);
                    ExtendedSearchResult searchResult = searchProvider.search(query);
                    if(searchResult != null)
                    {
                        resultList = searchResult.getResult();
                    }
                }
                model.setAutoCompleteResult(resultList);
            }
        }
        catch(Exception e)
        {
            LOG.error("Auto complete search failed (Reason: '" + e.getMessage() + "').", e);
            model.setAutoCompleteResult(Collections.EMPTY_LIST);
        }
    }


    public static List<TypedObject> removeDuplicatedItems(List<TypedObject> searchResult, List<TypedObject> objectsToRemove)
    {
        List<TypedObject> noDuplicateSearchResult = new ArrayList<>();
        noDuplicateSearchResult.addAll(searchResult);
        noDuplicateSearchResult.removeAll(objectsToRemove);
        return noDuplicateSearchResult;
    }


    protected static SearchProvider getSearchProvider()
    {
        return UISessionUtils.getCurrentSession().getSearchService().getSearchProvider();
    }
}
