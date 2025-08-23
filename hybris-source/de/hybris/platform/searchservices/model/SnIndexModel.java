package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class SnIndexModel extends ItemModel
{
    public static final String _TYPECODE = "SnIndex";
    public static final String ID = "id";
    public static final String INDEXEROPERATIONS = "indexerOperations";


    public SnIndexModel()
    {
    }


    public SnIndexModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "indexerOperations", type = Accessor.Type.GETTER)
    public List<SnIndexerOperationModel> getIndexerOperations()
    {
        return (List<SnIndexerOperationModel>)getPersistenceContext().getPropertyValue("indexerOperations");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "indexerOperations", type = Accessor.Type.SETTER)
    public void setIndexerOperations(List<SnIndexerOperationModel> value)
    {
        getPersistenceContext().setPropertyValue("indexerOperations", value);
    }
}
