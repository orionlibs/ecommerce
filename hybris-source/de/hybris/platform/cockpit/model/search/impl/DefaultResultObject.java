package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.impl.DefaultTypedObject;
import de.hybris.platform.cockpit.model.search.ResultObject;

@Deprecated
public class DefaultResultObject extends DefaultTypedObject implements ResultObject
{
    private final double score;


    public DefaultResultObject(BaseType type, Object object)
    {
        this(type, object, 1.0D);
    }


    public DefaultResultObject(BaseType type, Object object, double score)
    {
        super(type, object);
        this.score = score;
    }


    public double getScore()
    {
        return this.score;
    }
}
