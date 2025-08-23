package de.hybris.platform.cockpit.services.config;

public interface MutableColumnConfiguration extends ColumnConfiguration
{
    void setName(String paramString);


    void setVisible(boolean paramBoolean);


    void setSortable(boolean paramBoolean);


    void setEditable(boolean paramBoolean);


    void setSelectable(boolean paramBoolean);


    void setEditor(String paramString);


    void setWidth(String paramString);
}
