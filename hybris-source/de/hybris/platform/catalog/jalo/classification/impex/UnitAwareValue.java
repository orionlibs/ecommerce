package de.hybris.platform.catalog.jalo.classification.impex;

import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;

public class UnitAwareValue
{
    private final Object value;
    private final ClassificationAttributeUnit unit;


    public UnitAwareValue(Object value, ClassificationAttributeUnit unit)
    {
        this.value = value;
        this.unit = unit;
    }


    public Object getValue()
    {
        return this.value;
    }


    public ClassificationAttributeUnit getUnit()
    {
        return this.unit;
    }


    public boolean hasUnit()
    {
        return (this.unit != null);
    }
}
