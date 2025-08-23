package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

public class DigitsConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "DigitsConstraint";
    public static final String INTEGER = "integer";
    public static final String FRACTION = "fraction";


    public DigitsConstraintModel()
    {
    }


    public DigitsConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DigitsConstraintModel(Class _annotation, Integer _fraction, String _id, Integer _integer)
    {
        setAnnotation(_annotation);
        setFraction(_fraction);
        setId(_id);
        setInteger(_integer);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DigitsConstraintModel(Class _annotation, Integer _fraction, String _id, Integer _integer, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setFraction(_fraction);
        setId(_id);
        setInteger(_integer);
        setOwner(_owner);
    }


    @Accessor(qualifier = "fraction", type = Accessor.Type.GETTER)
    public Integer getFraction()
    {
        return (Integer)getPersistenceContext().getPropertyValue("fraction");
    }


    @Accessor(qualifier = "integer", type = Accessor.Type.GETTER)
    public Integer getInteger()
    {
        return (Integer)getPersistenceContext().getPropertyValue("integer");
    }


    @Accessor(qualifier = "fraction", type = Accessor.Type.SETTER)
    public void setFraction(Integer value)
    {
        getPersistenceContext().setPropertyValue("fraction", value);
    }


    @Accessor(qualifier = "integer", type = Accessor.Type.SETTER)
    public void setInteger(Integer value)
    {
        getPersistenceContext().setPropertyValue("integer", value);
    }
}
