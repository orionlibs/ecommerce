package de.hybris.platform.cmscockpit.components.editorarea.model;

public class SimpleSectionRowModel
{
    private String label;
    private boolean editable;
    private boolean visible;
    private Object value;
    private String toolTipText;


    public SimpleSectionRowModel(String label, Object value)
    {
        this.label = label;
        this.value = value;
        this.editable = true;
        this.visible = true;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public Object getValue()
    {
        return this.value;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public String getToolTipText()
    {
        return this.toolTipText;
    }


    public void setToolTipText(String toolTipText)
    {
        this.toolTipText = toolTipText;
    }
}
