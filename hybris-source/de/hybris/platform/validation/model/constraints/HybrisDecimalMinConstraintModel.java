package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;

public class HybrisDecimalMinConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "HybrisDecimalMinConstraint";
    public static final String VALUE = "value";


    public HybrisDecimalMinConstraintModel()
    {
    }


    public HybrisDecimalMinConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public HybrisDecimalMinConstraintModel(Class _annotation, String _id, BigDecimal _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public HybrisDecimalMinConstraintModel(Class _annotation, String _id, ItemModel _owner, BigDecimal _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
        setValue(_value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public BigDecimal getValue()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
