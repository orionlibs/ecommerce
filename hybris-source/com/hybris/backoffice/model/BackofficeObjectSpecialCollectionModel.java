package com.hybris.backoffice.model;

import com.hybris.backoffice.enums.BackofficeSpecialCollectionType;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class BackofficeObjectSpecialCollectionModel extends ItemModel
{
    public static final String _TYPECODE = "BackofficeObjectSpecialCollection";
    public static final String _USER2BACKOFFICEOBJECTCOLLECTIONRELATION = "User2BackofficeObjectCollectionRelation";
    public static final String COLLECTIONTYPE = "collectionType";
    public static final String USER = "user";
    public static final String ELEMENTS = "elements";


    public BackofficeObjectSpecialCollectionModel()
    {
    }


    public BackofficeObjectSpecialCollectionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeObjectSpecialCollectionModel(BackofficeSpecialCollectionType _collectionType, UserModel _user)
    {
        setCollectionType(_collectionType);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeObjectSpecialCollectionModel(BackofficeSpecialCollectionType _collectionType, ItemModel _owner, UserModel _user)
    {
        setCollectionType(_collectionType);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "collectionType", type = Accessor.Type.GETTER)
    public BackofficeSpecialCollectionType getCollectionType()
    {
        return (BackofficeSpecialCollectionType)getPersistenceContext().getPropertyValue("collectionType");
    }


    @Accessor(qualifier = "elements", type = Accessor.Type.GETTER)
    public List<BackofficeObjectCollectionItemReferenceModel> getElements()
    {
        return (List<BackofficeObjectCollectionItemReferenceModel>)getPersistenceContext().getPropertyValue("elements");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "collectionType", type = Accessor.Type.SETTER)
    public void setCollectionType(BackofficeSpecialCollectionType value)
    {
        getPersistenceContext().setPropertyValue("collectionType", value);
    }


    @Accessor(qualifier = "elements", type = Accessor.Type.SETTER)
    public void setElements(List<BackofficeObjectCollectionItemReferenceModel> value)
    {
        getPersistenceContext().setPropertyValue("elements", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
