package de.hybris.platform.cockpit.events.impl;

public class SectionModelEvent extends ComponentEvent
{
    public static final String CHANGED_EVENT = "changed";
    public static final String SELECTION_CHANGED_EVENT = "selectionChange";
    public static final String ITEMS_CHANGED_EVENT = "itemsChanged";
    private final Object data;
    private final String name;


    public SectionModelEvent(Object source, String name)
    {
        this(source, name, null);
    }


    public SectionModelEvent(Object source, String name, Object data)
    {
        super(source);
        this.data = data;
        this.name = name;
    }


    public Object getData()
    {
        return this.data;
    }


    public String getName()
    {
        return this.name;
    }
}
