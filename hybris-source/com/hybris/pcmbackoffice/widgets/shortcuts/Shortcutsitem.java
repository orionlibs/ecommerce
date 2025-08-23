package com.hybris.pcmbackoffice.widgets.shortcuts;

import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.cockpitng.util.YTestTools;
import org.apache.commons.lang3.ArrayUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class Shortcutsitem
{
    private final Listitem listItem;
    private final BackofficeObjectSpecialCollectionModel collectionModel;
    private final ShortcutsFlagEnum flag;
    private static final String LABEL = "shortcuts.label.";


    public Shortcutsitem(Listbox parent, ShortcutsFlagEnum flag, BackofficeObjectSpecialCollectionModel collectionModel)
    {
        String label = Labels.getLabel(String.format("%s%s", new Object[] {"shortcuts.label.", flag.getCode()}), ArrayUtils.toArray((Object[])new Integer[] {Integer.valueOf(0)}));
        this.listItem = new Listitem(label);
        YTestTools.modifyYTestId((Component)this.listItem, flag.getCode());
        parent.appendChild((Component)this.listItem);
        this.flag = flag;
        this.collectionModel = collectionModel;
    }


    public Shortcutsitem addEventListener(String eventType, EventListener<Event> eventListener)
    {
        this.listItem.addEventListener(eventType, eventListener);
        return this;
    }


    public Listitem getListItem()
    {
        return this.listItem;
    }


    public BackofficeObjectSpecialCollectionModel getCollectionModel()
    {
        return this.collectionModel;
    }


    public ShortcutsFlagEnum getFlag()
    {
        return this.flag;
    }


    public Shortcutsitem enableDrop(String droppables)
    {
        this.listItem.setDroppable(droppables);
        return this;
    }


    public void disableDrop()
    {
        this.listItem.setDroppable("false");
    }
}
