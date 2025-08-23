package de.hybris.platform.servicelayer.stats;

public abstract class AbstractStatisticsCollector implements StatisticsCollector
{
    private String name;
    private String label;
    private String color;
    private boolean enabled;


    public AbstractStatisticsCollector(String name, String label, String color)
    {
        this.label = label;
        this.name = name;
        this.color = color;
        this.enabled = true;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setColor(String color)
    {
        this.color = color;
    }


    public String getColor()
    {
        return this.color;
    }


    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }


    public boolean getEnabled()
    {
        return this.enabled;
    }
}
