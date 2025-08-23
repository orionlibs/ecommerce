package de.hybris.platform.impex.jalo.imp;

import java.util.HashMap;
import java.util.Map;

class ParamNames
{
    private final Map<String, Integer> counters = new HashMap<>();


    public String getNameFor(String name)
    {
        Integer counter = this.counters.get(name);
        if(counter == null)
        {
            this.counters.put(name, Integer.valueOf(1));
            return name;
        }
        this.counters.put(name, Integer.valueOf(counter.intValue() + 1));
        return name + "_" + name;
    }
}
