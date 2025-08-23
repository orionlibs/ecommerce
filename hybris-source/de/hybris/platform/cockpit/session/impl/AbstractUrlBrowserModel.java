package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UrlBrowserModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractUrlBrowserModel extends AbstractAdvancedBrowserModel implements UrlBrowserModel
{
    private final List<UrlMainAreaComponentFactory> viewModes = new ArrayList<>();


    public AbstractUrlBrowserModel(List<? extends UrlMainAreaComponentFactory> viewModes)
    {
        if(viewModes != null && !viewModes.isEmpty())
        {
            this.viewModes.addAll(viewModes);
        }
    }


    public void reload()
    {
        fireItemsChanged();
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public TypedObject getItem(int index)
    {
        return null;
    }


    public List<TypedObject> getItems()
    {
        return Collections.EMPTY_LIST;
    }


    public void updateItems()
    {
    }


    public List<UrlMainAreaComponentFactory> getAvailableViewModes()
    {
        return Collections.unmodifiableList(this.viewModes);
    }


    public void setAvailableViewModes(List<? extends UrlMainAreaComponentFactory> viewModes)
    {
        this.viewModes.clear();
        if(viewModes != null && !viewModes.isEmpty())
        {
            this.viewModes.addAll(viewModes);
        }
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new MultiViewContentBrowser(this);
    }


    protected UrlMainAreaComponentFactory getCurrentViewMode()
    {
        UrlMainAreaComponentFactory currentMode = null;
        if(getAvailableViewModes() != null)
        {
            for(UrlMainAreaComponentFactory viewFactory : getAvailableViewModes())
            {
                if(StringUtils.isNotBlank(viewFactory.getViewModeID()) && StringUtils.isNotBlank(getViewMode()) && viewFactory
                                .getViewModeID().equalsIgnoreCase(getViewMode()))
                {
                    currentMode = viewFactory;
                    break;
                }
            }
        }
        return currentMode;
    }
}
