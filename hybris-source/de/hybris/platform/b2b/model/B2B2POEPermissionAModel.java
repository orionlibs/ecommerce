package de.hybris.platform.b2b.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class B2B2POEPermissionAModel extends B2BPermissionModel
{
    public static final String _TYPECODE = "B2B2POEPermissionA";


    public B2B2POEPermissionAModel()
    {
    }


    public B2B2POEPermissionAModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2B2POEPermissionAModel(B2BUnitModel _Unit, String _code)
    {
        setUnit(_Unit);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2B2POEPermissionAModel(B2BUnitModel _Unit, String _code, ItemModel _owner)
    {
        setUnit(_Unit);
        setCode(_code);
        setOwner(_owner);
    }
}
