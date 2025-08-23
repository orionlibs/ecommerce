package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BasicCredentialModel extends AbstractCredentialModel
{
    public static final String _TYPECODE = "BasicCredential";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";


    public BasicCredentialModel()
    {
    }


    public BasicCredentialModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BasicCredentialModel(String _id, String _password, String _username)
    {
        setId(_id);
        setPassword(_password);
        setUsername(_username);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BasicCredentialModel(String _id, ItemModel _owner, String _password, String _username)
    {
        setId(_id);
        setOwner(_owner);
        setPassword(_password);
        setUsername(_username);
    }


    @Accessor(qualifier = "password", type = Accessor.Type.GETTER)
    public String getPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("password");
    }


    @Accessor(qualifier = "username", type = Accessor.Type.GETTER)
    public String getUsername()
    {
        return (String)getPersistenceContext().getPropertyValue("username");
    }


    @Accessor(qualifier = "password", type = Accessor.Type.SETTER)
    public void setPassword(String value)
    {
        getPersistenceContext().setPropertyValue("password", value);
    }


    @Accessor(qualifier = "username", type = Accessor.Type.SETTER)
    public void setUsername(String value)
    {
        getPersistenceContext().setPropertyValue("username", value);
    }
}
