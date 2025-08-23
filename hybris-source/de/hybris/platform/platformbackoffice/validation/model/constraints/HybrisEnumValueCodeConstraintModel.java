package de.hybris.platform.platformbackoffice.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

public class HybrisEnumValueCodeConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "HybrisEnumValueCodeConstraint";
    public static final String VALUE = "value";


    public HybrisEnumValueCodeConstraintModel()
    {
    }


    public HybrisEnumValueCodeConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public HybrisEnumValueCodeConstraintModel(Class _annotation, String _id, String _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public HybrisEnumValueCodeConstraintModel(Class _annotation, String _id, ItemModel _owner, String _value)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
        setValue(_value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public String getValue()
    {
        return (String)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(String value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
