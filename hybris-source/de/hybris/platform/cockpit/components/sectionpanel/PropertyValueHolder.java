package de.hybris.platform.cockpit.components.sectionpanel;

public class PropertyValueHolder extends DefaultSectionRow
{
    private Object value;
    private boolean localized;


    public PropertyValueHolder(String label)
    {
        super(label);
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public Object getValue()
    {
        return this.value;
    }


    public void setLocalized(boolean localized)
    {
        this.localized = localized;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }
}
