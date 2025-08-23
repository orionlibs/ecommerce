package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.contentbrowser.ListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.productcockpit.session.ProductSearchBrowserModelListener;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class BundleProductSearchBrowserModel extends DefaultSearchBrowserModel
{
    private static final Logger LOG = Logger.getLogger(BundleProductSearchBrowserModel.class);
    private CatalogService productCockpitCatalogService = null;


    public BundleProductSearchBrowserModel()
    {
        super(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Product"));
    }


    public BundleProductSearchBrowserModel(String templateCode)
    {
        super(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(templateCode));
    }


    public void addBrowserModelListener(BrowserModelListener listener)
    {
        if(listener instanceof ProductSearchBrowserModelListener)
        {
            super.addBrowserModelListener(listener);
        }
        else
        {
            LOG.warn("Not adding listener. Reason: Listener not of type '" + ProductSearchBrowserModelListener.class
                            .getCanonicalName() + "'");
        }
    }


    public Collection<CatalogVersionModel> getSelectedCatalogVersions()
    {
        Collection<CatalogVersionModel> ret = (Collection<CatalogVersionModel>)getLastQuery().getContextParameter("selectedCatalogVersions");
        return (ret == null) ? Collections.<CatalogVersionModel>emptySet() : ret;
    }


    protected void setSelectedCatalogVersions(Collection<CatalogVersionModel> selectedCatalogVersions)
    {
        getLastQuery().setContextParameter("selectedCatalogVersions", selectedCatalogVersions);
    }


    public Collection<CategoryModel> getSelectedCategories()
    {
        Collection<CategoryModel> ret = (Collection<CategoryModel>)getLastQuery().getContextParameter("selectedCategories");
        return (ret == null) ? Collections.<CategoryModel>emptySet() : ret;
    }


    protected void setSelectedCategories(Collection<CategoryModel> selectedCategories)
    {
        getLastQuery().setContextParameter("selectedCategories", selectedCategories);
    }


    public void setRootType(ObjectTemplate rootType)
    {
        if(rootType != null && (this.rootType == null || this.rootType != rootType) &&
                        UISessionUtils.getCurrentSession().getTypeService().getBaseType("Product").isAssignableFrom((ObjectType)rootType))
        {
            super.setRootType(rootType);
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
        int pageSize = (query.getCount() > 0) ? query.getCount() : getPageSize();
        SearchType selectedType = getSelectedType(query);
        Query searchQuery = new Query(Collections.singletonList(selectedType), query.getSimpleText(), query.getStart(), pageSize);
        searchQuery.setParameterValues(query.getParameterValues());
        searchQuery.setParameterOrValues(query.getParameterOrValues());
        searchQuery.setExcludeSubTypes(query.isExcludeSubTypes());
        ObjectTemplate selTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
        if(selTemplate != null)
        {
            searchQuery.setContextParameter("objectTemplate", selTemplate);
        }
        updateCatalogVersionsFromQuery(query);
        updateSelectedCategoriesFromQuery(query);
        Collection<CatalogVersionModel> catalogVersions = getSelectedCatalogVersions();
        if(catalogVersions.isEmpty() && getSelectedCategories().isEmpty())
        {
            catalogVersions = getProductCockpitCatalogService().getAvailableCatalogVersions();
        }
        searchQuery.setContextParameter("selectedCatalogVersions", catalogVersions);
        if(!getSelectedCategories().isEmpty())
        {
            searchQuery.setContextParameter("selectedCategories", getSelectedCategories());
        }
        return sortAndGetResults(query, searchProvider, searchQuery);
    }


    protected ExtendedSearchResult sortAndGetResults(Query query, SearchProvider searchProvider, Query searchQuery)
    {
        Map<PropertyDescriptor, Boolean> sortCriterion = getSortCriterion(query);
        addCriterionToSearchQuery(sortCriterion, searchQuery);
        try
        {
            Query clonedQuery = (Query)searchQuery.clone();
            setLastQuery(clonedQuery);
        }
        catch(CloneNotSupportedException e)
        {
            LOG.error("Cloning the query is not supported");
            LOG.debug("Cloning exception", e);
        }
        if(getBrowserFilter() != null)
        {
            getBrowserFilter().filterQuery(searchQuery);
        }
        ExtendedSearchResult result = searchProvider.search(searchQuery);
        updateLabels();
        return result;
    }


    protected void updateSelectedCategoriesFromQuery(Query query)
    {
        if(query.getContextParameter("selectedCategories") != null)
        {
            setSelectedCategories((Collection<CategoryModel>)query
                            .getContextParameter("selectedCategories"));
        }
    }


    protected void updateCatalogVersionsFromQuery(Query query)
    {
        if(query.getContextParameter("selectedCatalogVersions") != null)
        {
            setSelectedCatalogVersions((Collection<CatalogVersionModel>)query
                            .getContextParameter("selectedCatalogVersions"));
        }
    }


    protected SearchType getSelectedType(Query query)
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


    protected void addCriterionToSearchQuery(Map<PropertyDescriptor, Boolean> sortCriterion, Query searchQuery)
    {
        PropertyDescriptor sortProperties = null;
        boolean asc = true;
        if(MapUtils.isNotEmpty(sortCriterion))
        {
            sortProperties = sortCriterion.keySet().iterator().next();
            if(sortProperties == null)
            {
                LOG.warn("Could not add sort criterion (Reason: Specified sort property is null).");
            }
            else
            {
                if(sortCriterion.get(sortProperties) != null)
                {
                    asc = ((Boolean)sortCriterion.get(sortProperties)).booleanValue();
                }
                searchQuery.addSortCriterion(sortProperties, asc);
            }
        }
        updateAdvancedSearchModel(searchQuery, sortProperties, asc);
    }


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public CatalogService getProductCockpitCatalogService()
    {
        if(this.productCockpitCatalogService == null)
        {
            try
            {
                this.productCockpitCatalogService = (CatalogService)SpringUtil.getBean("productCockpitCatalogService");
            }
            catch(BeansException e)
            {
                LOG.error("Could not get catalog service", (Throwable)e);
            }
        }
        return this.productCockpitCatalogService;
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        List<MainAreaComponentFactory> viewModes = new ArrayList<>();
        viewModes.add(new ListMainAreaComponentFactory());
        return viewModes;
    }


    protected String getAllItemLabel()
    {
        return Labels.getLabel("general.all_products");
    }


    protected void updateLabels()
    {
        StringBuilder sBuff = new StringBuilder();
        StringBuilder advancedSearchBuilder = new StringBuilder();
        if(getAdvancedSearchModel().getParameterContainer().hasValues(true))
        {
            appendSearchFieldValueFilter(advancedSearchBuilder);
            appendSortProperties(advancedSearchBuilder);
        }
        else
        {
            sBuff.append((getSimpleQuery().length() > 0) ? getSimpleQuery() : getAllItemLabel());
        }
        if(hasExactlyOneSearchQueryItem())
        {
            updateLabelSingleItem(sBuff, advancedSearchBuilder);
        }
        else if(hasMulipleSearchQueryItems())
        {
            updateLabelMultipleSelection(advancedSearchBuilder);
        }
        if(advancedSearchBuilder.length() > 0)
        {
            boolean useBrackets = (sBuff.length() > 0);
            sBuff.append((useBrackets ? " (" : "") + (useBrackets ? " (" : "") + advancedSearchBuilder.toString());
        }
        setLabel(sBuff.toString());
    }


    protected void appendSortProperties(StringBuilder advancedSearchBuilder)
    {
        if(getAdvancedSearchModel().getParameterContainer().getSortProperty() != null)
        {
            if(advancedSearchBuilder.length() > 0)
            {
                advancedSearchBuilder.append(" | ");
            }
            advancedSearchBuilder.append("Sort: ");
            advancedSearchBuilder.append(getAdvancedSearchModel().getParameterContainer().getSortProperty().getName());
            advancedSearchBuilder.append('/');
            advancedSearchBuilder.append(getAdvancedSearchModel().getParameterContainer().isSortAscending() ? "ASC" : "DESC");
        }
    }


    protected void appendSearchFieldValueFilter(StringBuilder advancedSearchBuilder)
    {
        if(!getAdvancedSearchModel().getParameterContainer().getSearchFieldValueMap().isEmpty())
        {
            advancedSearchBuilder.append("Filter: ");
            Iterator<SearchField> iterator = getAdvancedSearchModel().getParameterContainer().getSearchFieldValueMap().keySet().iterator();
            while(iterator.hasNext())
            {
                SearchField field = iterator.next();
                advancedSearchBuilder.append(field.getLabel());
                if(iterator.hasNext())
                {
                    advancedSearchBuilder.append(", ");
                }
            }
        }
    }


    protected boolean hasExactlyOneSearchQueryItem()
    {
        return ((getSelectedCatalogVersions().size() == 1 && getSelectedCategories().isEmpty()) || (
                        getSelectedCatalogVersions().isEmpty() && getSelectedCategories().size() == 1));
    }


    protected boolean hasMulipleSearchQueryItems()
    {
        return (!getSelectedCatalogVersions().isEmpty() || !getSelectedCategories().isEmpty());
    }


    protected void updateLabelSingleItem(StringBuilder sBuff, StringBuilder advancedSearchBuilder)
    {
        if(sBuff.length() > 0)
        {
            sBuff.append(" | ");
        }
        if(getSelectedCatalogVersions().size() == 1)
        {
            sBuff.append(getPathAsString(getSelectedCatalogVersions().iterator().next()));
        }
        else
        {
            sBuff.append(getPathAsString(getSelectedCategories().iterator().next()));
        }
        setExtendedLabel("" + sBuff + sBuff);
    }


    protected void updateLabelMultipleSelection(StringBuilder advancedSearchBuilder)
    {
        String delimiter = " | ";
        String extendedLabel = "";
        for(CatalogVersionModel uicv : getSelectedCatalogVersions())
        {
            String path = getPathAsString(uicv);
            if(path.length() > 0)
            {
                extendedLabel = extendedLabel + extendedLabel + delimiter;
                delimiter = ", ";
            }
        }
        for(CategoryModel uic : getSelectedCategories())
        {
            String path = getPathAsString(uic);
            if(path.length() > 0)
            {
                extendedLabel = extendedLabel + extendedLabel + delimiter;
                delimiter = ", ";
            }
        }
        if(advancedSearchBuilder.length() > 0)
        {
            extendedLabel = extendedLabel + " |  (" + extendedLabel + ")";
        }
        setExtendedLabel(extendedLabel);
    }


    protected String getPathAsString(CategoryModel uic)
    {
        String path = "";
        String category = "";
        if(uic != null)
        {
            category = (uic.getName() != null) ? uic.getName() : "";
            String tmp = getPathAsString(getProductCockpitCatalogService().getCatalogVersion(uic));
            if(tmp.length() > 0 && category.length() > 0)
            {
                path = tmp + " » " + tmp;
            }
        }
        return path;
    }


    protected String getPathAsString(CatalogVersionModel uicv)
    {
        String path = "";
        String catalogVersion = "";
        String catalog = "";
        if(uicv != null)
        {
            catalogVersion = (uicv.getVersion() == null) ? "" : uicv.getVersion();
            CatalogModel c = getProductCockpitCatalogService().getCatalog(uicv);
            if(c != null)
            {
                catalog = (c.getName() != null) ? c.getName() : "";
            }
        }
        path = path + path;
        if(catalog.length() > 0 && catalogVersion.length() > 0)
        {
            path = path + " » ";
        }
        path = path + path;
        return path;
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = (SearchProvider)SpringUtil.getBean("productSearchProvider");
        }
        return this.searchProvider;
    }
}
