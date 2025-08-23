package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import java.math.BigDecimal;

public class DecimalMaxConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "DecimalMaxConstraint";
    public static final String VALUE = "value";
    public static final String INCLUSIVE = "inclusive";


    public DecimalMaxConstraintModel()
    {
    }


    public DecimalMaxConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DecimalMaxConstraintModel(Class _annotation, String _id, BigDecimal _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DecimalMaxConstraintModel(Class _annotation, String _id, ItemModel _owner, BigDecimal _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
        setValue(_value);
    }


    @Accessor(qualifier = "inclusive", type = Accessor.Type.GETTER)
    public Boolean getInclusive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("inclusive");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public BigDecimal getValue()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "inclusive", type = Accessor.Type.SETTER)
    public void setInclusive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("inclusive", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
