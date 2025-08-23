package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModelListener;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;

public abstract class AbstractPageableBrowserModel extends AbstractAdvancedBrowserModel implements PageableBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPageableBrowserModel.class);
    private List<Integer> pageSizes = new ArrayList<>();
    private int offset = 0;
    private int pageSize = 30;
    private int maxPageSize = 250;
    private boolean simplePaging;
    private boolean contextPgSzInitialized = false;
    private int contextItemsPageSize = -1;
    private int contextItemsPageIndex = 0;


    public AbstractPageableBrowserModel()
    {
        this.pageSizes.add(Integer.valueOf(4));
        this.pageSizes.add(Integer.valueOf(12));
        this.pageSizes.add(Integer.valueOf(30));
        this.pageSizes.add(Integer.valueOf(60));
        this.pageSizes.add(Integer.valueOf(100));
    }


    public void setContextItemsDirectly(TypedObject item, Collection<TypedObject> contextItems)
    {
        setContextItemsPageIndex(0);
        super.setContextItemsDirectly(item, contextItems);
    }


    public boolean addPageSize(int pageSize)
    {
        boolean added = false;
        if(!this.pageSizes.contains(Integer.valueOf(pageSize)) && pageSize > 0 && pageSize != getMaxPageSize())
        {
            this.pageSizes.add(Integer.valueOf(pageSize));
            Collections.sort(this.pageSizes);
            this.pageSize = pageSize;
            added = true;
        }
        return added;
    }


    public int getLastPage()
    {
        int lastPage = 0;
        if(getPageSize() > 0)
        {
            lastPage = (getTotalCount() - 1) / getPageSize();
        }
        return lastPage;
    }


    public int getCurrentPage()
    {
        int currentPage = 0;
        if(getPageSize() > 0)
        {
            currentPage = getOffset() / getPageSize();
        }
        return currentPage;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public List<Integer> getPageSizes()
    {
        return Collections.unmodifiableList(this.pageSizes);
    }


    public int getOffset()
    {
        return this.offset;
    }


    public void setCurrentPage(int currentPage)
    {
        setOffset(currentPage * getPageSize());
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    public void setOffset(int offset)
    {
        this.offset = offset;
    }


    public void updateItems()
    {
        updateItems(getCurrentPage());
    }


    protected void firePagingChanged()
    {
        for(BrowserModelListener listener : this.browserListeners)
        {
            ((PageableBrowserModelListener)listener).pagingChanged(this);
        }
    }


    public void addBrowserModelListener(BrowserModelListener listener)
    {
        if(listener instanceof PageableBrowserModelListener)
        {
            super.addBrowserModelListener(listener);
        }
        else
        {
            LOG.warn("Listener not registered (Reason: Not of type 'PageableBrowserModelListener').");
        }
    }


    public abstract void updateItems(int paramInt);


    public boolean hasStatusBar()
    {
        return true;
    }


    public void setPageSizes(List<Integer> pageSizes)
    {
        this.pageSizes = pageSizes;
    }


    public int getMaxPageSize()
    {
        return this.maxPageSize;
    }


    public void setMaxPageSize(int maxPageSize)
    {
        this.maxPageSize = maxPageSize;
    }


    public void setContextItemsPageSize(int contextItemsPageSize)
    {
        if(this.contextItemsPageSize != contextItemsPageSize)
        {
            this.contextItemsPageSize = contextItemsPageSize;
            setContextItemsPageIndex(0);
        }
    }


    public int getContextItemsPageSize()
    {
        if(!this.contextPgSzInitialized)
        {
            this.contextPgSzInitialized = true;
            this.contextItemsPageSize = this.pageSize;
            String cockpitParameter = UITools.getCockpitParameter("default.contextArea.pageSize", Executions.getCurrent());
            if(cockpitParameter != null)
            {
                try
                {
                    this.contextItemsPageSize = Integer.parseInt(cockpitParameter);
                }
                catch(NumberFormatException e)
                {
                    LOG.warn("Wrong parameter format for key [*]cockpit.default.contextArea.pageSize, expected integer but was '" + cockpitParameter + "'");
                }
            }
        }
        return this.contextItemsPageSize;
    }


    public void setContextItemsPageIndex(int contextItemsPageIndex)
    {
        this.contextItemsPageIndex = contextItemsPageIndex;
    }


    public int getContextItemsPageIndex()
    {
        return this.contextItemsPageIndex;
    }


    public List<TypedObject> getContextItemsPaged()
    {
        List<TypedObject> items = getContextItems();
        int pageOffset = (items.size() < getContextItemsPageSize()) ? 0 : (getContextItemsPageIndex() * getContextItemsPageSize());
        int toIndex = Math.min(pageOffset + getContextItemsPageSize(), items.size());
        return items.subList(pageOffset, toIndex);
    }


    public boolean isSimplePaging()
    {
        return this.simplePaging;
    }


    public void setSimplePaging(boolean simplePaging)
    {
        this.simplePaging = simplePaging;
    }
}
