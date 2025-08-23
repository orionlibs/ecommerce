package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zk.ui.Component;

public class SectionChangeEvent extends SectionEvent
{
    private String label;


    public SectionChangeEvent(String name, Component target, Section section)
    {
        super(name, target, section);
        this.label = section.getLabel();
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }
}
