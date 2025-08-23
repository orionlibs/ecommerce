package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public abstract class AbstractFacetChartFilterAdapter
{
    protected void deleteFilterFromModel(String nameOfDeletedItem, String selectedFilterValue, WidgetInstanceManager widgetInstanceManager, Executable onDeletedCallback)
    {
        if(widgetInstanceManager.getModel() == null)
        {
            return;
        }
        Set selectedFilterValues = (Set)widgetInstanceManager.getModel().getValue(selectedFilterValue, Set.class);
        if(CollectionUtils.isNotEmpty(selectedFilterValues))
        {
            Iterator iterator = selectedFilterValues.iterator();
            while(iterator.hasNext())
            {
                Object filterValue = iterator.next();
                boolean canDelete = canDelete(filterValue, nameOfDeletedItem);
                if(canDelete)
                {
                    iterator.remove();
                    onDeletedCallback.execute();
                }
            }
        }
    }


    abstract boolean canDelete(Object paramObject, String paramString);
}
