package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl.type.BundleRuleType;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zkplus.spring.SpringUtil;

public class BundleRulesSearchBrowserModel extends BundleProductSearchBrowserModel
{
    private static final Logger LOG = Logger.getLogger(BundleRulesSearchBrowserModel.class);
    private BundleRuleType bundleRuleType = null;


    public BundleRulesSearchBrowserModel(String templateCode)
    {
        super(templateCode);
        this.bundleRuleType = BundleRuleType.fromValue(templateCode);
    }


    public void setSimpleQuery(String simpleQuery)
    {
        List<SearchParameterValue> paramValues = new ArrayList<>();
        for(SearchParameterValue searchParameterValue : getLastQuery().getParameterValues())
        {
            if(this.bundleRuleType.getTemplateBundleName()
                            .equalsIgnoreCase(searchParameterValue.getParameterDescriptor().getQualifier()))
            {
                paramValues.add(searchParameterValue);
            }
        }
        super.setSimpleQuery(simpleQuery);
        getLastQuery().setParameterValues(paramValues);
    }


    public void setRootType(ObjectTemplate rootType)
    {
        if(rootType != null && (this.rootType == null || this.rootType != rootType))
        {
            if(UISessionUtils.getCurrentSession().getTypeService().getBaseType(this.bundleRuleType.getRuleName())
                            .isAssignableFrom((ObjectType)rootType))
            {
                super.setRootType(rootType);
            }
        }
    }


    protected ExtendedSearchResult doSearchInternal(Query query)
    {
        ServicesUtil.validateParameterNotNull(query, "Query can not be null.");
        SearchProvider searchProvider = getSearchProvider();
        if(searchProvider == null)
        {
            return null;
        }
        Query searchQuery = createSearchQuery(query);
        searchQuery = setupQueryCatalog(searchQuery, query);
        return sortAndGetResults(query, searchProvider, searchQuery);
    }


    protected Query createSearchQuery(Query query)
    {
        int pageSize = (query.getCount() > 0) ? query.getCount() : getPageSize();
        SearchType selectedType = getSelectedTypeFromQuery(query);
        Query searchQuery = new Query(Collections.singletonList(selectedType), query.getSimpleText(), query.getStart(), pageSize);
        searchQuery.setParameterValues(query.getParameterValues());
        searchQuery.setParameterOrValues(query.getParameterOrValues());
        searchQuery.setExcludeSubTypes(query.isExcludeSubTypes());
        if(CollectionUtils.isNotEmpty(searchQuery.getParameterValues()))
        {
            if(!bundleForQueryExists(searchQuery))
            {
                searchQuery.setParameterValues(updateSearchParameters(searchQuery.getParameterValues()));
            }
        }
        else
        {
            searchQuery.setParameterValues(updateSearchParameters(searchQuery.getParameterValues()));
        }
        ObjectTemplate selTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
        if(selTemplate != null)
        {
            searchQuery.setContextParameter("objectTemplate", selTemplate);
        }
        return searchQuery;
    }


    protected boolean bundleForQueryExists(Query searchQuery)
    {
        boolean bundleExists = false;
        for(SearchParameterValue searchParameterValue : searchQuery.getParameterValues())
        {
            if(this.bundleRuleType.getTemplateBundleName().equalsIgnoreCase(searchParameterValue
                            .getParameterDescriptor().getQualifier()))
            {
                bundleExists = true;
            }
        }
        return bundleExists;
    }


    protected SearchType getSelectedTypeFromQuery(Query query)
    {
        SearchType selectedType = null;
        if(query.getSelectedTypes().size() == 1)
        {
            selectedType = query.getSelectedTypes().iterator().next();
        }
        else if(!query.getSelectedTypes().isEmpty())
        {
            selectedType = query.getSelectedTypes().iterator().next();
            LOG.warn("Query has ambigious search types. Using '" + selectedType.getCode() + "' for searching.");
        }
        if(selectedType == null)
        {
            selectedType = getSearchType();
        }
        return selectedType;
    }


    protected Query setupQueryCatalog(Query searchQuery, Query userQuery)
    {
        if(userQuery.getContextParameter("selectedCatalogVersions") != null)
        {
            setSelectedCatalogVersions((Collection)userQuery
                            .getContextParameter("selectedCatalogVersions"));
        }
        if(userQuery.getContextParameter("selectedCategories") != null)
        {
            setSelectedCategories((Collection)userQuery
                            .getContextParameter("selectedCategories"));
        }
        Collection<CatalogVersionModel> catver = getSelectedCatalogVersions();
        if(catver.isEmpty() && getSelectedCategories().isEmpty())
        {
            catver = getProductCockpitCatalogService().getAvailableCatalogVersions();
        }
        searchQuery.setContextParameter("selectedCatalogVersions", catver);
        if(!getSelectedCategories().isEmpty())
        {
            searchQuery.setContextParameter("selectedCategories", getSelectedCategories());
        }
        return searchQuery;
    }


    protected List<SearchParameterValue> updateSearchParameters(List<SearchParameterValue> parameterValues)
    {
        List<SearchParameterValue> paramValues = new ArrayList<>(parameterValues);
        for(SearchParameterValue searchParameterValue : getLastQuery().getParameterValues())
        {
            if(this.bundleRuleType.getTemplateBundleName()
                            .equalsIgnoreCase(searchParameterValue.getParameterDescriptor().getQualifier()))
            {
                paramValues.add(searchParameterValue);
            }
        }
        return paramValues;
    }


    protected String getAllItemLabel()
    {
        return "";
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = (SearchProvider)SpringUtil.getBean("genericSearchProvider");
        }
        return this.searchProvider;
    }
}
