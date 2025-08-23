package de.hybris.platform.hmc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.SavedValueEntryType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Set;

public class SavedValuesModel extends ItemModel
{
    public static final String _TYPECODE = "SavedValues";
    public static final String _ITEMSAVEDVALUESRELATION = "ItemSavedValuesRelation";
    public static final String MODIFIEDITEMTYPE = "modifiedItemType";
    public static final String MODIFIEDITEMDISPLAYSTRING = "modifiedItemDisplayString";
    public static final String TIMESTAMP = "timestamp";
    public static final String USER = "user";
    public static final String CHANGEDATTRIBUTES = "changedAttributes";
    public static final String NUMBEROFCHANGEDATTRIBUTES = "numberOfChangedAttributes";
    public static final String MODIFICATIONTYPE = "modificationType";
    public static final String SAVEDVALUESENTRIES = "savedValuesEntries";
    public static final String MODIFIEDITEMPOS = "modifiedItemPOS";
    public static final String MODIFIEDITEM = "modifiedItem";


    public SavedValuesModel()
    {
    }


    public SavedValuesModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedValuesModel(SavedValueEntryType _modificationType, String _modifiedItemDisplayString, ComposedTypeModel _modifiedItemType, Date _timestamp, UserModel _user)
    {
        setModificationType(_modificationType);
        setModifiedItemDisplayString(_modifiedItemDisplayString);
        setModifiedItemType(_modifiedItemType);
        setTimestamp(_timestamp);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedValuesModel(SavedValueEntryType _modificationType, ItemModel _modifiedItem, String _modifiedItemDisplayString, ComposedTypeModel _modifiedItemType, ItemModel _owner, Date _timestamp, UserModel _user)
    {
        setModificationType(_modificationType);
        setModifiedItem(_modifiedItem);
        setModifiedItemDisplayString(_modifiedItemDisplayString);
        setModifiedItemType(_modifiedItemType);
        setOwner(_owner);
        setTimestamp(_timestamp);
        setUser(_user);
    }


    @Accessor(qualifier = "changedAttributes", type = Accessor.Type.GETTER)
    public String getChangedAttributes()
    {
        return (String)getPersistenceContext().getPropertyValue("changedAttributes");
    }


    @Accessor(qualifier = "modificationType", type = Accessor.Type.GETTER)
    public SavedValueEntryType getModificationType()
    {
        return (SavedValueEntryType)getPersistenceContext().getPropertyValue("modificationType");
    }


    @Accessor(qualifier = "modifiedItem", type = Accessor.Type.GETTER)
    public ItemModel getModifiedItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("modifiedItem");
    }


    @Accessor(qualifier = "modifiedItemDisplayString", type = Accessor.Type.GETTER)
    public String getModifiedItemDisplayString()
    {
        return (String)getPersistenceContext().getPropertyValue("modifiedItemDisplayString");
    }


    @Accessor(qualifier = "modifiedItemType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getModifiedItemType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("modifiedItemType");
    }


    @Accessor(qualifier = "numberOfChangedAttributes", type = Accessor.Type.GETTER)
    public Integer getNumberOfChangedAttributes()
    {
        return (Integer)getPersistenceContext().getPropertyValue("numberOfChangedAttributes");
    }


    @Accessor(qualifier = "savedValuesEntries", type = Accessor.Type.GETTER)
    public Set<SavedValueEntryModel> getSavedValuesEntries()
    {
        return (Set<SavedValueEntryModel>)getPersistenceContext().getPropertyValue("savedValuesEntries");
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
    public Date getTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("timestamp");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "modificationType", type = Accessor.Type.SETTER)
    public void setModificationType(SavedValueEntryType value)
    {
        getPersistenceContext().setPropertyValue("modificationType", value);
    }


    @Accessor(qualifier = "modifiedItem", type = Accessor.Type.SETTER)
    public void setModifiedItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("modifiedItem", value);
    }


    @Accessor(qualifier = "modifiedItemDisplayString", type = Accessor.Type.SETTER)
    public void setModifiedItemDisplayString(String value)
    {
        getPersistenceContext().setPropertyValue("modifiedItemDisplayString", value);
    }


    @Accessor(qualifier = "modifiedItemType", type = Accessor.Type.SETTER)
    public void setModifiedItemType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("modifiedItemType", value);
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
    public void setTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("timestamp", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
