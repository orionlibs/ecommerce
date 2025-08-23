package de.hybris.platform.cockpit.model.config;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyGroup
{
    private final PropertyGroupConfiguration parent;
    private final String qualifier;
    private final Map<String, String> labels = new HashMap<>();
    private boolean visible = true;
    private boolean tabbed = false;
    private final List<PropertyGroupEntry> entries = new ArrayList<>();


    public PropertyGroup(PropertyGroupConfiguration configuration, String qualifier)
    {
        this.qualifier = qualifier;
        this.parent = configuration;
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


    public String getQualifier()
    {
        return this.qualifier;
    }


    public List<PropertyGroupEntry> getEntries()
    {
        return !this.entries.isEmpty() ? Collections.<PropertyGroupEntry>unmodifiableList(this.entries) : Collections.EMPTY_LIST;
    }


    public void addEntry(int pos, PropertyGroupEntry entry)
    {
        this.entries.remove(entry);
        if(pos >= 0)
        {
            this.entries.add(pos, entry);
        }
        else
        {
            this.entries.add(entry);
        }
        entry.setGroup(this);
    }


    public PropertyGroupEntry createEntry(int pos, PropertyDescriptor property, boolean editable)
    {
        PropertyGroupEntry entry = new PropertyGroupEntry(property, editable);
        addEntry(-1, entry);
        return entry;
    }


    public void removeEntry(PropertyGroupEntry entry)
    {
        this.entries.remove(entry);
        entry.setGroup(null);
    }


    public void removeAllEntries()
    {
        for(PropertyGroupEntry e : this.entries)
        {
            e.setGroup(null);
        }
        this.entries.clear();
    }


    public void setAllVisible(boolean visible)
    {
        for(PropertyGroupEntry e : this.entries)
        {
            e.setVisible(visible);
        }
    }


    public List<PropertyGroupEntry> getVisibleEntries()
    {
        if(this.entries == null || this.entries.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<PropertyGroupEntry> ret = new ArrayList<>(this.entries.size());
        for(PropertyGroupEntry e : this.entries)
        {
            if(e.isVisible())
            {
                ret.add(e);
            }
        }
        return ret;
    }


    public String getLabel(String iso)
    {
        return this.labels.get(iso);
    }


    public Map<String, String> getAllLabels()
    {
        return this.labels.isEmpty() ? Collections.EMPTY_MAP : Collections.<String, String>unmodifiableMap(this.labels);
    }


    public void setLabel(String iso, String label)
    {
        this.labels.put(iso, label);
    }


    public void setAllLabels(Map<String, String> labels)
    {
        this.labels.clear();
        if(labels != null)
        {
            this.labels.putAll(labels);
        }
    }


    public PropertyGroupConfiguration getConfiguration()
    {
        return this.parent;
    }
}
