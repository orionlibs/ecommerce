package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

public class MaxConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "MaxConstraint";
    public static final String VALUE = "value";


    public MaxConstraintModel()
    {
    }


    public MaxConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MaxConstraintModel(Class _annotation, String _id, Long _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MaxConstraintModel(Class _annotation, String _id, ItemModel _owner, Long _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
        setValue(_value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Long getValue()
    {
        return (Long)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Long value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
