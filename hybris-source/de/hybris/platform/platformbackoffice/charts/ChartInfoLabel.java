package de.hybris.platform.platformbackoffice.charts;

public class ChartInfoLabel
{
    private final String label;
    private final String value;


    public ChartInfoLabel(String label, String value)
    {
        this.label = label;
        this.value = value;
    }


    public String getLabel()
    {
        return this.label;
    }


    public String getValue()
    {
        return this.value;
    }
}
