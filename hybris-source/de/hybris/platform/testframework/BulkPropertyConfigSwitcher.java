package de.hybris.platform.testframework;

import java.util.ArrayList;
import java.util.List;

public class BulkPropertyConfigSwitcher
{
    private final List<PropertyConfigSwitcher> switchers = new ArrayList<>();


    public void switchToValue(String key, String value)
    {
        PropertyConfigSwitcher switcher = new PropertyConfigSwitcher(key);
        switcher.switchToValue(value);
        this.switchers.add(switcher);
    }


    public void switchAllBack()
    {
        for(PropertyConfigSwitcher switcher : this.switchers)
        {
            switcher.switchBackToDefault();
        }
    }
}
