package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

public class AssertTrueConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "AssertTrueConstraint";


    public AssertTrueConstraintModel()
    {
    }


    public AssertTrueConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AssertTrueConstraintModel(Class _annotation, String _id)
    {
        setAnnotation(_annotation);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AssertTrueConstraintModel(Class _annotation, String _id, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
    }
}
