package de.hybris.platform.servicelayer.stats;

public interface StatisticsCollector
{
    void setName(String paramString);


    String getName();


    void setLabel(String paramString);


    String getLabel();


    void setColor(String paramString);


    String getColor();


    void setEnabled(boolean paramBoolean);


    boolean getEnabled();
}
