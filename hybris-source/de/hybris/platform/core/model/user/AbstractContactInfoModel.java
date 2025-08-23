package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractContactInfoModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractContactInfo";
    public static final String _USER2CONTACTINFOS = "User2ContactInfos";
    public static final String CODE = "code";
    public static final String USERPOS = "userPOS";
    public static final String USER = "user";


    public AbstractContactInfoModel()
    {
    }


    public AbstractContactInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractContactInfoModel(String _code, UserModel _user)
    {
        setCode(_code);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractContactInfoModel(String _code, ItemModel _owner, UserModel _user)
    {
        setCode(_code);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
