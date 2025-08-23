package de.hybris.platform.validation.model.constraints;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class NotEmptyConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "NotEmptyConstraint";


    public NotEmptyConstraintModel()
    {
    }


    public NotEmptyConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NotEmptyConstraintModel(Class _annotation, String _id)
    {
        setAnnotation(_annotation);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NotEmptyConstraintModel(Class _annotation, String _id, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
    }
}
