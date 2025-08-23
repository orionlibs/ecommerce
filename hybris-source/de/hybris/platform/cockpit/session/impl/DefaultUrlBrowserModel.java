package de.hybris.platform.cockpit.session.impl;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;

public class DefaultUrlBrowserModel extends AbstractUrlBrowserModel
{
    public DefaultUrlBrowserModel(List<UrlMainAreaComponentFactory> viewModes)
    {
        super(viewModes);
    }


    public Object clone() throws CloneNotSupportedException
    {
        DefaultUrlBrowserModel ret = new DefaultUrlBrowserModel(getAvailableViewModes());
        ret.setViewMode(getViewMode());
        return ret;
    }


    public String getViewMode()
    {
        String viewMode = super.getViewMode();
        if(StringUtils.isBlank(viewMode))
        {
            Iterator<UrlMainAreaComponentFactory> iterator = getAvailableViewModes().iterator();
            if(iterator.hasNext())
            {
                viewMode = ((UrlMainAreaComponentFactory)iterator.next()).getViewModeID();
            }
        }
        return viewMode;
    }


    public String getLabel()
    {
        return Labels.getLabel(getArea().getPerspective().getUid());
    }


    public String getExtendedLabel()
    {
        return getLabel();
    }


    public boolean hasStatusBar()
    {
        return true;
    }
}
