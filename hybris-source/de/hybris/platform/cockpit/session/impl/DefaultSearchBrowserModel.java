package de.hybris.platform.cockpit.session.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CompareMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultSearchContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.GridMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.ListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchHelper;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

public class DefaultSearchBrowserModel extends AbstractSearchBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSearchBrowserModel.class);
    protected SearchProvider searchProvider = null;
    private List<MainAreaComponentFactory> viewModes = null;
    private boolean showCreateButton = false;


    public DefaultSearchBrowserModel()
    {
        this(null);
    }


    public DefaultSearchBrowserModel(ObjectTemplate rootType)
    {
        super(rootType);
        if(this.viewMode == null)
        {
            this.viewMode = "LIST";
        }
        applyPagingMode();
    }


    private void applyPagingMode()
    {
        String pagingMold = "";
        UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        if(perspective != null)
        {
            pagingMold = UITools.getCockpitParameter("default.pagingMold-" + perspective.getUid(), Executions.getCurrent());
        }
        if(StringUtils.isBlank(pagingMold))
        {
            pagingMold = UITools.getCockpitParameter("default.pagingMold", Executions.getCurrent());
        }
        setSimplePaging(StringUtils.equals("simple", pagingMold));
    }


    public void updateItems(Query query)
    {
        setOffset(query.getStart());
        clearSelection();
        ExtendedSearchResult searchResult = doSearchInternal(query);
        setResult(searchResult);
        setOffset(searchResult.getStart());
        setTotalCount(searchResult.getTotalCount());
        fireItemsChanged();
        if(this.contextItems != null)
        {
            setContextItemsPageIndex(0);
            fireContextItemsChanged(true);
        }
    }


    public void updateItems(int page)
    {
        setCurrentPage(page);
        clearSelection();
        if(getLastQuery() == null)
        {
            LOG.error("Can not perform search. Reason: Last query has not been set.");
        }
        else
        {
            Query query = new Query(getLastQuery().getSelectedTypes(), getLastQuery().getSimpleText(), getOffset(), getPageSize());
            query.setNeedTotalCount(!isSimplePaging());
            query.setParameterOrValues(getLastQuery().getParameterOrValues());
            query.setParameterValues(getLastQuery().getParameterValues());
            query.setSortCriteria(getLastQuery().getSortCriteria());
            query.setSimpleSearch(getLastQuery().isSimpleSearch());
            query.setExcludeSubTypes(getLastQuery().isExcludeSubTypes());
            ExtendedSearchResult searchResult = doSearchInternal(query);
            setResult(searchResult);
            setOffset(searchResult.getStart());
            setTotalCount(searchResult.getTotalCount());
            fireItemsChanged();
            if(this.contextItems != null)
            {
                setContextItemsPageIndex(0);
                fireContextItemsChanged(true);
            }
        }
    }


    public Object clone() throws CloneNotSupportedException
    {
        DefaultSearchBrowserModel browserModel = new DefaultSearchBrowserModel(getRootType());
        browserModel.setSearchProvider(getSearchProvider());
        browserModel.setResult(getResult());
        browserModel.setSimplePaging(isSimplePaging());
        browserModel.setLastQuery(getLastQuery());
        browserModel.setSortableProperties(getAdvancedSearchModel().getSortableProperties());
        browserModel.setSortAsc(getAdvancedSearchModel().isSortAscending());
        browserModel.setOffset(getOffset());
        browserModel.setPageSize(getPageSize());
        browserModel.setTotalCount(getTotalCount());
        browserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        browserModel.setBrowserFilter(getBrowserFilter());
        browserModel.setLabel(getLabel());
        browserModel.setViewMode(getViewMode());
        return browserModel;
    }


    public TypedObject getItem(int index)
    {
        if(index < getCurrentPage() * getPageSize() || index >= (getCurrentPage() + 1) * getPageSize())
        {
            setCurrentPage(index / getPageSize());
            updateItems(getCurrentPage());
        }
        List<? extends TypedObject> pageItems = getItems();
        return pageItems.get(index % getPageSize());
    }


    public List<TypedObject> getItems()
    {
        List<TypedObject> ret = Lists.newArrayList();
        if(getResult() != null)
        {
            ret.addAll(getResult().getResult());
            TypeTools.filterOutRemovedItems(ret);
        }
        return ret;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new DefaultSearchContentBrowser();
    }


    public void setSearchProvider(SearchProvider searchProvider)
    {
        this.searchProvider = searchProvider;
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = UISessionUtils.getCurrentSession().getSearchService().getSearchProvider();
        }
        return this.searchProvider;
    }


    protected void updateAdvancedSearchModel(Query query, PropertyDescriptor sortProp, boolean asc)
    {
        if(this.advancedSearchModel != null)
        {
            ObjectTemplate selectedTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
            if(selectedTemplate == null && !query.getSelectedTypes().isEmpty())
            {
                selectedTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(((SearchType)query.getSelectedTypes().iterator().next()).getCode());
            }
            if(!query.getParameterValues().isEmpty() || !query.getParameterOrValues().isEmpty() || (selectedTemplate != null &&
                            !selectedTemplate.equals(getRootType())))
            {
                this.advancedSearchModel.setSelectedType(selectedTemplate);
                for(SearchParameterValue paramValue : query.getParameterValues())
                {
                    SearchParameterDescriptor paramDescr = paramValue.getParameterDescriptor();
                    for(SearchField searchField : this.advancedSearchModel.getSearchFields())
                    {
                        PropertyDescriptor propDescr = this.advancedSearchModel.getPropertyDescriptor(searchField);
                        if(propDescr != null && paramDescr.getQualifier().equalsIgnoreCase(propDescr.getQualifier()))
                        {
                            if(paramValue.getValue() instanceof ConditionValue)
                            {
                                this.advancedSearchModel.getParameterContainer().put(searchField,
                                                AdvancedSearchHelper.createSingleConditionValueContainer((ConditionValue)paramValue.getValue()));
                            }
                            else
                            {
                                this.advancedSearchModel.getParameterContainer().put(searchField,
                                                AdvancedSearchHelper.createSimpleConditionValue(paramValue.getValue()));
                            }
                            try
                            {
                                this.advancedSearchModel.showSearchField(searchField);
                            }
                            catch(Exception e)
                            {
                                LOG.warn("Could not mark search field as visible.", e);
                            }
                        }
                    }
                }
                for(List<SearchParameterValue> paramValues : (Iterable<List<SearchParameterValue>>)query.getParameterOrValues())
                {
                    if(paramValues.isEmpty())
                    {
                        continue;
                    }
                    SearchParameterDescriptor paramDescr = ((SearchParameterValue)paramValues.get(0)).getParameterDescriptor();
                    for(SearchField searchField : this.advancedSearchModel.getSearchFields())
                    {
                        PropertyDescriptor propDescr = this.advancedSearchModel.getPropertyDescriptor(searchField);
                        if(propDescr != null && paramDescr.getQualifier().equalsIgnoreCase(propDescr.getQualifier()))
                        {
                            List<Object> values = new ArrayList();
                            for(SearchParameterValue paramValue : paramValues)
                            {
                                values.add(paramValue.getValue());
                            }
                            if(!values.isEmpty())
                            {
                                this.advancedSearchModel.getParameterContainer().put(searchField,
                                                AdvancedSearchHelper.createSimpleConditionValue(values));
                                try
                                {
                                    this.advancedSearchModel.showSearchField(searchField);
                                }
                                catch(Exception e)
                                {
                                    LOG.warn("Could not mark search field as visible.", e);
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                this.advancedSearchModel.getParameterContainer().clear();
            }
            if(sortProp != null)
            {
                this.advancedSearchModel.getParameterContainer().setSortAscending(asc);
                this.advancedSearchModel.getParameterContainer().setSortProperty(sortProp);
                this.advancedSearchModel.setSortCriterion(sortProp, asc);
            }
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
            ObjectTemplate selTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
            if(selTemplate != null)
            {
                searchQuery.setContextParameter("objectTemplate", selTemplate);
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
                LOG.error("Cloning the query is not supported");
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


    protected String getAllItemLabel()
    {
        return Labels.getLabel("general.allitems");
    }


    protected void updateLabels()
    {
        StringBuffer sBuff = new StringBuffer();
        if(getAdvancedSearchModel().getParameterContainer().hasValues(true))
        {
            if(!getAdvancedSearchModel().getParameterContainer().getSearchFieldValueMap().isEmpty())
            {
                sBuff.append("Filter: ");
                Iterator<SearchField> iterator = getAdvancedSearchModel().getParameterContainer().getSearchFieldValueMap().keySet().iterator();
                while(iterator.hasNext())
                {
                    SearchField field = iterator.next();
                    sBuff.append(field.getLabel());
                    if(iterator.hasNext())
                    {
                        sBuff.append(", ");
                    }
                }
            }
            if(getAdvancedSearchModel().getParameterContainer().getSortProperty() != null)
            {
                if(sBuff.length() > 0)
                {
                    sBuff.append(" | ");
                }
                sBuff.append("Sort: ");
                sBuff.append(getAdvancedSearchModel().getParameterContainer().getSortProperty().getName());
                sBuff.append('/');
                sBuff.append(getAdvancedSearchModel().getParameterContainer().isSortAscending() ? "ASC" : "DESC");
            }
        }
        else
        {
            sBuff.append((getSimpleQuery().length() > 0) ? getSimpleQuery() : getAllItemLabel());
        }
        setLabel(sBuff.toString());
    }


    public List<PropertyDescriptor> getSortProperties()
    {
        List<PropertyDescriptor> sortCriteria = getSearchType().getSortProperties();
        if(sortCriteria != null)
        {
            return sortCriteria;
        }
        return Collections.emptyList();
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
        UINavigationArea navigationArea = getArea().getManagingPerspective().getNavigationArea();
        List<ObjectCollection> specialCollections = navigationArea.getObjectCollectionService().getSpecialCollections(
                        UISessionUtils.getCurrentSession().getUser());
        UICollectionQuery collectionQuery = null;
        for(ObjectCollection oc : specialCollections)
        {
            if("blacklist".equalsIgnoreCase(oc.getQualifier()))
            {
                collectionQuery = new UICollectionQuery(oc);
                break;
            }
        }
        if(collectionQuery != null)
        {
            if(navigationArea instanceof BaseUICockpitNavigationArea)
            {
                List<TypedObject> items = new ArrayList<>();
                Object[] indexesArray = indexes.toArray();
                for(int i = 0; i < indexesArray.length; i++)
                {
                    items.add(getItem(((Integer)indexesArray[i]).intValue()));
                }
                for(TypedObject typedObject : items)
                {
                    ((BaseUICockpitNavigationArea)navigationArea).addToCollection(typedObject, collectionQuery, false);
                }
            }
            List<BrowserModel> browsers = getArea().getManagingPerspective().getBrowserArea().getBrowsers();
            for(BrowserModel browser : browsers)
            {
                browser.updateItems();
            }
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(getArea(), null, Collections.EMPTY_LIST));
            getArea().getManagingPerspective().getNavigationArea().update();
        }
    }


    public void removeItems(Collection<Integer> indexes)
    {
        UINavigationArea navigationArea = getArea().getManagingPerspective().getNavigationArea();
        if(navigationArea instanceof BaseUICockpitNavigationArea)
        {
            Object[] indexesArray = indexes.toArray();
            for(int i = 0; i < indexesArray.length; i++)
            {
                getItems().remove(((Integer)indexesArray[i]).intValue());
            }
        }
    }


    public List<TypedObject> getSelectedItems()
    {
        if(isAllMarked())
        {
            List<TypedObject> selectedItems = Lists.newArrayList();
            SearchProvider searchProvider = getSearchProvider();
            Query query = null;
            try
            {
                query = (Query)getLastQuery().clone();
                query.setStart(0);
                query.setCount(-1);
            }
            catch(CloneNotSupportedException e)
            {
                LOG.error("Cloning the query is not supported");
            }
            if(getBrowserFilterFixed() != null)
            {
                getBrowserFilterFixed().filterQuery(query);
            }
            ExtendedSearchResult result = searchProvider.search(query);
            if(result != null)
            {
                selectedItems.addAll(result.getResult());
            }
            return selectedItems;
        }
        return super.getSelectedItems();
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new GridMainAreaComponentFactory());
            this.viewModes.add(new ListMainAreaComponentFactory());
            this.viewModes.add(new CompareMainAreaComponentFactory());
        }
        return this.viewModes;
    }


    public boolean isShowCreateButton()
    {
        return this.showCreateButton;
    }


    public void setShowCreateButton(boolean showCreateButton)
    {
        this.showCreateButton = showCreateButton;
    }
}
