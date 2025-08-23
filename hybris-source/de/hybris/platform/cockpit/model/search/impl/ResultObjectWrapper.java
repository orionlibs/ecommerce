package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.AbstractTypedObject;
import de.hybris.platform.cockpit.model.search.ResultObject;
import java.util.Collection;

public class ResultObjectWrapper extends AbstractTypedObject implements ResultObject
{
    private final double score;
    private final TypedObject wrappedObject;


    public ResultObjectWrapper(TypedObject wrappedObject, Object rawObject)
    {
        this(wrappedObject, rawObject, 1.0D);
    }


    public ResultObjectWrapper(TypedObject wrappedObject, Object rawObject, double score)
    {
        this.wrappedObject = wrappedObject;
        this.score = score;
        this.object = rawObject;
    }


    public double getScore()
    {
        return this.score;
    }


    public BaseType getType()
    {
        return this.wrappedObject.getType();
    }


    public Collection<ExtendedType> getExtendedTypes()
    {
        return this.wrappedObject.getExtendedTypes();
    }


    public Collection<ObjectTemplate> getPotentialTemplates()
    {
        return this.wrappedObject.getPotentialTemplates();
    }


    public Collection<ObjectTemplate> getAssignedTemplates()
    {
        return this.wrappedObject.getAssignedTemplates();
    }


    public Object getObject()
    {
        return this.wrappedObject.getObject();
    }


    public boolean instanceOf(ObjectType type)
    {
        return this.wrappedObject.instanceOf(type);
    }
}
