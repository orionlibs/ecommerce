package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ImportBatchModel extends BatchModel
{
    public static final String _TYPECODE = "ImportBatch";
    public static final String GROUP = "group";
    public static final String METADATA = "metadata";
    public static final String IMPORTCONTENTCODE = "importContentCode";
    public static final String IMPORTBATCHCONTENT = "importBatchContent";


    public ImportBatchModel()
    {
    }


    public ImportBatchModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImportBatchModel(String _executionId, String _id, DistributedProcessModel _process, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setProcess(_process);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImportBatchModel(String _executionId, String _id, String _importContentCode, String _metadata, ItemModel _owner, DistributedProcessModel _process, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setImportContentCode(_importContentCode);
        setMetadata(_metadata);
        setOwner(_owner);
        setProcess(_process);
        setType(_type);
    }


    @Accessor(qualifier = "group", type = Accessor.Type.GETTER)
    public int getGroup()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("group"));
    }


    @Accessor(qualifier = "importBatchContent", type = Accessor.Type.GETTER)
    public ImportBatchContentModel getImportBatchContent()
    {
        return (ImportBatchContentModel)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "importBatchContent");
    }


    @Accessor(qualifier = "importContentCode", type = Accessor.Type.GETTER)
    public String getImportContentCode()
    {
        return (String)getPersistenceContext().getPropertyValue("importContentCode");
    }


    @Accessor(qualifier = "metadata", type = Accessor.Type.GETTER)
    public String getMetadata()
    {
        return (String)getPersistenceContext().getPropertyValue("metadata");
    }


    @Accessor(qualifier = "group", type = Accessor.Type.SETTER)
    public void setGroup(int value)
    {
        getPersistenceContext().setPropertyValue("group", toObject(value));
    }


    @Accessor(qualifier = "importContentCode", type = Accessor.Type.SETTER)
    public void setImportContentCode(String value)
    {
        getPersistenceContext().setPropertyValue("importContentCode", value);
    }


    @Accessor(qualifier = "metadata", type = Accessor.Type.SETTER)
    public void setMetadata(String value)
    {
        getPersistenceContext().setPropertyValue("metadata", value);
    }
}
