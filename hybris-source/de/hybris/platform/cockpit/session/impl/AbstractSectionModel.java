package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.events.impl.SectionModelEvent;
import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import de.hybris.platform.cockpit.session.SectionModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSectionModel implements SectionModel
{
    protected boolean modified = false;
    private final List<SectionModelListener> sectionListeners = new ArrayList<>();
    protected Object rootItem = null;


    public AbstractSectionModel(Object rootItem)
    {
        this.rootItem = rootItem;
    }


    public void addSectionModelListener(SectionModelListener listener)
    {
        if(listener != null && !this.sectionListeners.contains(listener))
        {
            this.sectionListeners.add(listener);
        }
    }


    public void removeSectionModelListener(SectionModelListener listener)
    {
        this.sectionListeners.remove(listener);
    }


    protected void fireEvent(SectionModelEvent event)
    {
        List<SectionModelListener> smListeners = new LinkedList<>(this.sectionListeners);
        for(SectionModelListener listener : smListeners)
        {
            listener.onSectionModelEvent(event);
        }
        this.modified = false;
    }


    public void setRootItem(Object rootItem)
    {
        this.rootItem = rootItem;
    }


    public Object getRootItem()
    {
        return this.rootItem;
    }


    public String toString()
    {
        return getClass().getSimpleName() + "[label='" + getClass().getSimpleName() + "', itemCount='" + getLabel() + "']";
    }


    public void setSectionModelListeners(List<SectionModelListener> sectionListeners)
    {
        this.sectionListeners.clear();
        if(sectionListeners != null)
        {
            this.sectionListeners.addAll(sectionListeners);
        }
    }


    public List<SectionModelListener> getSectionModelListeners()
    {
        return (this.sectionListeners == null || this.sectionListeners.isEmpty()) ? Collections.EMPTY_LIST :
                        Collections.<SectionModelListener>unmodifiableList(this.sectionListeners);
    }
}
