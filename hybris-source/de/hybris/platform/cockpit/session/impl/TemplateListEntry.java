package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public class TemplateListEntry
{
    private ObjectTemplate template;
    private int index;
    private int depth;


    public TemplateListEntry()
    {
    }


    public TemplateListEntry(ObjectTemplate template, int index, int depth)
    {
        this.template = template;
        this.index = index;
        this.depth = depth;
    }


    public ObjectTemplate getTemplate()
    {
        return this.template;
    }


    public int getIndex()
    {
        return this.index;
    }


    public String getLabel()
    {
        return this.template.toString();
    }


    public boolean isAbstract()
    {
        return this.template.isAbstract();
    }


    public int getDepth()
    {
        return this.depth;
    }
}
