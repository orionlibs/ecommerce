package de.hybris.platform.b2b.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import java.util.Set;

public class B2BCustomerEmailConstraintModel extends PatternConstraintModel
{
    public static final String _TYPECODE = "B2BCustomerEmailConstraint";


    public B2BCustomerEmailConstraintModel()
    {
    }


    public B2BCustomerEmailConstraintModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCustomerEmailConstraintModel(Class _annotation, Set<RegexpFlag> _flags, String _id, String _regexp)
    {
        setAnnotation(_annotation);
        setFlags(_flags);
        setId(_id);
        setRegexp(_regexp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCustomerEmailConstraintModel(Class _annotation, Set<RegexpFlag> _flags, String _id, ItemModel _owner, String _regexp)
    {
        setAnnotation(_annotation);
        setFlags(_flags);
        setId(_id);
        setOwner(_owner);
        setRegexp(_regexp);
    }
}
