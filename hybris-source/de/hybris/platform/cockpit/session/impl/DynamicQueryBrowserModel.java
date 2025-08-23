package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CompareMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultPageableContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.GridMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.ListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.query.DynamicQueryService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class DynamicQueryBrowserModel extends CollectionBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DynamicQueryBrowserModel.class);
    private transient DynamicQuery dynamicQuery = null;
    private DynamicQueryService predefineQueryService;


    public DynamicQueryBrowserModel()
    {
        setItemsMovable(true);
        setItemsRemovable(true);
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new DefaultPageableContentBrowser();
    }


    public String getLabel()
    {
        String ret = (this.dynamicQuery != null) ? this.dynamicQuery.getLabel() : "";
        if(getBrowserFilterFixed() != null)
        {
            ret = ret + " - " + ret;
        }
        return ret;
    }


    public void updateItems(int activePage)
    {
        clearSelection();
        setCurrentPage(activePage);
        if(this.dynamicQuery != null)
        {
            List<TypedObject> matchedElements = getPredefinedQueryService().getDynamicQueryResults(this.dynamicQuery);
            int totalCount = matchedElements.size();
            int fromIndex = Math.max(0, getOffset());
            int toIndex = Math.min(getPageSize() + fromIndex, totalCount);
            this.items = new ArrayList(matchedElements.subList(fromIndex, toIndex));
            setTotalCount(totalCount);
            fireChanged();
        }
    }


    public List<TypedObject> getItems()
    {
        return (this.items == null) ? Collections.EMPTY_LIST : this.items;
    }


    public ObjectTemplate getRootType()
    {
        ObjectTemplate rootType = null;
        int size = getTotalCount();
        if(size > 0 && getOffset() < size)
        {
            TypedObject item = getItem(getOffset());
            if(item != null)
            {
                rootType = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(item);
            }
        }
        if(rootType == null)
        {
            rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
        }
        return rootType;
    }


    public TypedObject getItem(int index)
    {
        if(index < getCurrentPage() * getPageSize() || index >= (getCurrentPage() + 1) * getPageSize())
        {
            setCurrentPage(index / getPageSize());
            updateItems(getCurrentPage());
        }
        if(getItems().size() > index % getPageSize())
        {
            return getItems().get(index % getPageSize());
        }
        return null;
    }


    public void setDynamicQuery(DynamicQuery dynamicQuery)
    {
        if(this.dynamicQuery != dynamicQuery)
        {
            this.dynamicQuery = dynamicQuery;
        }
    }


    public DynamicQuery getDynamicQuery()
    {
        return this.dynamicQuery;
    }


    public DynamicQueryService getPredefinedQueryService()
    {
        if(this.predefineQueryService == null)
        {
            try
            {
                this.predefineQueryService = (DynamicQueryService)SpringUtil.getBean("dynamicQueryService");
            }
            catch(Exception e)
            {
                LOG.error("Could not get service for dynamic query!", e);
            }
        }
        return this.predefineQueryService;
    }


    public Object clone() throws CloneNotSupportedException
    {
        DynamicQueryBrowserModel browserModel = new DynamicQueryBrowserModel();
        browserModel.setOffset(getOffset());
        browserModel.setPageSize(getPageSize());
        browserModel.setTotalCount(getTotalCount());
        browserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        browserModel.setBrowserFilter(getBrowserFilter());
        browserModel.setDynamicQuery(this.dynamicQuery);
        browserModel.updateItems(browserModel.getCurrentPage());
        browserModel.setSimplePaging(isSimplePaging());
        return browserModel;
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList();
            this.viewModes.add(new GridMainAreaComponentFactory());
            this.viewModes.add(new ListMainAreaComponentFactory());
            this.viewModes.add(new CompareMainAreaComponentFactory());
        }
        return this.viewModes;
    }
}
