package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SnIndexerOperationModel extends ItemModel
{
    public static final String _TYPECODE = "SnIndexerOperation";
    public static final String _SNINDEX2INDEXEROPERATION = "SnIndex2IndexerOperation";
    public static final String ID = "id";
    public static final String INDEX = "index";


    public SnIndexerOperationModel()
    {
    }


    public SnIndexerOperationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexerOperationModel(String _id, SnIndexModel _index)
    {
        setId(_id);
        setIndex(_index);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexerOperationModel(String _id, SnIndexModel _index, ItemModel _owner)
    {
        setId(_id);
        setIndex(_index);
        setOwner(_owner);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "index", type = Accessor.Type.GETTER)
    public SnIndexModel getIndex()
    {
        return (SnIndexModel)getPersistenceContext().getPropertyValue("index");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "index", type = Accessor.Type.SETTER)
    public void setIndex(SnIndexModel value)
    {
        getPersistenceContext().setPropertyValue("index", value);
    }
}
