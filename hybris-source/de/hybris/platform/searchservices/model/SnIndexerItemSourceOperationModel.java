package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class SnIndexerItemSourceOperationModel extends ItemModel
{
    public static final String _TYPECODE = "SnIndexerItemSourceOperation";
    public static final String _SNINCREMENTALINDEXERCRONJOB2INDEXERITEMSOURCEOPERATION = "SnIncrementalIndexerCronJob2IndexerItemSourceOperation";
    public static final String DOCUMENTOPERATIONTYPE = "documentOperationType";
    public static final String INDEXERITEMSOURCE = "indexerItemSource";
    public static final String CRONJOBPOS = "cronJobPOS";
    public static final String CRONJOB = "cronJob";
    public static final String FIELDS = "fields";


    public SnIndexerItemSourceOperationModel()
    {
    }


    public SnIndexerItemSourceOperationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexerItemSourceOperationModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
    public IncrementalSnIndexerCronJobModel getCronJob()
    {
        return (IncrementalSnIndexerCronJobModel)getPersistenceContext().getPropertyValue("cronJob");
    }


    @Accessor(qualifier = "documentOperationType", type = Accessor.Type.GETTER)
    public SnDocumentOperationType getDocumentOperationType()
    {
        return (SnDocumentOperationType)getPersistenceContext().getPropertyValue("documentOperationType");
    }


    @Accessor(qualifier = "fields", type = Accessor.Type.GETTER)
    public List<SnFieldModel> getFields()
    {
        return (List<SnFieldModel>)getPersistenceContext().getPropertyValue("fields");
    }


    @Accessor(qualifier = "indexerItemSource", type = Accessor.Type.GETTER)
    public AbstractSnIndexerItemSourceModel getIndexerItemSource()
    {
        return (AbstractSnIndexerItemSourceModel)getPersistenceContext().getPropertyValue("indexerItemSource");
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
    public void setCronJob(IncrementalSnIndexerCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cronJob", value);
    }


    @Accessor(qualifier = "documentOperationType", type = Accessor.Type.SETTER)
    public void setDocumentOperationType(SnDocumentOperationType value)
    {
        getPersistenceContext().setPropertyValue("documentOperationType", value);
    }


    @Accessor(qualifier = "fields", type = Accessor.Type.SETTER)
    public void setFields(List<SnFieldModel> value)
    {
        getPersistenceContext().setPropertyValue("fields", value);
    }


    @Accessor(qualifier = "indexerItemSource", type = Accessor.Type.SETTER)
    public void setIndexerItemSource(AbstractSnIndexerItemSourceModel value)
    {
        getPersistenceContext().setPropertyValue("indexerItemSource", value);
    }
}
