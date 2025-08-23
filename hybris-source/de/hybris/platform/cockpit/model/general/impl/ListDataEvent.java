package de.hybris.platform.cockpit.model.general.impl;

import de.hybris.platform.cockpit.model.general.ListModel;

public class ListDataEvent
{
    public static final int CONTENTS_CHANGED = 0;
    public static final int INTERVAL_ADDED = 1;
    public static final int INTERVAL_REMOVED = 2;
    private final ListModel<?> model;
    private final int type;
    private final int index0;
    private final int index1;


    public ListDataEvent(ListModel<?> model, int type, int index0, int index1)
    {
        if(model == null)
        {
            throw new IllegalArgumentException("Model can not be null");
        }
        this.model = model;
        this.type = type;
        this.index0 = index0;
        this.index1 = index1;
    }


    public ListModel<?> getModel()
    {
        return this.model;
    }


    public int getType()
    {
        return this.type;
    }


    public int getIndex0()
    {
        return this.index0;
    }


    public int getIndex1()
    {
        return this.index1;
    }
}
