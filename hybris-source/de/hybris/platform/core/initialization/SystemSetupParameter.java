package de.hybris.platform.core.initialization;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SystemSetupParameter
{
    private final String key;
    private final Map<String, Boolean> values = new LinkedHashMap<>();
    private boolean multiSelect = false;
    private String label;


    public SystemSetupParameter(String key)
    {
        this.key = key;
    }


    public void addValue(String value, boolean selected)
    {
        this.values.put(value, Boolean.valueOf(selected));
    }


    public void setSelected(String value)
    {
        if(this.values.containsKey(value))
        {
            this.values.put(value, Boolean.TRUE);
        }
    }


    public void addValue(String value)
    {
        addValue(value, false);
    }


    public void addValues(String[] values)
    {
        for(String value : values)
        {
            addValue(value);
        }
    }


    public Map<String, Boolean> getValues()
    {
        return this.values;
    }


    public String getKey()
    {
        return this.key;
    }


    public boolean isDefault(String parameter)
    {
        return ((Boolean)this.values.get(parameter)).booleanValue();
    }


    public String[] getDefaults()
    {
        List<String> defaults = new ArrayList<>();
        for(Map.Entry<String, Boolean> isDefault : this.values.entrySet())
        {
            if(((Boolean)isDefault.getValue()).booleanValue())
            {
                defaults.add(isDefault.getKey());
            }
        }
        String[] array = new String[defaults.size()];
        for(int i = 0; i < defaults.size(); i++)
        {
            array[i] = defaults.get(i);
        }
        return array;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        if(this.label == null || this.label.isEmpty())
        {
            return this.key + ":";
        }
        return this.label;
    }


    public void setMultiSelect(boolean multiSelect)
    {
        this.multiSelect = multiSelect;
    }


    public boolean isMultiSelect()
    {
        return this.multiSelect;
    }
}
