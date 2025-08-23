package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.browser.BrowserModelFactory;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultProductSearchBrowserModel extends DefaultSearchBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultProductSearchBrowserModel.class);
    private CatalogService productCockpitCatalogService = null;


    public DefaultProductSearchBrowserModel()
    {
        super(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Product"));
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


    public Object clone() throws CloneNotSupportedException
    {
        BrowserModelFactory factory = (BrowserModelFactory)SpringUtil.getBean("BrowserModelFactory");
        DefaultProductSearchBrowserModel browserModel = (DefaultProductSearchBrowserModel)factory.createBrowserModel("DefaultProductSearchBrowserModel");
        browserModel.setSearchProvider(getSearchProvider());
        browserModel.setResult(getResult());
        browserModel.setSimplePaging(isSimplePaging());
        browserModel.setLastQuery((getLastQuery() == null) ? null : (Query)getLastQuery().clone());
        browserModel.setSortableProperties(getAdvancedSearchModel().getSortableProperties());
        browserModel.setSortAsc(getAdvancedSearchModel().isSortAscending());
        browserModel.setOffset(getOffset());
        List<Integer> pageSizes = new ArrayList<>();
        pageSizes.addAll(getPageSizes());
        browserModel.setPageSizes(pageSizes);
        browserModel.setPageSize(getPageSize());
        browserModel.setTotalCount(getTotalCount());
        browserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        browserModel.setBrowserFilter(getBrowserFilter());
        browserModel.updateLabels();
        browserModel.setViewMode(getViewMode());
        browserModel.setShowCreateButton(isShowCreateButton());
        return browserModel;
    }


    public Collection<CatalogVersionModel> getSelectedCatalogVersions()
    {
        Collection<CatalogVersionModel> ret = (Collection<CatalogVersionModel>)getLastQuery().getContextParameter("selectedCatalogVersions");
        return (ret == null) ? Collections.EMPTY_SET : ret;
    }


    protected void setSelectedCatalogVersions(Collection<CatalogVersionModel> selectedCatalogVersions)
    {
        getLastQuery().setContextParameter("selectedCatalogVersions", selectedCatalogVersions);
    }


    public Collection<CategoryModel> getSelectedCategories()
    {
        Collection<CategoryModel> ret = (Collection<CategoryModel>)getLastQuery().getContextParameter("selectedCategories");
        return (ret == null) ? Collections.EMPTY_SET : ret;
    }


    protected void setSelectedCategories(Collection<CategoryModel> selectedCategories)
    {
        getLastQuery().setContextParameter("selectedCategories", selectedCategories);
    }


    public void setRootType(ObjectTemplate rootType)
    {
        if(rootType != null && (this.rootType == null || !this.rootType.equals(rootType)) &&
                        UISessionUtils.getCurrentSession().getTypeService().getBaseType("Product").isAssignableFrom((ObjectType)rootType))
        {
            super.setRootType(rootType);
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Root type not changed. Reason: Either the same root type has already been set or it is not allowed. Current root type: '" + this.rootType + "', requested root type: '" + rootType + "'. Only 'Product' and its subtypes allowed).");
        }
    }


    protected ExtendedSearchResult doSearchInternal(Query query)
    {
        if(query == null)
        {
            throw new IllegalArgumentException("Query can not be null.");
        }
        ExtendedSearchResult result = null;
        SearchProvider searchProvider = getSearchProvider();
        if(searchProvider != null)
        {
            Query searchQuery = null;
            int pageSize = (query.getCount() > 0) ? query.getCount() : getPageSize();
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
            searchQuery = new Query(Collections.singletonList(selectedType), query.getSimpleText(), query.getStart(), pageSize);
            searchQuery.setNeedTotalCount(!isSimplePaging());
            searchQuery.setParameterValues(query.getParameterValues());
            searchQuery.setParameterOrValues(query.getParameterOrValues());
            searchQuery.setExcludeSubTypes(query.isExcludeSubTypes());
            ObjectTemplate selTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
            if(selTemplate != null)
            {
                searchQuery.setContextParameter("objectTemplate", selTemplate);
            }
            if(query.getContextParameter("selectedCatalogVersions") != null)
            {
                try
                {
                    setSelectedCatalogVersions((Collection<CatalogVersionModel>)query
                                    .getContextParameter("selectedCatalogVersions"));
                }
                catch(Exception e)
                {
                    LOG.warn("Could not select catalog versions", e);
                }
            }
            if(query.getContextParameter("selectedCategories") != null)
            {
                try
                {
                    setSelectedCategories((Collection<CategoryModel>)query
                                    .getContextParameter("selectedCategories"));
                }
                catch(Exception e)
                {
                    LOG.warn("Could not set selected categories", e);
                }
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
            Map<PropertyDescriptor, Boolean> sortCriterion = getSortCriterion(query);
            PropertyDescriptor sortProp = null;
            boolean asc = false;
            if(sortCriterion != null && !sortCriterion.isEmpty())
            {
                sortProp = sortCriterion.keySet().iterator().next();
                if(sortProp == null)
                {
                    LOG.warn("Could not add sort criterion (Reason: Specified sort property is null).");
                }
                else
                {
                    if(sortCriterion.get(sortProp) != null)
                    {
                        asc = ((Boolean)sortCriterion.get(sortProp)).booleanValue();
                    }
                    searchQuery.addSortCriterion(sortProp, asc);
                }
            }
            updateAdvancedSearchModel(searchQuery, sortProp, asc);
            try
            {
                Query clonedQuery = (Query)searchQuery.clone();
                setLastQuery(clonedQuery);
            }
            catch(CloneNotSupportedException e)
            {
                LOG.error("Cloning the query is not supported", e);
            }
            if(getBrowserFilter() != null)
            {
                getBrowserFilter().filterQuery(searchQuery);
            }
            result = searchProvider.search(searchQuery);
            updateLabels();
        }
        return result;
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
            catch(Exception e)
            {
                LOG.error("Could not get catalog service", e);
            }
        }
        return this.productCockpitCatalogService;
    }


    protected String getAllItemLabel()
    {
        return Labels.getLabel("general.all_products");
    }


    protected void updateLabels()
    {
        StringBuffer sBuff = new StringBuffer();
        StringBuffer advancedSearchBuff = new StringBuffer();
        if(getAdvancedSearchModel().getParameterContainer().hasValues(true))
        {
            if(!getAdvancedSearchModel().getParameterContainer().getSearchFieldValueMap().isEmpty())
            {
                advancedSearchBuff.append("Filter: ");
                Iterator<SearchField> iterator = getAdvancedSearchModel().getParameterContainer().getSearchFieldValueMap().keySet().iterator();
                while(iterator.hasNext())
                {
                    SearchField field = iterator.next();
                    advancedSearchBuff.append(field.getLabel());
                    if(iterator.hasNext())
                    {
                        advancedSearchBuff.append(", ");
                    }
                }
            }
            if(getAdvancedSearchModel().getParameterContainer().getSortProperty() != null)
            {
                if(advancedSearchBuff.length() > 0)
                {
                    advancedSearchBuff.append(" | ");
                }
                advancedSearchBuff.append("Sort: ");
                advancedSearchBuff.append(getAdvancedSearchModel().getParameterContainer().getSortProperty().getName());
                advancedSearchBuff.append('/');
                advancedSearchBuff.append(getAdvancedSearchModel().getParameterContainer().isSortAscending() ? "ASC" : "DESC");
            }
        }
        else
        {
            sBuff.append((getSimpleQuery().length() > 0) ? getSimpleQuery() : getAllItemLabel());
        }
        if((getSelectedCatalogVersions().size() == 1 && getSelectedCategories().isEmpty()) || (
                        getSelectedCatalogVersions().isEmpty() && getSelectedCategories().size() == 1))
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
        else if(!getSelectedCatalogVersions().isEmpty() || !getSelectedCategories().isEmpty())
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
            if(advancedSearchBuff.length() > 0)
            {
                extendedLabel = extendedLabel + " |  (" + extendedLabel + ")";
            }
            setExtendedLabel(extendedLabel);
        }
        if(advancedSearchBuff.length() > 0)
        {
            boolean useBrackets = (sBuff.length() > 0);
            sBuff.append((useBrackets ? " (" : "") + (useBrackets ? " (" : "") + advancedSearchBuff.toString());
        }
        setLabel(sBuff.toString());
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
