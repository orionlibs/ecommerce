package de.hybris.platform.servicelayer.test;

import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class DynamicAttributesIntSampleBean implements DynamicAttributeHandler<Integer, TestItemType2Model>
{
    private static final int SUM = 2;


    public Integer get(TestItemType2Model model)
    {
        int tmp = model.getInteger().intValue() + 2;
        return Integer.valueOf(tmp);
    }


    public void set(TestItemType2Model model, Integer value)
    {
        int tmp = value.intValue() - 2;
        model.setInteger(Integer.valueOf(tmp));
    }
}
