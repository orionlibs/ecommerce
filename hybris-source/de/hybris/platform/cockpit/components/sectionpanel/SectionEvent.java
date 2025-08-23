package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zk.ui.Component;

public class SectionEvent extends SectionPanelEvent
{
    private final Section section;
    private int index = -1;


    public SectionEvent(String name, Component target, Section section)
    {
        super(name, target, section);
        this.section = section;
    }


    public Section getSection()
    {
        return this.section;
    }


    public int getIndex()
    {
        return this.index;
    }


    public void setIndex(int index)
    {
        this.index = index;
    }
}
