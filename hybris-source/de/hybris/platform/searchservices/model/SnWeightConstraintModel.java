package de.hybris.platform.searchservices.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

public class SnWeightConstraintModel extends AttributeConstraintModel
{
    public static final String _TYPECODE = "SnWeightConstraint";


    public SnWeightConstraintModel()
    {
    }


    public SnWeightConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnWeightConstraintModel(Class _annotation, String _id)
    {
        setAnnotation(_annotation);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnWeightConstraintModel(Class _annotation, String _id, ItemModel _owner)
    {
        setAnnotation(_annotation);
        setId(_id);
        setOwner(_owner);
    }
}
