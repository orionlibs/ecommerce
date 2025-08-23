package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.ListModelDataListener;
import de.hybris.platform.cockpit.model.general.impl.ListDataEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractListModel<T> implements ListModel<T>
{
    protected transient List<ListModelDataListener> listEditorDataListeners = null;


    public AbstractListModel()
    {
        this.listEditorDataListeners = new ArrayList<>();
    }


    public void addListModelDataListener(ListModelDataListener listener)
    {
        if(listener != null)
        {
            this.listEditorDataListeners.add(listener);
        }
    }


    public void removeListModelDataListener(ListModelDataListener listener)
    {
        if(listener != null)
        {
            this.listEditorDataListeners.remove(listener);
        }
    }


    protected List<ListModelDataListener> getListDataListeners()
    {
        return this.listEditorDataListeners;
    }


    protected void fireEvent(int type, int index0, int index1)
    {
        ListDataEvent event = new ListDataEvent(this, type, index0, index1);
        for(Iterator<ListModelDataListener> it = this.listEditorDataListeners.iterator(); it.hasNext(); )
        {
            ((ListModelDataListener)it.next()).changed(event);
        }
    }
}
