package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zk.ui.Component;

public class RowEvent extends SectionPanelEvent
{
    private final Section section;
    private final SectionRow row;
    private int index = -1;


    public RowEvent(String name, SectionPanel target, Section section, SectionRow sectionRow)
    {
        super(name, (Component)target, sectionRow);
        this.section = section;
        this.row = sectionRow;
    }


    public Section getSection()
    {
        return this.section;
    }


    public SectionRow getRow()
    {
        return this.row;
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
