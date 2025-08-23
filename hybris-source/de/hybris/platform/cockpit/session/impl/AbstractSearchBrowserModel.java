package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.BrowserFilterFactory;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModelListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;

public abstract class AbstractSearchBrowserModel extends AbstractPageableBrowserModel implements SearchBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSearchBrowserModel.class);
    public static final String QUERY_PATH_DELIMITER = " | ";
    public static final String PATH_LIST_DELIMITER = ", ";
    public static final String PATH_DELIMITER = " » ";
    protected static final String ADVANCED_SEARCH_CONF = "advancedSearch";
    protected DefaultAdvancedSearchModel advancedSearchModel = null;
    private boolean advancedSearchVisible = false;
    private boolean advancedSearchSticky = false;
    private Query lastQuery = null;
    private ExtendedSearchResult result = null;
    private UIConfigurationService uiConfigurationService = null;


    public AbstractSearchBrowserModel(ObjectTemplate rootType)
    {
        setItemsMovable(false);
        setItemsRemovable(false);
        setContextItemsMovable(false);
        setContextItemsRemovable(false);
        this.lastQuery = new Query(null, "", 0, 0);
        this.rootType = rootType;
        if(this.rootType != null)
        {
            this.rootType = rootType;
            UIConfigurationService configService = getUIConfigurationService();
            AdvancedSearchConfiguration componentConfig = (AdvancedSearchConfiguration)configService.getComponentConfiguration(rootType, "advancedSearch", AdvancedSearchConfiguration.class);
            this.advancedSearchModel = new DefaultAdvancedSearchModel(componentConfig, "advancedSearch");
        }
    }


    public void setRootType(ObjectTemplate rootType)
    {
        if(rootType != null)
        {
            UIConfigurationService configService = getUIConfigurationService();
            AdvancedSearchConfiguration componentConfig = (AdvancedSearchConfiguration)configService.getComponentConfiguration(rootType, "advancedSearch", AdvancedSearchConfiguration.class);
            AdvancedSearchParameterContainer oldParamContainer = null;
            if(this.advancedSearchModel != null)
            {
                oldParamContainer = this.advancedSearchModel.getParameterContainer();
            }
            this.advancedSearchModel = new DefaultAdvancedSearchModel(componentConfig, "advancedSearch", oldParamContainer);
        }
        super.setRootType(rootType);
    }


    public ObjectTemplate getLastType()
    {
        ObjectTemplate ret = null;
        if(this.lastQuery == null || this.lastQuery.getSelectedTypes().isEmpty())
        {
            ret = super.getLastType();
        }
        else if(this.lastQuery != null)
        {
            Object rawObjectTemplate = this.lastQuery.getContextParameter("objectTemplate");
            if(rawObjectTemplate != null)
            {
                ret = (ObjectTemplate)rawObjectTemplate;
            }
            else
            {
                ret = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(((SearchType)this.lastQuery.getSelectedTypes().iterator().next()).getCode());
            }
        }
        return ret;
    }


    public void setLastQuery(Query query)
    {
        this.lastQuery = query;
    }


    public Query getLastQuery()
    {
        return this.lastQuery;
    }


    public void collapse()
    {
        setContextVisibleDirect(false);
        setAdvancedSearchVisible(false);
        fireChanged();
    }


    public boolean isCollapsed()
    {
        return (!isContextVisible() && !isAdvancedSearchVisible());
    }


    public void setResult(ExtendedSearchResult result)
    {
        this.result = result;
    }


    public ExtendedSearchResult getResult()
    {
        return this.result;
    }


    public void setSortedByProperty(PropertyDescriptor sortProp)
    {
        if(this.advancedSearchModel == null)
        {
            LOG.warn("Can not set sort field. Reason: Advanced search model not available.");
        }
        else
        {
            this.advancedSearchModel.setSortedByProperty(sortProp);
        }
    }


    public void setSortAsc(boolean asc)
    {
        if(this.advancedSearchModel == null)
        {
            LOG.warn("Can not set sort direction. Reason: Advanced search model not available.");
        }
        else
        {
            this.advancedSearchModel.setSortAscending(asc);
        }
    }


    public void setSortableProperties(List<PropertyDescriptor> sortProps)
    {
        if(this.advancedSearchModel == null)
        {
            LOG.warn("Can not set sort fields. Reason: Advanced search model not available.");
        }
        else
        {
            this.advancedSearchModel.setSortableProperties(sortProps);
        }
    }


    public String getSimpleQuery()
    {
        return (getLastQuery().getSimpleText() == null) ? "" : getLastQuery().getSimpleText();
    }


    public void setSimpleQuery(String simpleQuery)
    {
        if(UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea() instanceof BaseUICockpitNavigationArea)
        {
            ((BaseUICockpitNavigationArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea())
                            .setSelectedSavedQuery(null, false);
        }
        Query lastQuery = getLastQuery();
        lastQuery.setSimpleText(simpleQuery);
        lastQuery.setSimpleSearch(true);
        if(!this.advancedSearchVisible)
        {
            lastQuery.clearParameterOrValues();
            lastQuery.clearParameterValues();
            lastQuery.setContextParameter("objectTemplate", null);
            getAdvancedSearchModel().getParameterContainer().clear();
            lastQuery.setSelectedTypes(Collections.singleton(getSearchType()));
            this.advancedSearchModel.setSelectedType(getRootType());
        }
    }


    public AdvancedSearchModel getAdvancedSearchModel()
    {
        return (AdvancedSearchModel)this.advancedSearchModel;
    }


    public boolean isAdvancedSearchSticky()
    {
        return this.advancedSearchSticky;
    }


    public void setAdvancedSearchSticky(boolean sticky)
    {
        this.advancedSearchSticky = sticky;
    }


    public boolean isAdvancedHeaderDropdownSticky()
    {
        return isAdvancedSearchSticky();
    }


    public boolean isAdvancedHeaderDropdownVisible()
    {
        return isAdvancedSearchVisible();
    }


    public boolean isAdvancedSearchVisible()
    {
        return this.advancedSearchVisible;
    }


    public void setAdvancedSearchVisible(boolean advancedSearchVisible)
    {
        if(this.advancedSearchVisible != advancedSearchVisible)
        {
            this.advancedSearchVisible = advancedSearchVisible;
            if(advancedSearchVisible)
            {
                setSimpleQuery("");
            }
            fireAdvancedSearchVisibilityChanged();
        }
    }


    public abstract List<PropertyDescriptor> getSortProperties();


    public abstract void updateItems(Query paramQuery);


    protected void fireAdvancedSearchVisibilityChanged()
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            ((SearchBrowserModelListener)listener).advancedSearchVisibiltyChanged(this);
        }
    }


    protected Map<PropertyDescriptor, Boolean> getSortCriterion(Query query)
    {
        Map<PropertyDescriptor, Boolean> sortCriteria = null;
        if(getAdvancedSearchModel() != null)
        {
            if(query == null || query.getSortCriteria() == null || query.getSortCriteria().isEmpty())
            {
                sortCriteria = Collections.singletonMap(getAdvancedSearchModel().getSortedByProperty(), Boolean.FALSE);
            }
            else if(getAdvancedSearchModel().getSortableProperties() != null &&
                            !getAdvancedSearchModel().getSortableProperties().isEmpty())
            {
                for(Map.Entry<PropertyDescriptor, Boolean> sortCriterion : (Iterable<Map.Entry<PropertyDescriptor, Boolean>>)query.getSortCriteria().entrySet())
                {
                    if(getAdvancedSearchModel().getSortableProperties().contains(sortCriterion.getKey()))
                    {
                        sortCriteria = Collections.singletonMap(sortCriterion.getKey(), sortCriterion.getValue());
                        break;
                    }
                }
            }
        }
        else
        {
            LOG.warn("Advanced search model is null. Make sure the root type has been set.");
        }
        return (sortCriteria == null) ? Collections.EMPTY_MAP : sortCriteria;
    }


    protected SearchType getSearchType()
    {
        SearchType searchType = null;
        if(getRootType() == null)
        {
            LOG.error("Could not get search type (Reason: no root type has been specified).");
        }
        else
        {
            searchType = UISessionUtils.getCurrentSession().getSearchService().getSearchType(getRootType());
        }
        return searchType;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public void addBrowserModelListener(BrowserModelListener listener)
    {
        if(listener instanceof SearchBrowserModelListener)
        {
            super.addBrowserModelListener(listener);
        }
        else
        {
            LOG.warn("Listener not registered (Reason: Not of type '" + SearchBrowserModelListener.class.getCanonicalName() + "').");
        }
    }


    public Set<BrowserFilter> getAvailableBrowserFilters()
    {
        Set<BrowserFilter> availableBrowserFilters = new LinkedHashSet<>();
        availableBrowserFilters.add(null);
        availableBrowserFilters.add(getBrowserFilterFixed());
        availableBrowserFilters.addAll(getConfiguredBrowserFilters());
        return availableBrowserFilters;
    }


    protected Set<BrowserFilter> getConfiguredBrowserFilters()
    {
        BrowserFilterFactory browserFilterFactory = (BrowserFilterFactory)SpringUtil.getBean("browserFilterFactory");
        BaseType baseType = (getAdvancedSearchModel() == null) ? null : ((getAdvancedSearchModel().getSelectedType() == null) ? null : getAdvancedSearchModel().getSelectedType().getBaseType());
        return browserFilterFactory.getBrowserFilters((ObjectType)baseType);
    }
}
