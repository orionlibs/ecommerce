package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

public class SizeConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "SizeConstraint";
    public static final String MIN = "min";
    public static final String MAX = "max";


    public SizeConstraintModel()
    {
    }


    public SizeConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SizeConstraintModel(Class _annotation, String _id, Long _max, Long _min)
    {
        setAnnotation(_annotation);
        setId(_id);
        setMax(_max);
        setMin(_min);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SizeConstraintModel(Class _annotation, String _id, Long _max, Long _min, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setId(_id);
        setMax(_max);
        setMin(_min);
        setOwner(_owner);
    }


    @Accessor(qualifier = "max", type = Accessor.Type.GETTER)
    public Long getMax()
    {
        return (Long)getPersistenceContext().getPropertyValue("max");
    }


    @Accessor(qualifier = "min", type = Accessor.Type.GETTER)
    public Long getMin()
    {
        return (Long)getPersistenceContext().getPropertyValue("min");
    }


    @Accessor(qualifier = "max", type = Accessor.Type.SETTER)
    public void setMax(Long value)
    {
        getPersistenceContext().setPropertyValue("max", value);
    }


    @Accessor(qualifier = "min", type = Accessor.Type.SETTER)
    public void setMin(Long value)
    {
        getPersistenceContext().setPropertyValue("min", value);
    }
}
