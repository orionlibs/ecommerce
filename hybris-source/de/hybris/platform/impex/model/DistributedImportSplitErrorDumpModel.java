package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DistributedImportSplitErrorDumpModel extends ImportBatchContentModel
{
    public static final String _TYPECODE = "DistributedImportSplitErrorDump";
    public static final String PROCESSCODE = "processCode";


    public DistributedImportSplitErrorDumpModel()
    {
    }


    public DistributedImportSplitErrorDumpModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedImportSplitErrorDumpModel(String _code, String _content, String _processCode)
    {
        setCode(_code);
        setContent(_content);
        setProcessCode(_processCode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedImportSplitErrorDumpModel(String _code, String _content, ItemModel _owner, String _processCode)
    {
        setCode(_code);
        setContent(_content);
        setOwner(_owner);
        setProcessCode(_processCode);
    }


    @Accessor(qualifier = "processCode", type = Accessor.Type.GETTER)
    public String getProcessCode()
    {
        return (String)getPersistenceContext().getPropertyValue("processCode");
    }


    @Accessor(qualifier = "processCode", type = Accessor.Type.SETTER)
    public void setProcessCode(String value)
    {
        getPersistenceContext().setPropertyValue("processCode", value);
    }
}
