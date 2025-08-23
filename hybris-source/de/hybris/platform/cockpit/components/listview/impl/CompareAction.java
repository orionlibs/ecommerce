package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractMultiSelectOnlyAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class CompareAction extends AbstractMultiSelectOnlyAction
{
    private static final Logger LOG = LoggerFactory.getLogger(CompareAction.class);


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        return (getSelectedItemsFromContext(context).size() == 2) ? "/cockpit/images/compare_action.gif" : null;
    }


    public Menupopup getMultiSelectPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return null;
    }


    private Collection<TypedObject> getSelectedItemsFromContext(ListViewAction.Context context)
    {
        List<TypedObject> ret = new ArrayList<>();
        if(context.getListModel() != null)
        {
            List<Integer> selectedIndexes = context.getListModel().getSelectedIndexes();
            for(Integer integer : selectedIndexes)
            {
                try
                {
                    Object object = context.getListModel().getValueAt(integer.intValue());
                    if(object instanceof TypedObject)
                    {
                        ret.add((TypedObject)object);
                        continue;
                    }
                    LOG.error("Selected item not a typed object.");
                }
                catch(Exception e)
                {
                    LOG.error("Could not get selected item.", e);
                }
            }
        }
        else if(context.getBrowserModel() != null)
        {
            return context.getBrowserModel().getSelectedItems();
        }
        return ret;
    }
}
