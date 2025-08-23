package de.hybris.platform.servicelayer.test;

import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class DynamicAttributesStringSampleBean implements DynamicAttributeHandler<String, TestItemType2Model>
{
    public static final String VALUE_DELIMITER = ",";


    public String get(TestItemType2Model item)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item model is required");
        }
        return item.getFoo() + "," + item.getFoo();
    }


    public void set(TestItemType2Model item, String value)
    {
        if(item != null && value != null)
        {
            String[] split = value.split(",");
            item.setFoo(split[0]);
            item.setBar(split[1]);
        }
    }
}
