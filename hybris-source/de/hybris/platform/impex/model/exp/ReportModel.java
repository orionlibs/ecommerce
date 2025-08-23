package de.hybris.platform.impex.model.exp;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ReportModel extends ExportModel
{
    public static final String _TYPECODE = "Report";


    public ReportModel()
    {
    }


    public ReportModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReportModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReportModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }
}
