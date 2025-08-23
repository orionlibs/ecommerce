package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.util.resource.Labels;

public class DefaultSection implements EditableSection
{
    private String label;
    private String localizedLabel;
    private boolean modified;
    private boolean visible;
    private boolean tabbed;
    private boolean initialOpen = true;
    private boolean open = false;
    private boolean editMode = false;


    public DefaultSection()
    {
    }


    public DefaultSection(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public void setLocalizedLabel(String localizedLabel)
    {
        this.localizedLabel = localizedLabel;
    }


    public String getLocalizedLabel()
    {
        return Labels.getLabel(this.localizedLabel);
    }


    public boolean isModified()
    {
        return this.modified;
    }


    public void setModified(boolean modified)
    {
        this.modified = modified;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public void setTabbed(boolean tabbed)
    {
        this.tabbed = tabbed;
    }


    public boolean isTabbed()
    {
        return this.tabbed;
    }


    public boolean isInEditMode()
    {
        return this.editMode;
    }


    public void setInEditMode(boolean value)
    {
        this.editMode = value;
    }


    public boolean isInitialOpen()
    {
        return this.initialOpen;
    }


    public void setInitialOpen(boolean initialOpen)
    {
        this.initialOpen = initialOpen;
    }


    public boolean isOpen()
    {
        return this.open;
    }


    public void setOpen(boolean open)
    {
        this.open = open;
    }
}
