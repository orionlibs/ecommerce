package de.hybris.platform.validation.model.constraints;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class TypeConstraintModel extends AbstractConstraintModel
{
    public static final String _TYPECODE = "TypeConstraint";


    public TypeConstraintModel()
    {
    }


    public TypeConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypeConstraintModel(Class _annotation, String _id)
    {
        setAnnotation(_annotation);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TypeConstraintModel(Class _annotation, String _id, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
    }
}
