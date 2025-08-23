package de.hybris.platform.servicelayer.test;

import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class DynamicAttributesEnumSampleBean implements DynamicAttributeHandler<Gender, TestItemType2Model>
{
    public Gender get(TestItemType2Model model)
    {
        return Gender.MALE;
    }


    public void set(TestItemType2Model model, Gender value)
    {
        throw new UnsupportedOperationException("Write is not allowed");
    }
}
