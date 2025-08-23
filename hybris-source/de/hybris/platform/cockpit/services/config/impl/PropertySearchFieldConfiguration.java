package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultSearchField;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PropertySearchFieldConfiguration implements SearchFieldConfiguration
{
    private final PropertyDescriptor propertyDescriptor;
    private DefaultSearchField searchField = null;
    private boolean sortable;
    private boolean visible = false;
    private boolean sortDisabled = true;
    private String name = null;
    private String label = null;
    private String editor;
    private Map<String, String> parameters = null;
    private List<EditorConditionEntry> conditionEntries;
    private SearchFieldConfiguration.EntryListMode entryListMode;


    public PropertySearchFieldConfiguration(PropertyDescriptor propertyDescriptor)
    {
        if(propertyDescriptor == null)
        {
            throw new IllegalArgumentException("PropertyDescriptor can not be null.");
        }
        this.propertyDescriptor = propertyDescriptor;
        loadValues();
    }


    private void loadValues()
    {
        String qualifier = this.propertyDescriptor.getQualifier();
        this.name = qualifier;
        this.label = this.propertyDescriptor.getName();
        PropertyDescriptor.Occurrence occurrence = this.propertyDescriptor.getOccurence();
        if(occurrence.equals(PropertyDescriptor.Occurrence.REQUIRED))
        {
            this.visible = true;
        }
        else
        {
            this.visible = false;
        }
        this.sortable = false;
        String[] sortableQualifiers = {"product.code", "product.name", "item.pk"};
        for(String sortableQualifier : sortableQualifiers)
        {
            if(sortableQualifier.equalsIgnoreCase(qualifier))
            {
                this.sortable = true;
                break;
            }
        }
        this.sortDisabled = true;
    }


    public String getName()
    {
        return this.name;
    }


    public DefaultSearchField getSearchField()
    {
        if(this.searchField == null)
        {
            this.searchField = new DefaultSearchField(this.name, this.sortable);
            this.searchField.setLabel(this.label);
            this.searchField.setVisible(this.visible);
            this.searchField.setSortDisabled(this.sortDisabled);
        }
        return this.searchField;
    }


    public boolean isSortDisabled()
    {
        return this.sortDisabled;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setSortDisabled(boolean sortDisabled)
    {
        this.sortDisabled = sortDisabled;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public String getEditor()
    {
        return this.editor;
    }


    public void setEditor(String editor)
    {
        this.editor = editor;
    }


    public void setParameters(Map<String, String> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, String> getParameters()
    {
        return this.parameters;
    }


    public List<EditorConditionEntry> getConditionEntries()
    {
        return (this.conditionEntries == null) ? Collections.EMPTY_LIST : this.conditionEntries;
    }


    public SearchFieldConfiguration.EntryListMode getEntryListMode()
    {
        return this.entryListMode;
    }


    public void setConditionEntries(List<EditorConditionEntry> conditionEntries)
    {
        this.conditionEntries = conditionEntries;
    }


    public void setEntryListMode(SearchFieldConfiguration.EntryListMode entryListMode)
    {
        this.entryListMode = entryListMode;
    }
}
