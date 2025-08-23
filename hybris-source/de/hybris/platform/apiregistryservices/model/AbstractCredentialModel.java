package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractCredentialModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractCredential";
    public static final String ID = "id";


    public AbstractCredentialModel()
    {
    }


    public AbstractCredentialModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCredentialModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCredentialModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }
}
